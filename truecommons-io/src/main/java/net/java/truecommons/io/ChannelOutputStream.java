/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import edu.umd.cs.findbugs.annotations.CleanupObligation;
import edu.umd.cs.findbugs.annotations.DischargesObligation;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.WillCloseWhenClosed;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Adapts a {@link WritableByteChannel} to an output stream.
 *
 * @see    ChannelInputStream
 * @author Christian Schlichtherle
 */
@NotThreadSafe
@CleanupObligation
public class ChannelOutputStream extends OutputStream {

    private final ByteBuffer single = ByteBuffer.allocate(1);

    /** The adapted nullable writable byte channel. */
    protected @Nullable WritableByteChannel channel;

    protected ChannelOutputStream() { }

    public ChannelOutputStream(
            final @CheckForNull @WillCloseWhenClosed WritableByteChannel channel) {
        this.channel = Objects.requireNonNull(channel);
    }

    @Override
    public void write(int b) throws IOException {
        single.put(0, (byte) b).clear();
        if (1 != channel.write(single)) throw new IOException("write error");
    }

    @Override
    public final void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (len != channel.write(ByteBuffer.wrap(b, off, len)))
            throw new IOException("write error");
    }

    @Override
    public void flush() throws IOException { }

    @Override
    @DischargesObligation
    public void close() throws IOException { channel.close(); }
}
