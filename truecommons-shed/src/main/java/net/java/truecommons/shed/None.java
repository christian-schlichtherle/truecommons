/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An option which holds a {@code null} element.
 *
 * @see Option
 * @author Christian Schlichtherle
 */
final class None extends Option<Object> {

    static final None SINGLETON = new None();

    private None() { }

    private Object readResolve() throws ObjectStreamException {
        return SINGLETON;
    }

    @Override
    public Iterator<Object> iterator() { return Collections.emptyIterator(); }

    @Override
    public int size() { return 0; }

    @Override
    public boolean isEmpty() { return true; }

    @Override
    public Object get() { throw new NoSuchElementException(); }

    @Override
    public @Nullable Object getOrElse(@Nullable Object alternative) {
        return alternative;
    }

    @Override
    public @Nullable Object orNull() { return null; }

    @Override
    public boolean equals(Object other) { return other instanceof None; }

    @Override
    public int hashCode() { return 42; }
}
