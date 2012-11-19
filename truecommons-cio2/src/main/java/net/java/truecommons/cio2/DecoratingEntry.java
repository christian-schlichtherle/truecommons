/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio2;

import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import net.java.truecommons.shed.Option;
import net.java.truecommons.shed.UniqueObject;

/**
 * An abstract decorator for an entry.
 *
 * @param  <E> the type of the decorated entry.
 * @since  TrueCommons 2.4
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
    public Option<EntryName> name() { return entry.name(); }

    @Override
    public Option<Long> size(Size type) { return entry.size(type); }

    @Override
    public Option<Long> time(Access type) { return entry.time(type); }

    @Override
    public Option<Boolean> permits(Access type, Entity entity) {
        return entry.permits(type, entity);
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
