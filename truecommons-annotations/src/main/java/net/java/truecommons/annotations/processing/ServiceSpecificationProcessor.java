/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.annotations.processing;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import net.java.truecommons.annotations.ServiceSpecification;

/**
 * Processes the {@link ServiceSpecification} annotation.
 *
 * @since  TrueCommons 2.1
 * @author Christian Schlichtherle
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
public final class ServiceSpecificationProcessor extends ServiceAnnnotationProcessor {

    @Override
    public boolean process(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        for (final Element e : roundEnv.getElementsAnnotatedWith(ServiceSpecification.class)) {
            final TypeElement spec = (TypeElement) e;
            valid(spec, spec);
        }
        return false; // critical!
    }

    boolean valid(final TypeElement spec, final Element loc) {
        {
            final Set<Modifier> modifiers = spec.getModifiers();
            if (!modifiers.contains(PUBLIC) || modifiers.contains(FINAL))
                return error("Not a public and non-final class or interface.", loc);
            if (spec.getNestingKind().isNested()) {
                if (!modifiers.contains(STATIC))
                    return error("Impossible to implement outside of the lexical scope of the enclosing class.", loc);
                warning("Bad practice: Not a top-level class or interface.", loc);
            }
        }
        final Collection<ExecutableElement> constructors = new LinkedList<>();
        for (final Element elem : spec.getEnclosedElements())
            if (elem.getKind() == CONSTRUCTOR)
                constructors.add((ExecutableElement) elem);
        return constructors.isEmpty() || valid(constructors) ||
                error("No public or protected constructor with zero parameters available.", loc);
    }

    private boolean valid(final Collection<ExecutableElement> constructors) {
        for (final ExecutableElement ctor : constructors)
            if (valid(ctor))
                return true;
        return false;
    }

    private boolean valid(final ExecutableElement ctor) {
        return (ctor.getModifiers().contains(PUBLIC) ||
                ctor.getModifiers().contains(PROTECTED)) &&
                ctor.getParameters().isEmpty();
    }
}
