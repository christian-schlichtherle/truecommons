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
            validType(spec);
        }
        return false; // critical!
    }

    private boolean validType(final TypeElement spec) {
        final Set<Modifier> modifiers = spec.getModifiers();
        if (!modifiers.contains(PUBLIC) || modifiers.contains(FINAL))
            return invalidType("needs to be a public and non-final type", spec);
        return true;
    }

    private boolean invalidType(final String message, final TypeElement impl) {
        processingEnv.getMessager().printMessage(ERROR,
                "A specification " + message + ".", impl);
        return false;
    }
}
