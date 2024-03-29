/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

import static java.lang.Math.min;

/**
 * Provides buffered random read-only access to its decorated seekable byte
 * channel.
 * Note that this channel maintains its own virtual file pointer.
 *
 * @author Christian Schlichtherle
 */
public class BufferedReadOnlyChannel extends ReadOnlyChannel {

    private static final long INVALID = Long.MIN_VALUE;

    /** The virtual file pointer. */
    private long pos;

    /**
     * The position in the decorated channel where the buffer starts.
     * This is always a multiple of the buffer size.
     */
    private long bufferStart = INVALID;

    /** The buffer for the channel data. */
    private final byte[] buffer;

    /**
     * Constructs a new buffered read-only channel.
     * Closing this channel will close the given channel.
     *
     * @param channel the channel to decorate.
     */
    public BufferedReadOnlyChannel(SeekableByteChannel channel) {
        this(channel, Streams.BUFFER_SIZE);
    }

    /**
     * Constructs a new buffered read-only channel.
     * Closing this channel will close the given seekable byte channel.
     *
     * @param channel the channel to decorate.
     * @param bufferSize the size of the byte buffer.
     */
    public BufferedReadOnlyChannel(
            final SeekableByteChannel channel,
            final int bufferSize) {
        super(channel);
        buffer = new byte[bufferSize];
    }

    @Override
    public int read(final ByteBuffer dst) throws IOException {
        // Check no-op first for compatibility with FileChannel.
        final int remaining = dst.remaining();
        if (remaining <= 0)
            return 0;

        // Check is open and not at EOF.
        final long size = size();
        if (position() >= size) // ensure pos is initialized, but do NOT cache!
            return -1;

        // Read of buffer data.
        int total = 0; // amount of data copied dst
        final int bufferSize = buffer.length;
        while (total < remaining && pos < size) {
            positionBuffer();
            final int bufferPos = (int) (pos - bufferStart);
            int bufferLimit = min(remaining - total, bufferSize - bufferPos);
            bufferLimit = (int) min(bufferLimit, size - pos);
            assert bufferLimit > 0;
            dst.put(buffer, bufferPos, bufferLimit);
            total += bufferLimit;
            pos += bufferLimit;
        }

        return total;
    }

    @Override
    public long position() throws IOException {
        checkOpen();
        return pos;
    }

    @Override
    public SeekableByteChannel position(final long pos) throws IOException {
        checkOpen();
        if (0 > pos)
            throw new IllegalArgumentException();
        this.pos = pos;
        return this;
    }

    /**
     * Notifies this channel of concurrent changes in its decorated channel.
     * Calling this method triggers a reload of the buffer on the next read
     * access.
     *
     * @return {@code this}
     */
    public BufferedReadOnlyChannel sync() {
        bufferStart = INVALID;
        return this;
    }

    /**
     * Positions the buffer so that it holds the data
     * referenced by the virtual file pointer.
     *
     * @throws IOException on any I/O error.
     *         The buffer gets invalidated in this case.
     */
    private void positionBuffer() throws IOException {
        final int bufferSize = buffer.length;

        // Check position.
        final long pos = this.pos;
        long bufferStart = this.bufferStart;
        final long nextBufferStart = bufferStart + bufferSize;
        if (bufferStart <= pos && pos < nextBufferStart)
            return;

        try {
            final SeekableByteChannel channel = this.channel;

            // Move position.
            // Round down to multiple of buffer size.
            this.bufferStart = bufferStart = pos / bufferSize * bufferSize;
            if (bufferStart != nextBufferStart)
                channel.position(bufferStart);

            // Fill buffer until end of file or buffer.
            // This should normally complete in one loop cycle, but we do not
            // depend on this as it would be a violation of ReadOnlyFile's
            // contract.
            int total = 0;
            final ByteBuffer buffer = ByteBuffer.wrap(this.buffer);
            do {
                int read = channel.read(buffer);
                if (read < 0)
                    break;
                total += read;
            } while (total < bufferSize);
        } catch (final Throwable ex) {
            this.bufferStart = INVALID;
            throw ex;
        }
    }
}
