/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.io

import java.io._
import de.schlichtherle.truecommons.io.Streams._
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._
import org.scalatest.mock._
import org.mockito._
import org.mockito.Matchers._
import org.mockito.Mockito._
import scala.util._

@RunWith(classOf[JUnitRunner])
class StreamsSpec extends WordSpec with ShouldMatchers with MockitoSugar {
  import StreamsSpec._

  def givenA = afterWord("given a")

  def any[A: ClassManifest] =
    Matchers.any(implicitly[ClassManifest[A]].erasure.asInstanceOf[Class[A]])

  def in = { 
    val b = new Array[Byte](bufferSize)
    Random.nextBytes(b)
    new ByteArrayInputStream(b) {
      def bytes = b
    }
  }

  def out = new ByteArrayOutputStream(bufferSize)

  "Streams.cat(InputStream, OutputStream)" should {
    "fail with a NullPointerException " when givenA {
      "null InputStream" in {
        intercept[NullPointerException] {
          cat(null, out)
        }
      }

      "null OutputStream)" in {
        intercept[NullPointerException] {
          cat(in, null)
        }
      }
    }

    "call only InputStream.read(byte[], int,  int)" in {
      val in = mock[InputStream]
      when(in read (any, any, any)) thenReturn -1
      cat(in, out)
      verify(in) read (any, any, any)
      verifyNoMoreInteractions(in)
    }

    "call OutputStream.write(byte[], int,  int) at least once and OutputStream.flush() exactly once and nothing else" in {
      val out = mock[OutputStream]
      cat(in, out)
      verify(out, atLeastOnce) write (any, any, any)
      verify(out) flush ()
      verifyNoMoreInteractions(out)
    }

    "fail with an InputException which wraps the original IOException from InputStream.read(byte[], int, int)" in {
      val in = mock[InputStream]
      val ex = new IOException
      doThrow(ex) when in read (any, any, any)
      intercept[InputException](cat(in, out)).getCause should be theSameInstanceAs ex
    }

    "fail with the original IOException from OutputStream.write(byte[], int, int)" in {
      val out = mock[OutputStream]
      val ex = new IOException
      doThrow(ex) when out write (any, any, any)
      intercept[IOException](cat(in, out)) should be theSameInstanceAs ex
    }

    "produce a copy of the data" when {
      "returning" in {
        val in = this.in
        val out = this.out
        for (_ <- 0 to 1) {
          cat(in, out)
          in.available should be (0)
          in.bytes should equal (out.toByteArray)
        }
      }
    }
  }

  "Streams.copy(InputStream, OutputStream)" should {
    "fail with a NullPointerException" when givenA {
      "null InputStream" in {
        intercept[NullPointerException] {
          copy(null.asInstanceOf[InputStream], new ByteArrayOutputStream)
        }
      }

      "null OutputStream" in {
        intercept[NullPointerException] {
          copy(new ByteArrayInputStream(Array[Byte]()), null.asInstanceOf[OutputStream])
        }
      }
    }

    "call InputStream.close()" when {
      "returning" in {
        val in = mock[InputStream]
        when(in read (any, any, any)) thenReturn -1
        copy(in, out)
        verify(in) close ()
      }

      "throwing an IOException from InputStream.read(byte[], int, int)" in {
        val in = mock[InputStream]
        doThrow(new IOException) when in read (any, any, any)
        intercept[IOException](copy(in, out))
        verify(in) close ()
      }

      "throwing an IOException from InputStream.close()" in {
        val in = mock[InputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when in close ()
        intercept[IOException](copy(in, out))
        verify(in) close ()
      }

      "throwing an IOException from OutputStream.write(byte[], int, int)" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn 1
        doThrow(new IOException) when out write (any, any, any)
        intercept[IOException](copy(in, out))
        verify(in) close ()
      }

      "throwing an IOException from OutputStream.close()" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when out close ()
        intercept[IOException](copy(in, out))
        verify(in) close ()
      }
    }

    "call OutputStream.close()" when {
      "returning" in {
        val out = mock[OutputStream]
        copy(in, out)
        verify(out) close ()
      }

      "throwing an IOException from InputStream.read(byte[], int, int)" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        doThrow(new IOException) when(in) read (any, any, any)
        intercept[IOException](copy(in, out))
        verify(out) close ()
      }

