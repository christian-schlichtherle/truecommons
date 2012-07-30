/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Christian Schlichtherle
 */
public class BufferedReadOnlyChannelIT extends ReadOnlyChannelITSuite {

    @Override
    protected SeekableByteChannel newChannel(Path path) throws IOException {
        return new BufferedReadOnlyChannel(Files.newByteChannel(path));
    }
}