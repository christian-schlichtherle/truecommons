/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;

/**
 * An option which holds a non-{@code null} element.
 *
 * @see Option
 * @author Christian Schlichtherle
 */
final class Some<T> extends Option<T> {

    private final T element;

    Some(final T element) {
        this.element = Objects.requireNonNull(element);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.singleton(element).iterator();
    }

    @Override
    public int size() { return 1; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public T get() { return element; }

    @Override
    public T getOrElse(@Nullable T alternative) { return element; }

    @Override
    public T orNull() { return element; }

    @Override
    public boolean equals(Object that) {
        return this == that
                || that instanceof Some
                && this.element.equals(((Some<?>) that).element);
    }

    @Override
    public int hashCode() { return element.hashCode(); }
}
