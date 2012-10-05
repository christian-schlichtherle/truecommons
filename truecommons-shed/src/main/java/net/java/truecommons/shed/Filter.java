/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

/**
 * A generic filter interface.
 * 
 * @since  TrueCommons 1.0.11
 * @author Christian Schlichtherle
 */
public interface Filter<T> {

    /**
     * Returns {@code true} if and only if this filter accepts the given
     * {@code element}.
     * 
     * @param  element the element to test.
     * @return Whether or not this filter accepts the given {@code element}.
     */
    boolean accept(T element);

    /**
     * A filter which accepts any object.
     */
    Filter<Object> ACCEPT_ANY = new Filter() {
        @Override
        public boolean accept(Object element) { return true; }
    };

    /**
     * A filter which accepts no objects.
     */
    Filter<Object> ACCEPT_NONE = new Filter() {
        @Override
        public boolean accept(Object element) { return false; }
    };
}
