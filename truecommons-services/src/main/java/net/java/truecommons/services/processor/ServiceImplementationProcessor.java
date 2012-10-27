/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import static javax.lang.model.element.ElementKind.*;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import static javax.lang.model.element.Modifier.*;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleTypeVisitor6;
import javax.tools.Diagnostic.Kind;
import static javax.tools.Diagnostic.Kind.*;
import javax.tools.FileObject;
import static javax.tools.StandardLocation.*;
import net.java.truecommons.services.ServiceImplementation;
import net.java.truecommons.services.ServiceSpecification;

/**
 * Processes the {@link ServiceImplementation} annotation.
 *
 * @author Christian Schlichtherle
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceImplementationProcessor extends AbstractProcessor {

    private static final Comparator<TypeElement> TYPE_ELEMENT_COMPARATOR =
            new Comparator<TypeElement>() {
                @Override
                public int compare(TypeElement o1, TypeElement o2) {
                    return o1.getQualifiedName().toString().compareTo(
                            o2.getQualifiedName().toString());
                }
            };

    @Override
    public boolean process(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        final Registry registry = new Registry();
        for (final Element e : roundEnv.getElementsAnnotatedWith(ServiceImplementation.class)) {
            final TypeElement impl = (TypeElement) e;
            if (validType(impl)) scan(impl, registry);
        }
        registry.persist();
        return false; // critical!
    }

    private boolean validType(final TypeElement impl) {
        {
            final Set<Modifier> modifiers = impl.getModifiers();
            if (!modifiers.contains(PUBLIC)
                    || modifiers.contains(ABSTRACT)
                    || impl.getKind() != CLASS)
                return invalidType("needs to be a public and non-abstract class", impl);
        }
        final Collection<ExecutableElement>
                consColl = new LinkedList<ExecutableElement>();
        for (final Element e : impl.getEnclosedElements())
            if (e.getKind() == CONSTRUCTOR) consColl.add((ExecutableElement) e);
        if (!consColl.isEmpty() && !validConstructors(consColl))
            return invalidType("needs to have a public constructor with no parameters or no constructor at all", impl);
        return true;
    }

    private boolean invalidType(final String message, final TypeElement impl) {
        processingEnv.getMessager().printMessage(ERROR,
                "An implementation " + message + ".", impl);
        return false;
    }

    private boolean validConstructors(final Collection<ExecutableElement> coll) {
        for (ExecutableElement cons : coll)
            if (validConstructor(cons)) return true;
        return false;
    }

    private boolean validConstructor(final ExecutableElement cons) {
        return cons.getModifiers().contains(PUBLIC)
                && cons.getParameters().isEmpty();
    }

    private void scan(final TypeElement impl, final Registry registry) {

        class Visitor extends SimpleTypeVisitor6<Boolean, Void> {
            Visitor() { super(false); }

            @Override
            public Boolean visitDeclared(DeclaredType t, Void p) {
                boolean found = false;
                final TypeElement spec = (TypeElement) t.asElement();
                if (null != spec.getAnnotation(ServiceSpecification.class)) {
                    found = true;
                    registry.register(spec, impl);
                }
                for (final TypeMirror m : spec.getInterfaces())
                    found |= m.accept(this, p);
                return spec.getSuperclass().accept(this, p) || found;
            }
        } // Visitor

        if (!impl.asType().accept(new Visitor(), null))
            processingEnv.getMessager().printMessage(ERROR, "Cannot find any specification.", impl);
    }

    private final class Registry {
        final Map<TypeElement, Collection<TypeElement>>
            services = new HashMap<TypeElement, Collection<TypeElement>>();

        void register(final TypeElement spec, final TypeElement impl) {
            //processingEnv.getMessager().printMessage(NOTE, "Implements " + spec + ".", impl);
            Collection<TypeElement> coll = services.get(spec);
            if (null == coll) coll = new TreeSet<TypeElement>(TYPE_ELEMENT_COMPARATOR);
            coll.add(impl);
            services.put(spec, coll);
        }

        void persist() {
            final Filer filer = processingEnv.getFiler();
            final Messager messager = processingEnv.getMessager();
            for (final Entry<TypeElement, Collection<TypeElement>> entry : services.entrySet()) {
                final TypeElement spec = entry.getKey();
                final Collection<TypeElement> coll = entry.getValue();
                if (coll.isEmpty()) continue;
                final String relativeName = "META-INF/services/" + spec.getQualifiedName();
                try {
                    final FileObject fo = filer
                            .createResource(CLASS_OUTPUT, "", relativeName);
                    final Writer w = fo.openWriter();
                    try {
                        for (final TypeElement impl : coll) {
                            w.append(impl.getQualifiedName()).append("\n");
                            messager.printMessage(NOTE, String.format("Registered service class at: %s", relativeName), impl);
                        }
                    } finally {
                        w.close();
                    }
                } catch (final IOException ex) {
                    messager.printMessage(ERROR, String.format("Failed to register %d service class(es) at: %s: " , coll.size(), relativeName, ex.getMessage()), spec);
                }
            }
        }
    }
}
