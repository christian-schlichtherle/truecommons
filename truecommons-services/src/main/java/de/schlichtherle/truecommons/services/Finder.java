/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.ServiceConfigurationError;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Finds resources on the class path.
 * <p>
 * If the class loader provided to the constructor is the current thread's
 * context class loader, then the methods of this class will resourcesFor resources
 * using only this class loader.
 * Otherwise, the given class loader is used first. Second, the current
 * thread's context class loader is used.
 * <p>
 * When enumerating resources, the results of both class loaders are
 * concatenated, so a resource may get enumerated twice!
 * If this is undesirable, then you should create a set from the enumeration
 * results.
 *
 * @author Christian Schlichtherle
 */
@ThreadSafe
public final class Finder {

    private final ClassLoader l1;

    /**
     * Constructs a new finder which uses the given class loader first.
     *
     * @param loader the nullable class loader.
     *        If this is {@code null}, then the system class loader is used.
     */
    public Finder(final @CheckForNull ClassLoader loader) {
        this.l1 = null != loader ? loader : ClassLoader.getSystemClassLoader();
    }

    /**
     * Enumerates resources according to the algorithm described in the class
     * Javadoc.
     *
     * @param  name The fully qualified name of the resources to locate.
     * @return A concatenated enumeration for the resource on the class path.
     * @throws ServiceConfigurationError if locating the resources fails for
     *         some reason.
     */
    public Enumeration<URL> resourcesFor(final String name)
    throws ServiceConfigurationError {
        ClassLoader l2 = Thread.currentThread().getContextClassLoader();
        try {
            return l1 == l2
                    ? l1.getResources(name)
                    : new JointEnumeration<URL>(l1.getResources(name),
                                                l2.getResources(name));
        } catch (final IOException ex) {
            throw new ServiceConfigurationError(ex.toString(), ex);
        }
    }
}
