/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio

import java.io.IOException
import java.nio._
import net.java.truecommons.io.Loan._
import net.java.truecommons.cio.attribute._
import org.junit.runner._
import org.scalacheck._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._
import org.scalatest.prop._
import scala.collection.JavaConverters._
import scala.util._

@RunWith(classOf[JUnitRunner])
class MemoryBufferSpec
extends WordSpec with ShouldMatchers with PropertyChecks {

  "A memory buffer" should {
    "work as designed" when {
      "doing round-trip I/O" in {
        forAll ((Gen.choose(0, 99), "limit"),
                (Gen.choose(0, 99), "capacity")) {
          (limit: Int, capacity: Int) =>
          whenever (0 <= limit && 0 <= capacity) { // TODO: why is this still required?
            val oa = new Array[Byte](limit)
            Random nextBytes oa
            val mb = new MemoryBuffer(capacity)
            mb.sizes.read.isEmpty should be (true);
            {
              val times = mb.times.read
              (times get Access.CREATE) should not be (null)
              (times get Access.READ) should be (null)
              (times get Access.WRITE) should be (null)
              (times get Access.DELETE) should be (null)
            }
            loan (mb.output stream null) to { out =>
              var off = 0
              do {
                val max = limit - off
                val len = if (1 < max) 1 + Random.nextInt(max) else max
                out write (oa, off, len)
                off += len
              } while (off < limit)
              out flush ()
              out write (oa, off, 0)
            }
            {
              val sizes = mb.sizes.read
              Size.values foreach { size => (sizes get size) should be (limit) }
            }
            {
              val times = mb.times.read
              (times get Access.CREATE) should not be (null)
              (times get Access.READ) should be (null)
              (times get Access.WRITE) should not be (null)
              (times get Access.DELETE) should be (null)
            }
            val ia = new Array[Byte](limit)
            loan (mb.input stream null) to { in =>
              in.markSupported should be (true)
              in mark limit
              in skip limit should be (limit)
              in reset ()
              var off = 0
              do {
                val max = limit - off
                val len = if (1 < max) 1 + Random.nextInt(max) else max
                val read = in read (ia, off, len)
                off += read
              } while (off < limit)
              in.available should be (0)
              in.read should be (-1)
              in skip limit should be (0)
            }
            {
              val times = mb.times.read
              (times get Access.CREATE) should not be (null)
              (times get Access.READ) should not be (null)
              (times get Access.WRITE) should not be (null)
              (times get Access.DELETE) should be (null)
            }
            ia should equal (oa)
            mb release ()
            mb.sizes.read.isEmpty should be (true);
            {
              val times = mb.times.read
              (times get Access.CREATE) should not be (null)
              (times get Access.READ) should not be (null)
              (times get Access.WRITE) should not be (null)
              (times get Access.DELETE) should not be (null)
            }
          }
        }
      }
    }
  }
}
