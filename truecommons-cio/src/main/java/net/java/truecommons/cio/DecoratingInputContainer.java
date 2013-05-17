/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import edu.umd.cs.findbugs.annotations.DischargesObligation;
import java.io.IOException;
import javax.annotation.WillCloseWhenClosed;

/**
 * An abstract decorator for an input container.
 *
 * @param  <E> the type of the entries in the decorated container.
 * @see    DecoratingOutputContainer
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public abstract class DecoratingInputContainer<E extends Entry>
extends DecoratingContainer<E, InputContainer<E>>
implements InputContainer<E> {

    protected DecoratingInputContainer() { }

    protected DecoratingInputContainer(
            @WillCloseWhenClosed InputContainer<E> input) {
        super(input);
    }

    @Override
    public InputSocket<E> input(EntryName name) {
        return container.input(name);
    }

    @Override
    @DischargesObligation
    public void close() throws IOException { container.close(); }
}
