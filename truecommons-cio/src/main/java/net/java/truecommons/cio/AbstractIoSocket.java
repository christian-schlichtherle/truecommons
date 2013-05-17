/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import java.io.IOException;
import javax.annotation.concurrent.Immutable;
import net.java.truecommons.shed.UniqueObject;

/**
 * Abstract base class for I/O sockets.
 * <p>
 * Subclasses should be immutable.
 *
 * @param  <E> the type of the {@linkplain #target() target entry} for I/O
 *         operations.
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
@Immutable
public abstract class AbstractIoSocket<E extends Entry>
extends UniqueObject implements IoSocket<E> {

    /**
     * Returns a string representation of this object for debugging and logging
     * purposes.
     */
    @Override
    public String toString() {
        Object target;
        try {
            target = target();
        } catch (final IOException ex) {
            target = ex;
        }
        return String.format("%s@%x[target=%s]",
                getClass().getName(), hashCode(), target);
    }
}
