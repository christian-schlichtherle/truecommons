/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import edu.umd.cs.findbugs.annotations.CreatesObligation;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.EnumMap;
import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import static net.java.truecommons.cio.Entry.Access.READ;
import static net.java.truecommons.cio.Entry.Access.WRITE;
import net.java.truecommons.cio.Entry.Entity;
import net.java.truecommons.cio.Entry.Size;
import static net.java.truecommons.cio.Entry.UNKNOWN;
import net.java.truecommons.io.ByteBufferChannel;
import net.java.truecommons.io.DisconnectingSeekableChannel;

/**
 * An I/O buffer which shares its contents with a
 * {@linkplain ByteBuffer byte buffer}.
 * <p>
 * If the reference to the {@linkplain #getBuffer backing buffer} is
 * {@code null}, then any attempt to start input from this I/O buffer results
 * in a {@link FileNotFoundException}.
 * The reference gets automatically set upon each call to {@code close()}
 * on any {@link OutputStream} or {@link SeekableByteChannel} which has been
 * obtained from an {@link #output() output socket} for this I/O buffer.
 * The reference can also get explicitly set by calling the constructor
 * {@link #MemoryBuffer(String, ByteBuffer)} or the method
 * {@link #setBuffer(ByteBuffer)}.
 *
 * @author Christian Schlichtherle
 */
@NotThreadSafe
public class MemoryBuffer implements IoBuffer {

    private final String name;
    private int initialCapacity;
    private @CheckForNull ByteBuffer buffer;
    private final EnumMap<Access, Long> times = new EnumMap<>(Access.class);
    private int reads;
    private int writes;

    /**
     * Constructs a new memory buffer.
     * The reference to the {@linkplain #getBuffer() backing buffer} is set to
     * {@code null}.
     *
     * @param name the name of this memory buffer.
     * @param initialCapacity the initial capacity of the next backing buffer
     *        to allocate when starting output to this memory buffer.
     */
    public MemoryBuffer(String name, int initialCapacity) {
        this(name, null, initialCapacity);
    }

    /**
     * Constructs a new memory buffer.
     * The {@linkplain #getInitialCapacity() initial capacity} is set to the
     * capacity of the given backing buffer.
     *
     * @param name the name of this memory buffer.
     * @param buffer the byte buffer with the contents to share with this
     *        memory buffer.
     */
    public MemoryBuffer(String name, ByteBuffer buffer) {
        this(name, buffer, buffer.capacity());
    }

    private MemoryBuffer(
            final String name,
            final @CheckForNull ByteBuffer buffer,
            final int initialCapacity) {
        this.name = Objects.requireNonNull(name);
        setBuffer(buffer);
        setInitialCapacity(initialCapacity);
    }

    /**
     * Returns the initial capacity of the next backing buffer to allocate when
     * starting output to this memory buffer.
     *
     * @return The initial capacity of the next backing buffer to allocate when
     *         starting output to this memory buffer.
     */
    public final int getInitialCapacity() { return this.initialCapacity; }

    /**
     * Sets the initial capacity of the next backing buffer to allocate when
     * starting output to this memory buffer.
     *
     * @param initialCapacity the initial capacity of the next backing buffer
     *        to allocate when starting output to this memory buffer.
     */
    public final void setInitialCapacity(final int initialCapacity) {
        if (0 > initialCapacity)
            throw new IllegalArgumentException("Negative initial capacity: " + initialCapacity);
        this.initialCapacity = initialCapacity;
    }

    /**
     * Returns a {@linkplain ByteBuffer#duplicate() duplicate} of the nullable
     * backing buffer with the contents to share with this memory buffer.
     * If the backing buffer is not set, then {@code null} is returned.
     * Otherwise, the returned buffer's position is set to zero and its limit
     * is set to the size of the contents of this memory buffer.
     *
     * @return A {@linkplain ByteBuffer#duplicate() duplicate} of the nullable
     *         backing buffer with the contents to share with this memory
     *         buffer.
     */
    public final @Nullable ByteBuffer getBuffer() {
        return null != buffer ? buffer.duplicate() : null;
    }

