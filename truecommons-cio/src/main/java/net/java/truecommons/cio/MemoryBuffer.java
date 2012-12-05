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
import java.nio.file.attribute.AclEntry;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import net.java.truecommons.cio.attribute.*;
import static net.java.truecommons.cio.attribute.Access.*;
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
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
@NotThreadSafe
public class MemoryBuffer implements IoBuffer {

    private int initialCapacity;
    private @CheckForNull ByteBuffer buffer;
    private final EnumMap<Access, Long> times = new EnumMap<>(Access.class);
    private final EnumMap<Access, Integer> counts = new EnumMap<>(Access.class);

    /**
     * Constructs a new memory buffer.
     * The reference to the {@linkplain #getBuffer() backing buffer} is set to
     * {@code null}.
     *
     * @param name the name of this memory buffer.
     * @param initialCapacity the initial capacity of the next backing buffer
     *        to allocate when starting output to this memory buffer.
     */
    public MemoryBuffer(int initialCapacity) { this(null, initialCapacity); }

    /**
     * Constructs a new memory buffer.
     * The {@linkplain #getInitialCapacity() initial capacity} is set to the
     * capacity of the given backing buffer.
     *
     * @param name the name of this memory buffer.
     * @param buffer the byte buffer with the contents to share with this
     *        memory buffer.
     */
    public MemoryBuffer(ByteBuffer buffer) { this(buffer, buffer.capacity()); }

    private MemoryBuffer(
            final @CheckForNull ByteBuffer buffer,
            final int initialCapacity) {
        setBuffer(buffer);
        setInitialCapacity(initialCapacity);
        times.put(CREATE, System.currentTimeMillis());
        for (final Access access : Access.values()) counts.put(access, 0);
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
    public final AttributeView<Size, Long> sizes() {
        class Sizes extends EnumKeyAttributeView<Size, Long> {
            @Override public Class<Size> clazz() { return Size.class; }

            @Override
            public Map<Size, Long> read(final Set<? extends Size> keys) {
                if (null == buffer) return Collections.emptyMap();
                final Long value = Long.valueOf(buffer.limit());
                final Map<Size, Long> map = new EnumMap<>(Size.class);
                for (final Size size : keys) map.put(size, value);
                return map;
            }
        } // Sizes

        return new Sizes();
    }

    @Override
    public final AttributeView<Access, Long> times() {
        class Times implements AttributeView<Access, Long> {
            @Override
            public Map<Access, Long> read() { return times.clone(); }

            @Override
            public Map<Access, Long> read(final Set<? extends Access> keys) {
                final Map<Access, Long> clone = times.clone();
                clone.keySet().retainAll(keys);
                return clone;
            }
        } // Times

        return new Times();
    }

    /**
     * @param  type the access type.
     * @return The number of times an input or output connection to the backing
     *         buffer has been opened.
     */
    public final AttributeView<Access, Integer> counts() {
        class Counts implements AttributeView<Access, Integer> {
            @Override
            public Map<Access, Integer> read() { return counts.clone(); }

            @Override
            public Map<Access, Integer> read(final Set<? extends Access> keys) {
                final Map<Access, Integer> clone = counts.clone();
                clone.keySet().retainAll(keys);
                return clone;
            }
        } // Counts

        return new Counts();
    }

    @Override
    public AttributeView<Entity, List<AclEntry>> permissions() {
        return NullAttributeView.get();
    }

    @Override
    public AttributeView<Entity, UserPrincipal> principals() {
        return NullAttributeView.get();
    }

    @Override
    public AttributeView<Object, Object> attributes() {
        return NullAttributeView.get();
    }

    @Override
    public final InputSocket<MemoryBuffer> input() { return new Input(); }

    @Override
    public final OutputSocket<MemoryBuffer> output() { return new Output(); }

    @Override
    public void release() throws IOException {
        times.put(DELETE, System.currentTimeMillis());
        buffer = null;
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
            counts.put(READ, counts.get(READ) + 1);
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
            counts.put(WRITE, counts.get(WRITE) + 1);
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
