/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.cio;

import java.io.IOException;
import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract decorator for an I/O buffer.
 *
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class DecoratingIoBuffer
extends DecoratingEntry<IoBuffer> implements IoBuffer {

    protected DecoratingIoBuffer() { }

    protected DecoratingIoBuffer(IoBuffer entry) {
        super(entry);
    }

    @Override
    public InputSocket<? extends IoBuffer> input() {
        return entry.input();
    }

    @Override
    public OutputSocket<? extends IoBuffer> output() {
        return entry.output();
    }

    @Override
    public void release() throws IOException {
        entry.release();
    }
}
