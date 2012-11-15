/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.spi;

import net.java.truecommons.annotations.ServiceSpecification;
import net.java.truecommons.jmx.ObjectNameModifier;
import net.java.truecommons.jmx.sl.ObjectNameModifierLocator;
import net.java.truecommons.services.LocatableFactory;

/**
 * An abstract service for creating object name codecs.
 * Factory services are subject to service location by the
 * {@link ObjectNameModifierLocator#SINGLETON}.
 * <p>
 * If multiple factory services are locatable on the class path at run time,
 * the service with the greatest {@linkplain #getPriority() priority} gets
 * selected.
 *
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@ServiceSpecification
public abstract class ObjectNameModifierFactory
extends LocatableFactory<ObjectNameModifier> { }
