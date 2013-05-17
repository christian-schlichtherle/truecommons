/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed

import org.junit._
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._
import Filter._

@RunWith(classOf[JUnitRunner])
class FilterTest extends WordSpec with ShouldMatchers {

  "Filter.ACCEPT_ANY" should {
    "accept any parameter and always return true" in {
      ACCEPT_ANY.accept(null) should be (true)
    }
  }

  "Filter.ACCEPT_NONE" should {
    "accept any parameter and always return false" in {
      ACCEPT_NONE.accept(null) should be (false)
    }
  }
}
