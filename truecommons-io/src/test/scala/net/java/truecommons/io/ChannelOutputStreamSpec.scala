package net.java.truecommons.io

import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

@RunWith(classOf[JUnitRunner])
/** @author Christian Schlichtherle */
class ChannelOutputStreamSpec
extends WordSpec
   with ShouldMatchers
   with ParallelTestExecution {

  "A channel output stream" when {
    "constructed with a null channel parameteter" should {
      "report a null pointer exception" in {
        intercept[NullPointerException] { new ChannelOutputStream(null) }
      }
    }
  }
}
