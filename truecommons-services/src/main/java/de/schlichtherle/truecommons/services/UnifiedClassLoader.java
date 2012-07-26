/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Christian Schlichtherle
 */
final class UnifiedClassLoader extends ClassLoader {

    static ClassLoader resolve(
            final ClassLoader primary,
            final ClassLoader secondary) {
        assert null != primary;
        assert null != secondary;
        if (primary == secondary || isChildOf(primary, secondary))
            return primary;
        if (isChildOf(secondary, primary))
            return secondary;
        return new UnifiedClassLoader(primary, secondary);
    }

    private static boolean isChildOf(ClassLoader c, final ClassLoader r) {
        for (ClassLoader p; null != (p = c.getParent()); c = p)
            if (p == r) return true;
        return false;
    }

    private final ClassLoader secondary;

    private UnifiedClassLoader(
            final ClassLoader primary,
            final ClassLoader secondary) {
        super(primary);
        this.secondary = secondary;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return secondary.loadClass(name);
    }

    @Override
    protected URL findResource(String name) {
        return secondary.getResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        return secondary.getResources(name);
    }
}
