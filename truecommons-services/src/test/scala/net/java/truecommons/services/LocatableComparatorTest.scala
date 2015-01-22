/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services

import org.junit.runner._
import org.scalatest.junit._
import org.scalacheck.Gen
import org.scalatest._
import org.scalatest.matchers._
import org.scalatest.prop._
import scala.util._
import LocatableComparatorTest._

@RunWith(classOf[JUnitRunner])
class LocatableComparatorTest
extends WordSpec with ShouldMatchers with PropertyChecks {

  "A service comparator" when {
    "comparing locatable objects" should {
      "produce ordered results" in {
        val n = 256
        val locatables = Seq.fill(n)(new Test).sorted(LocatableOrdering)
        forAll ((Gen.choose(0, n - 2), "i")) { i: Int =>
          whenever (0 <= i && i < n - 1) {
            locatables(i).getPriority should be <= (locatables(i + 1).getPriority)
          }
        }
      }
    }
  }
}

private object LocatableComparatorTest {
  private class Test extends LocatableService {
    override val getPriority = Random.nextInt
  }

  private object LocatableOrdering
  extends LocatableComparator with Ordering[LocatableService]
}
