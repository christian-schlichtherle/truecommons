/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import net.java.truecommons.cio.attribute.*;
import net.java.truecommons.shed.UniqueObject;

/**
 * An abstract decorator for an entry.
 *
 * @param  <E> the type of the decorated entry.
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class DecoratingEntry<E extends Entry>
extends UniqueObject implements Entry {

    /** The nullable decorated entry. */
    @SuppressWarnings("ProtectedField")
    protected @Nullable E entry;

    protected DecoratingEntry() { }

    protected DecoratingEntry(final E entry) {
        this.entry = Objects.requireNonNull(entry);
    }

    @Override
    public AttributeView<Size, Long> sizes() { return entry.sizes(); }

    @Override
    public AttributeView<Access, Long> times() { return entry.times(); }

    @Override
    public AttributeView<Entity, List<AclEntry>> permissions() {
        return entry.permissions();
    }

    @Override
    public AttributeView<Entity, UserPrincipal> principals() {
        return entry.principals();
    }

    @Override
    public AttributeView<Object, Object> attributes() {
        return entry.attributes();
    }

    /**
     * Returns a string representation of this object for debugging and logging
     * purposes.
     */
    @Override
    public String toString() {
        return String.format("%s@%x[entry=%s]",
                getClass().getName(), hashCode(), entry);
    }
}