      "throwing an IOException from InputStream.close()" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when(in) close ()
        intercept[IOException](copy(in, out))
        verify(out) close ()
      }

      "throwing an IOException from OutputStream.write(byte[], int, int)" in {
        val out = mock[OutputStream]
        doThrow(new IOException) when(out) write (any, any, any)
        intercept[IOException](copy(in, out))
        verify(out) close ()
      }

      "throwing an IOException from OutputStream.close()" in {
        val out = mock[OutputStream]
        doThrow(new IOException) when(out) close ()
        intercept[IOException](copy(in, out))
        verify(out) close ()
      }
    }

    "fail with an InputException which wraps the original IOException from InputStream.close()" in {
      val in = mock[InputStream]
      val ex = new IOException
      when(in read (any, any, any)) thenReturn -1
      doThrow(ex) when in close ()
      intercept[InputException](copy(in, out)).getCause should be theSameInstanceAs ex
    }

    "fail with the original IOException from OutputStream.close()" in {
      val out = mock[OutputStream]
      val ex = new IOException
      doThrow(ex) when out close ()
      intercept[IOException](copy(in, out)) should be theSameInstanceAs ex
    }

    "produce a copy of the data" when {
      "returning" in {
        val in = this.in
        val out = this.out
        copy(in, out)
        in.available should be (0)
        in.bytes should equal (out.toByteArray)
      }
    }
  }

  "Streams.copy(Source, Sink)" should {
    "fail with a NullPointerException" when givenA {
      "null Source" in {
        intercept[NullPointerException] {
          copy(null.asInstanceOf[Source], new OneTimeSink(new ByteArrayOutputStream))
        }
      }

      "null Sink" in {
        intercept[NullPointerException] {
          copy(new OneTimeSource(new ByteArrayInputStream(Array[Byte]())), null.asInstanceOf[Sink])
        }
      }
    }

    "call InputStream.close()" when {
      "returning" in {
        val in = mock[InputStream]
        when(in read (any, any, any)) thenReturn -1
        copy(new OneTimeSource(in), new OneTimeSink(out))
        verify(in) close ()
      }

      "throwing an IOException from InputStream.read(byte[], int, int)" in {
        val in = mock[InputStream]
        doThrow(new IOException) when(in) read (any, any, any)
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(in) close ()
      }

      "throwing an IOException from InputStream.close()" in {
        val in = mock[InputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when(in) close ()
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(in) close ()
      }

      "throwing an IOException from OutputStream.write(byte[], int, int)" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn 1
        doThrow(new IOException) when(out) write (any, any, any)
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(in) close ()
      }

      "throwing an IOException from OutputStream.close()" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when(out) close ()
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(in) close ()
      }
    }

    "call OutputStream.close()" when {
      "returning" in {
        val out = mock[OutputStream]
        copy(new OneTimeSource(in), new OneTimeSink(out))
        verify(out) close ()
      }

      "throwing an IOException from InputStream.read(byte[], int, int)" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        doThrow(new IOException) when(in) read (any, any, any)
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(out) close ()
      }

      "throwing an IOException from InputStream.close()" in {
        val in = mock[InputStream]
        val out = mock[OutputStream]
        when(in read (any, any, any)) thenReturn -1
        doThrow(new IOException) when(in) close ()
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(out) close ()
      }

      "throwing an IOException from OutputStream.write(byte[], int, int)" in {
        val out = mock[OutputStream]
        doThrow(new IOException) when(out) write (any, any, any)
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(out) close ()
      }

      "throwing an IOException from OutputStream.close()" in {
        val out = mock[OutputStream]
        doThrow(new IOException) when(out) close ()
        intercept[IOException](copy(new OneTimeSource(in), new OneTimeSink(out)))
        verify(out) close ()
      }
    }

    "not call Sink.stream()" when {
      "throwing an IOException from Source.stream()" in {
        val source = mock[Source]
        val sink = mock[Sink]
        doThrow(new IOException) when source stream ()
        intercept[IOException](copy(source, sink))
        verify(sink, never) stream ()
      }
    }

    "call InputStream.close()" when {
      "throwing an IOException from Sink.stream()" in {
        val in = mock[InputStream]
        val sink = mock[Sink]
        doThrow(new IOException) when sink stream ()
        intercept[IOException](copy(new OneTimeSource(in), sink))
        verify(in) close ()
      }
    }

    "fail with an InputException which wraps the original IOException from Source.stream()" in {
      val source = mock[Source]
      val ex = new IOException
      doThrow(ex) when source stream ()
      intercept[InputException](copy(source, new OneTimeSink(out))).getCause should be theSameInstanceAs ex
    }

    "fail with the original IOException from Sink.stream()" in {
      val sink = mock[Sink]
      val ex = new IOException
      doThrow(ex) when sink stream ()
      intercept[IOException](copy(new OneTimeSource(in), sink)) should be theSameInstanceAs ex
    }

    "produce a copy of the data" when {
      "returning" in {
        val in = this.in
        val out = this.out
        copy(new OneTimeSource(in), new OneTimeSink(out))
        in.available should be (0)
        in.bytes should equal (out.toByteArray)
      }
    }
  }
}

object StreamsSpec {
  val bufferSize = 2 * Streams.FIFO_SIZE * Streams.BUFFER_SIZE
}
