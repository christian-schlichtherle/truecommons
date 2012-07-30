/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import edu.umd.cs.findbugs.annotations.DischargesObligation;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.nio.channels.NonWritableChannelException;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Adapts a {@linkplain ByteBuffer byte buffer} to a seekable byte channel.
 * 
 * @author Christian Schlichtherle
 */
@NotThreadSafe
public final class ByteBufferChannel extends AbstractSeekableChannel {

    private ByteBuffer buffer;
    private long position;
    private boolean closed;

    /**
     * Constructs a new seekable byte buffer channel with a
     * {@linkplain ByteBuffer#duplicate() duplicate} of the given byte buffer
     * as its initial {@linkplain #bufferDuplicate() byte buffer}.
     * Note that the buffer contents are shared between the client application
     * and this class.
     * 
     * @param  buffer the initial byte buffer to read or write.
     * @throws IllegalArgumentException if {@code buffer} is a writable direct
     *         buffer.
     */
    public ByteBufferChannel(final ByteBuffer buffer) {
        if (!buffer.isReadOnly() && !buffer.hasArray())
            throw new IllegalArgumentException();
        this.buffer = buffer.duplicate();
    }

    /**
     * Returns a {@linkplain ByteBuffer#duplicate() duplicate} of the backing
     * byte buffer.
     * Note that the buffer contents are shared between the client application
     * and this class.
     * 
     * @return A {@linkplain ByteBuffer#duplicate() duplicate} of the backing
     *         byte buffer.
     */
    public ByteBuffer bufferDuplicate() {
        return buffer.duplicate();
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        checkOpen();
        int remaining = dst.remaining();
        if (remaining <= 0) return 0;
        final long position = this.position;
        if (position >= buffer.limit()) return -1;
        buffer.position((int) position);
        final int available = buffer.remaining();
        final int srcLimit;
        if (available > remaining) {
            srcLimit = buffer.limit();
            buffer.limit(buffer.position() + remaining);
        } else {
            srcLimit = -1;
            remaining = available;
        }
        try {
            dst.put(buffer);
        } finally {
            if (srcLimit >= 0) buffer.limit(srcLimit);
        }
        this.position += remaining;
        return remaining;
    }

    @Override
    public int write(final ByteBuffer src) throws IOException {
        checkOpen();
        final int remaining = src.remaining();
        final long minLimit = position + remaining;
        final int limit = buffer.limit();
        if (minLimit > limit) {
            final long oldCapacity = buffer.capacity();
            if (minLimit <= oldCapacity) {
                buffer.limit((int) minLimit);
            } else if (minLimit > Integer.MAX_VALUE) {
                throw new OutOfMemoryError();
            } else {
                if (buffer.isReadOnly()) throw new NonWritableChannelException();
                long newCapacity = 0 < oldCapacity ? oldCapacity : 1;
                while ((newCapacity <<= 1) < minLimit) {
                }
                if (newCapacity > Integer.MAX_VALUE) newCapacity = minLimit;
                final byte[] array = new byte[(int) newCapacity];
                System.arraycopy(buffer.array(), buffer.arrayOffset(), array, 0, limit);
                buffer = ByteBuffer.wrap(array);
            }
            final long position = this.position;
            assert position <= Integer.MAX_VALUE;
            buffer.position((int) position);
        }
        try {
            buffer.put(src);
        } catch (final ReadOnlyBufferException ex) {
            throw new NonWritableChannelException();
        }
        position += remaining;
        return remaining;
    }

    @Override
    public long position() throws IOException {
        checkOpen();
        return position;
    }

    @Override
    public ByteBufferChannel position(long position) throws IOException {
        checkOpen();
        if (0 > position) throw new IllegalArgumentException();
        this.position = position;
        return this;
    }

    @Override
    public long size() throws IOException {
        checkOpen();
        return buffer.limit();
    }

    @Override
    public ByteBufferChannel truncate(final long size) throws IOException {
        checkOpen();
        if (buffer.isReadOnly()) throw new NonWritableChannelException();
        if (buffer.limit() > size) buffer.limit((int) size);
        if (position > size) position = size;
        return this;
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    @DischargesObligation
    public void close() {
        closed = true;
    }
}
