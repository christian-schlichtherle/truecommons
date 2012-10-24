/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import java.util.Comparator;

/**
 * Compares {@link Locatable}s.
 *
 * @author Christian Schlichtherle
 */
class LocatableComparator implements Comparator<Locatable> {
    @Override
    public int compare(Locatable o1, Locatable o2) {
        final int p1 = o1.getPriority();
        final int p2 = o2.getPriority();
        return p1 < p2 ? -1 : p1 == p2 ? 0 : 1;
    }
}
