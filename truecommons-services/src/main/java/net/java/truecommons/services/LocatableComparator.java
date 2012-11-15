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
final class LocatableComparator implements Comparator<Locatable>, Serializable {

    private static final long serialVersionUID = 0L;

    @Override
    public int compare(final Locatable o1, final Locatable o2) {
        final int p1 = o1.getPriority();
        final int p2 = o2.getPriority();
        return p1 < p2 ? -1 : p1 == p2 ? 0 : 1;
    }
}
