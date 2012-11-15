/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.sl;

import net.java.truecommons.jmx.ObjectNameModifier;
import net.java.truecommons.jmx.spi.ObjectNameModifierDecorator;
import net.java.truecommons.jmx.spi.ObjectNameModifierFactory;
import net.java.truecommons.services.Container;
import net.java.truecommons.services.Locator;

/**
 * A container of the singleton object name codec.
 * The codec is created by using a {@link Locator} to search for advertised
 * implementations of the factory service specification class
 * {@link ObjectNameModifierFactory}
 * and the decorator service specification class
 * {@link ObjectNameModifierDecorator}.
 *
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
public final class ObjectNameModifierLocator
implements Container<ObjectNameModifier> {

    /** The singleton instance of this class. */
    public static final ObjectNameModifierLocator
            SINGLETON = new ObjectNameModifierLocator();

    private ObjectNameModifierLocator() { }

    @Override
    public ObjectNameModifier get() { return Lazy.codec; }

    /** A static data utility class used for lazy initialization. */
    private static final class Lazy {
        static final ObjectNameModifier codec =
                new Locator(ObjectNameModifierLocator.class)
                .factory(ObjectNameModifierFactory.class, ObjectNameModifierDecorator.class)
                .get();
    }
}
