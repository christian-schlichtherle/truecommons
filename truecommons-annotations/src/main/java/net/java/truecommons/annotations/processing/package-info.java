/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
/**
 * Provides annotation processors for
 * {@linkplain net.java.truecommons.annotations.processing.ServiceSpecificationProcessor service specifications}
 * and
 * {@linkplain net.java.truecommons.annotations.processing.ServiceImplementationProcessor service implementations}.
 * These processors are registered in
 * {@code META-INF/services/javax.annotation.processing.Processor}, so you
 * normally don't need to set the annotation processor path when invoking
 * {@code javac}.
 *
 * @since  TrueCommons 2.1
 * @author Christian Schlichtherle
 */
@javax.annotation.Nonnull @javax.annotation.ParametersAreNonnullByDefault
package net.java.truecommons.annotations.processing;
