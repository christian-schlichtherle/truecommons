/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Compares {@link Locatable}s.
 *
 * @author Christian Schlichtherle
 */
final class ServiceComparator implements Comparator<Locatable>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Locatable o1, Locatable o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