    /**
     * Sets the nullable backing buffer with the contents to share with this
     * memory buffer.
     * If {@code buffer} is not {@code null}, a
     * {@linkplain ByteBuffer#duplicate() duplicate} is made and
     * {@linkplain ByteBuffer#rewind() rewind} in order to protect this
     * memory buffer from concurrent modifications of the given buffer's
     * properties.
     *
     * @param buffer the nullable byte buffer with the contents to share
     *        with this memory buffer.
     */
    public final void setBuffer(final @CheckForNull ByteBuffer buffer) {
        this.buffer = null != buffer
                ? (ByteBuffer) buffer.duplicate().rewind()
                : null;
    }

    @Override
    public final String getName() { return name; }

    @Override
    public final long getSize(Size type) {
        return null != buffer ? buffer.limit() : UNKNOWN;
    }

    /**
     * @param  type the access type.
     * @return The number of times an input or output connection to the backing
     *         buffer has been opened.
     */
    // http://java.net/jira/browse/TRUEZIP-83
    public final int getCount(Access type) {
        return type == WRITE ? writes : reads;
    }

    /**
     * @return The last time an input or output connection to the backing
     *         buffer has been {@code close()}d.
     */
    @Override
    public final long getTime(Access type) {
        final Long time = times.get(type);
        return null != time ? time : UNKNOWN;
    }

    @Override
    public Boolean isPermitted(Access type, Entity entity) { return true; }

    @Override
    public final InputSocket<MemoryBuffer> input() { return new Input(); }

    @Override
    public final OutputSocket<MemoryBuffer> output() { return new Output(); }

    @Override
    public void release() throws IOException { buffer = null; }

    /**
     * Returns a string representation of this object for debugging and logging
     * purposes.
     */
    @Override
    public String toString() {
        return String.format("%s[name=%s]",
                getClass().getName(),
                getName());
    }

    private final class Input extends AbstractInputSocket<MemoryBuffer> {
        @Override
        public MemoryBuffer target() throws IOException {
            return MemoryBuffer.this;
        }

        @Override
        public SeekableByteChannel channel(OutputSocket<? extends Entry> peer)
        throws IOException {
            return new DataInputChannel();
        }
    } // Input

    private final class Output extends AbstractOutputSocket<MemoryBuffer> {
        @Override
        public MemoryBuffer target() throws IOException {
            return MemoryBuffer.this;
        }

        @Override
        public SeekableByteChannel channel(InputSocket<? extends Entry> peer)
        throws IOException {
            return new DataOutputChannel();
        }
    } // Output

    private final class DataInputChannel extends DisconnectingSeekableChannel {
        boolean closed;

        @CreatesObligation
        DataInputChannel() throws FileNotFoundException {
            final ByteBuffer buffer = MemoryBuffer.this.buffer;
            if (null == buffer) throw new FileNotFoundException();
            channel = new ByteBufferChannel(buffer.asReadOnlyBuffer());
            reads++;
        }

        @Override
        public boolean isOpen() { return !closed; }

        @Override
        public void close() throws IOException {
            if (closed) return;
            channel.close();
            times.put(READ, System.currentTimeMillis());
            closed = true;
        }
    } // DataInputChannel

    private final class DataOutputChannel extends DisconnectingSeekableChannel {
        boolean closed;

        @CreatesObligation
        DataOutputChannel() {
            final ByteBuffer buffer = (ByteBuffer) ByteBuffer
                    .allocateDirect(initialCapacity)
                    .limit(0);
            channel = new ByteBufferChannel(buffer);
            writes++;
        }

        @Override
        public boolean isOpen() { return !closed; }

        @Override
        public void close() throws IOException {
            if (closed) return;
            channel.close();
            times.put(WRITE, System.currentTimeMillis());
            buffer = (ByteBuffer) ((ByteBufferChannel) channel)
                    .getBuffer()
                    .rewind();
            closed = true;
        }
    } // DataOutputChannel
}
