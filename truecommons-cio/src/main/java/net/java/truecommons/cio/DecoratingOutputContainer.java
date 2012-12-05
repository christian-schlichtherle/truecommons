/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import edu.umd.cs.findbugs.annotations.DischargesObligation;
import java.io.IOException;
import javax.annotation.WillCloseWhenClosed;

/**
 * An abstract decorator for an output container.
 *
 * @param  <E> the type of the entries in the decorated container.
 * @see    DecoratingInputContainer
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public abstract class DecoratingOutputContainer<E extends Entry>
extends DecoratingContainer<E, OutputContainer<E>>
implements OutputContainer<E> {

    protected DecoratingOutputContainer() { }

    protected DecoratingOutputContainer(
            @WillCloseWhenClosed OutputContainer<E> output) {
        super(output);
    }

    @Override
    public OutputSocket<E> output(E entry) { return container.output(entry); }

    @Override
    @DischargesObligation
    public void close() throws IOException { container.close(); }
}
