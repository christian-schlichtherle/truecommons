/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.sl;

import javax.annotation.concurrent.Immutable;
import javax.management.MBeanServer;
import net.java.truecommons.jmx.spi.MBeanServerDecorator;
import net.java.truecommons.jmx.spi.MBeanServerProvider;
import net.java.truecommons.services.Container;
import net.java.truecommons.services.Locator;

/**
 * A container of the singleton MBean server.
 * The MBean server is created by using a {@link Locator} to search for
 * advertised implementations of the factory service specification class
 * {@link MBeanServerProvider}
 * and the decorator service specification class
 * {@link MBeanServerDecorator}.
 *
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@Immutable
public final class MBeanServerLocator implements Container<MBeanServer> {

    /** The singleton instance of this class. */
    public static final MBeanServerLocator SINGLETON = new MBeanServerLocator();

    private MBeanServerLocator() { }

    @Override
    public MBeanServer get() { return Lazy.mbs; }

    /** A static data utility class used for lazy initialization. */
    private static final class Lazy {
        static final MBeanServer mbs
                = new Locator(MBeanServerLocator.class)
                .container(MBeanServerProvider.class, MBeanServerDecorator.class)
                .get();
    }
}
