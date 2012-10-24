/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio

import java.nio._
import net.java.truecommons.io.Loan._
import org.junit.runner._
import org.scalacheck._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._
import org.scalatest.prop._
import scala.util._
import Entry._

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
            val mb = new MemoryBuffer("name", capacity)
            mb getSize Size.DATA should be (UNKNOWN)
            mb getSize Size.STORAGE should be (UNKNOWN)
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
            mb getSize Size.DATA should be (limit)
            mb getSize Size.STORAGE should be (limit)
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
            ia should equal (oa)
            mb release ()
            mb getSize Size.DATA should be (UNKNOWN)
            mb getSize Size.STORAGE should be (UNKNOWN)
          }
        }
      }
    }
  }
}
