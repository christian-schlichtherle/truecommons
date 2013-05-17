/*
 * Copyright (C) 2012-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx

import javax.management._
import net.java.truecommons.jmx.sl._
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

/**
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@RunWith(classOf[JUnitRunner])
class ObjectNameModifierSpec extends WordSpec with ShouldMatchers {

  def modifier = ObjectNameModifierLocator.SINGLETON.get

  "An object name modifier" should {
    "do a proper round trip conversion" in{
      val original = new ObjectName(":key=value")
      val modified = modifier apply original
      modified should not equal (original)
      val clone = modifier unapply modified
      clone should not be theSameInstanceAs (original)
      clone should equal (original)
    }
  }
}
