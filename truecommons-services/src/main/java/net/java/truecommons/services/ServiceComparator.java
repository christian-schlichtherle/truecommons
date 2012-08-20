/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares {@link Service}s.
 * 
 * @author Christian Schlichtherle
 */
final class ServiceComparator implements Comparator<Service>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Service o1, Service o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
