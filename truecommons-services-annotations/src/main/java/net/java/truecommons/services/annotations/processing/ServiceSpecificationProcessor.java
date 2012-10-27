/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services.annotations.processing;

import java.util.*;
import javax.annotation.processing.*;
import javax.lang.model.*;
import javax.lang.model.element.*;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import static javax.tools.Diagnostic.Kind.*;
import net.java.truecommons.services.annotations.*;

/**
 * Processes the {@link ServiceSpecification} annotation.
 *
 * @author Christian Schlichtherle
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public final class ServiceSpecificationProcessor extends AbstractProcessor {

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
        final Collection<ExecutableElement>
                ctors = new LinkedList<ExecutableElement>();
        for (final Element elem : spec.getEnclosedElements())
            if (elem.getKind() == CONSTRUCTOR)
                ctors.add((ExecutableElement) elem);
        return ctors.isEmpty() || valid(ctors)
                || error("No public or protected constructor with an empty parameter list available.", loc);
    }

    private boolean valid(final Collection<ExecutableElement> ctors) {
        for (final ExecutableElement ctor : ctors) if (valid(ctor)) return true;
        return false;
    }

    private boolean valid(final ExecutableElement ctor) {
        return (ctor.getModifiers().contains(PUBLIC)
                || ctor.getModifiers().contains(PROTECTED))
                && ctor.getParameters().isEmpty();
    }

    private boolean error(final String message, final Element loc) {
        processingEnv.getMessager().printMessage(ERROR, message , loc);
        return false;
    }

    private void warning(final String message, final Element loc) {
        processingEnv.getMessager().printMessage(WARNING, message , loc);
    }
}
