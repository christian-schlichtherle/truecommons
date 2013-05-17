/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import java.io.Closeable;

/**
 * A container which provides output sockets for writing entries.
 *
 * @param  <E> the type of the entries in this container.
 * @see    InputContainer
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface OutputContainer<E extends Entry>
extends Closeable, Container<E> {

    /**
     * Returns an output socket for writing the given entry.
     * <p>
     * Upon return from this method, this interface must reflect the given
     * entry, even if it hasn't been written yet.
     *
     * @param  entry the entry, which is the
     *         {@linkplain OutputSocket#target local target} of the
     *         returned output socket.
     * @return An output socket for writing the given entry.
     */
    OutputSocket<E> output(E entry);
}
