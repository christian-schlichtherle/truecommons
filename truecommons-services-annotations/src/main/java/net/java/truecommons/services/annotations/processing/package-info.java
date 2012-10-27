/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
/**
 * Provides annotation processors for
 * {@link net.java.truecommons.services.ServiceSpecification}s and
 * {@link net.java.truecommons.services.ServiceImplementation}s.
 * These processors are registered in
 * {@code META-INF/services/javax.annotation.processing.Processor}, so you
 * normally don't need to set the annotation processor path when invoking
 * {@code javac}.
 *
 * @author Christian Schlichtherle
 */
@javax.annotation.Nonnull @javax.annotation.ParametersAreNonnullByDefault
package net.java.truecommons.services.annotations.processing;
