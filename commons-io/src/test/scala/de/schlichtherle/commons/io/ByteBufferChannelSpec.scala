/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.commons.io

import java.io._
import java.nio._
import java.nio.channels._
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._
import org.scalatest.mock._
import scala.util._

@RunWith(classOf[JUnitRunner])
class ByteBufferChannelSpec
extends WordSpec with ShouldMatchers with BeforeAndAfter with MockitoSugar {
  import ByteBufferChannelSpec._

  var array: Array[Byte] = _

  before {
    array = new Array[Byte](bufferSize)
    Random nextBytes array
  }

  def assertInvariants(channel: ByteBufferChannel) = {
    channel close()
    channel.isOpen should be (true)
    channel
  }

  def newReadOnlyByteBufferChannel =
    assertInvariants(new ByteBufferChannel(ByteBuffer wrap array asReadOnlyBuffer))

  def newEmptyByteBufferChannel =
    assertInvariants(new ByteBufferChannel(ByteBuffer allocate 0))

  "A ByteBufferChannel" when {
    "constructing" should {
      "accept no null ByteBuffer" in {
        intercept[NullPointerException] {
          new ByteBufferChannel(null)
        }
      }

      "accept a direct ByteBuffer only if its read-only" in {
        intercept[IllegalArgumentException] {
          new ByteBufferChannel(ByteBuffer allocateDirect 1)
        }
        new ByteBufferChannel(ByteBuffer allocateDirect 1 asReadOnlyBuffer)
      }
    }

    "given a read-only ByteBuffer" should {
      "have the size() of the given buffer" in {
        newReadOnlyByteBufferChannel should have size array.length
      }

      "have position() zero" in {
        newReadOnlyByteBufferChannel.position() should be (0)
      }

      "support reading the ByteBuffer and repositioning repeatedly" in {
        val channel = newReadOnlyByteBufferChannel
        for (i <- 1 to 2) {
          val data = ByteBuffer allocate array.length
          channel read data should be (array.length)
          data.array should equal (array)
          channel.position should be (array.length)
          channel read ByteBuffer.allocate(bufferSize) should be (-1)
          channel.position should be (array.length)
          channel position 0
        }
      }

      "throw a NonWritableChannelException on write(ByteBuffer)" in {
        intercept[NonWritableChannelException] {
          newReadOnlyByteBufferChannel write ByteBuffer.allocate(bufferSize)
        }
      }

      "throw a NonWritableChannelException on truncate(long)" in {
        intercept[NonWritableChannelException] {
          newReadOnlyByteBufferChannel truncate 0
        }
      }
    }

    "given an empty ByteBuffer" should {
      "have size() zero" in {
        newEmptyByteBufferChannel should have size 0
      }

      "have position() zero" in {
        newEmptyByteBufferChannel.position() should be (0)
      }

      "report EOF on read(ByteBuffer)" in {
        newEmptyByteBufferChannel read ByteBuffer.allocate(bufferSize) should be (-1)
      }

      "support writing, rewinding, rereading and truncating the ByteBuffer repeatedly" in {
        val channel = newEmptyByteBufferChannel
        for (i <- 1 to 2) {
          // Write.
          channel write ByteBuffer.wrap(array) should be (array.length)
          channel.position should be (array.length)
          channel should have size array.length

          // Rewind.
          channel position 0
          channel should have size array.length

          // Reread.
          val copy = ByteBuffer allocate array.length
          channel read copy should be (array.length)
          copy.array should equal (array)
          channel.position should be (array.length)
          channel should have size array.length

          // Check EOF.
          channel read ByteBuffer.allocate(bufferSize) should be (-1)
          channel.position should be (array.length)
          channel should have size array.length

          // Truncate.
          channel truncate 0
          channel.position should be (0)
          channel should have size 0
        }
      }
    }
  }
}

object ByteBufferChannelSpec {
  val bufferSize = 128
}
