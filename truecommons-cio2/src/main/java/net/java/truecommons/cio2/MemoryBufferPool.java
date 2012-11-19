/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 * A pool of I/O buffers which share their contents with
 * {@linkplain ByteBuffer byte buffer}s.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
@ThreadSafe
public final class MemoryBufferPool extends IoBufferPool {

    private final int initialCapacity;
    private final AtomicInteger total = new AtomicInteger();
    private final AtomicInteger active = new AtomicInteger();

    /**
     * Constructs a new memory buffer pool.
     *
     * @param initialCapacity the initial capacity of the byte buffer to use
     *        for writing to an allocated memory buffer.
     */
    public MemoryBufferPool(final int initialCapacity) {
        if (0 > initialCapacity)
            throw new IllegalArgumentException("Negative initial capacity: " + initialCapacity);
        this.initialCapacity = initialCapacity;
    }

    @Override
    public IoBuffer allocate() {
        final Buffer buffer = new Buffer(total.getAndIncrement());
        active.getAndIncrement();
        return buffer;
    }

    /**
     * Returns the number of memory buffers allocated but not yet released from
     * this pool.
     *
     * @return The number of memory buffers allocated but not yet released from
     *         this pool.
     */
    public int size() {
        return active.get();
    }

    @NotThreadSafe
    private final class Buffer extends MemoryBuffer {
        private boolean released;

        Buffer(int i) { super(MemoryBufferPool.this.initialCapacity); }

        @Override
        public void release() throws IOException {
            if (released)
                throw new IllegalStateException("I/O buffer has already been released!");
            active.getAndDecrement();
            super.release();
            released = true;
        }
    }
}
