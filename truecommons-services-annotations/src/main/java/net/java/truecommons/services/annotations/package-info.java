/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
/**
 * Provides annotations for
 * {@linkplain net.java.truecommons.services.annotations.ServiceSpecification service specifications}
 * and
 * {@linkplain net.java.truecommons.services.annotations.ServiceImplementation service implementations}.
 * Using these annotations saves you from the tedious and error-prone process
 * of manually editing files in the resource directory
 * {@code META-INF/services} and enables design-time error checking in your IDE.
 *
 * <h3>The {@code @ServiceImplementation} Annotation</h3>
 * <p>
 * Suppose you wanted to implement a service specification class of the JSE API.
 * Using the {@code @ServiceImplementation} annotation, your implementation
 * could then look similar to this:
 * <pre>{@code
 * package com.company.project;
 *
 * import java.nio.charset.spi.CharsetProvider;
 * import net.java.truecommons.services.annotations.ServiceImplementation;
 *
 * \@ServiceImplementation(CharsetProvider.class)
 * public class Ibm437CharsetProvider extends CharsetProvider {
 *     ...
 * }
 * }</pre>
 * <p>
 * The
 * {@linkplain net.java.truecommons.services.annotations.processing.ServiceImplementationProcessor processor}
 * associated with the {@code @ServiceImplementation} annotation will then
 * generate the resource file
 * {@code META-INF/services/java.nio.charset.spi.CharsetProvider}
 * and place the class name {@code com.company.project.Ibm437CharsetProvider}
 * into it.
 * <p>
 * The annotation processor performs some static code analysis in order to
 * detect any obvious errors and emits appropriate error messages,
 * e.g. if the implementation class is non-public or abstract
 * or if there is no public constructor with an empty parameter list available.
 * <p>
 * If your IDE performs annotation processing, then any error messages should
 * get highlighted in the editor at design-time.
 * Furthermore, if your IDE supports refactoring, then changing the class name
 * of the implementation automatically updates the entry in the registry
 * resource file.
 *
 * <h3>The {@code @ServiceSpecification} Annotation</h3>
 * <p>
 * Suppose you wanted to design your own specification class or interface.
 * Using the {@code @ServiceSpecification} annotation, your specification
 * could then look similar to this:
 * <pre>{@code
 * package com.company.project.spec;
 *
 * import net.java.truecommons.services.annotations.ServiceSpecification;
 *
 * \@ServiceSpecification
 * public interface UltimateServiceSpecification {
 *     ...
 * }
 * }</pre>
 * <p>
 * The
 * {@linkplain net.java.truecommons.services.annotations.processing.ServiceSpecificationProcessor processor}
 * associated with the {@code @ServiceSpecification} annotation will then
 * perform some static code analysis to detect any obvious errors and emit
 * appropriate error messages, e.g. if the specification class or interface is
 * non-public or final or if there is no public or protected constructor with
 * an empty parameter list available.
 * <p>
 * An implementation of your specification could then look like this:
 * <pre>{@code
 * package com.company.project.impl;
 *
 * import com.company.project.spec.UltimateServiceSpecification;
 * import net.java.truecommons.services.annotations.ServiceSpecification;
 *
 * \@ServiceImplementation
 * public class UltimateServiceImplementation
 * implements UltimateServiceSpecification {
 *     ...
 * }
 * }</pre>
 * <p>
 * Note that the {@code @ServiceImplementation} annotation does not specify any
 * implemented classes or interfaces.
 * The annotation processor associated with the {@code @ServiceImplementation}
 * annotation will then scan the type hierarchy of the annotated class for any
 * superclass or interface which is annotated with the
 * {@code @ServiceSpecification} annotation and generate the registry resource
 * files according to its findings.
 * If no specification class or interface is found then an appropriate error
 * message gets emitted.
 *
 * @see    java.util.ServiceLoader
 * @author Christian Schlichtherle
 */
@javax.annotation.Nonnull @javax.annotation.ParametersAreNonnullByDefault
package net.java.truecommons.services.annotations;