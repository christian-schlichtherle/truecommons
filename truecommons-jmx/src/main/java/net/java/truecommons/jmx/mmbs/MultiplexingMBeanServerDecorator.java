/*
 * Copyright (C) 2012-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.mmbs;

import javax.annotation.concurrent.Immutable;
import javax.management.MBeanServer;
import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.jmx.sl.ObjectNameModifierLocator;
import net.java.truecommons.jmx.spi.MBeanServerDecorator;

/**
 * Decorates the given MBean server with a {@link MultiplexingMBeanServer} if
 * and only if this class is <em>not</em> defined by the system class loader.
 * 
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceImplementation
public final class MultiplexingMBeanServerDecorator
extends MBeanServerDecorator {

    @Override
    public MBeanServer apply(final MBeanServer mbs) {
        return getClass().getClassLoader() == ClassLoader.getSystemClassLoader()
                ? mbs
                : new MultiplexingMBeanServer(mbs, ObjectNameModifierLocator.SINGLETON.get());
    }

    /** @return -100 */
    @Override
    public int getPriority() { return -100; }
}
