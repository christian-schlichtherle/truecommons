/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io

/**
  * @author Christian Schlichtherle
  */
import java.nio._
import java.nio.file._

class ByteBufferReadOnlyChannelIT extends ReadOnlyChannelITSuite {
  override def newChannel(path: Path) =
    new ReadOnlyChannel(new ByteBufferChannel(ByteBuffer.wrap(Files.readAllBytes(path))))
}
