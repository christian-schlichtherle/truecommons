/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.cio;

import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import net.java.truecommons3.shed.UniqueObject;

/**
 * An abstract decorator for an entry.
 *
 * @param  <E> the type of the decorated entry.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class DecoratingEntry<E extends Entry>
extends UniqueObject implements Entry {

    /** The nullable decorated entry. */
    protected @Nullable E entry;

    protected DecoratingEntry() { }

    protected DecoratingEntry(final E entry) {
        this.entry = Objects.requireNonNull(entry);
    }

    @Override
    public String getName() {
        return entry.getName();
    }

    @Override
    public long getSize(Size type) {
        return entry.getSize(type);
    }

    @Override
    public long getTime(Access type) {
        return entry.getTime(type);
    }

    @Override
    public Boolean isPermitted(Access type, Entity entity) {
        return entry.isPermitted(type, entity);
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
