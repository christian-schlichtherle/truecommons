/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services.processor;

import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import static javax.lang.model.element.Modifier.*;
import javax.lang.model.element.TypeElement;
import static javax.tools.Diagnostic.Kind.*;
import net.java.truecommons.services.ServiceSpecification;

/**
 * Processes the {@link ServiceSpecification} annotation.
 *
 * @author Christian Schlichtherle
 */
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class ServiceSpecificationProcessor extends AbstractProcessor {

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
        final Set<Modifier> modifiers = spec.getModifiers();
        return modifiers.contains(PUBLIC) && !modifiers.contains(FINAL)
            || error("Not a public and non-final class or interface.", loc);
    }

    private boolean error(
            final String message,
            final Element loc) {
        processingEnv.getMessager().printMessage(ERROR, message , loc);
        return false;
    }
}
