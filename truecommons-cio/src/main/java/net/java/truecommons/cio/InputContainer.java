/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import java.io.Closeable;

/**
 * A container which provides input sockets for reading entries.
 *
 * @param  <E> the type of the entries in this container.
 * @see    OutputContainer
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface InputContainer<E extends Entry>
extends Closeable, Container<E> {

    /**
     * Returns an input socket for reading the named entry.
     *
     * @param  name the entry name.
     * @return An input socket for reading the named entry.
     */
    InputSocket<E> input(EntryName name);
}
