/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.shed

import java.io._

import net.java.truecommons3.shed.ResourceLoan._
import org.junit.runner.RunWith
import org.mockito.Mockito._
import org.scalatest.Matchers._
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar.mock

/** @author Christian Schlichtherle */
@RunWith(classOf[JUnitRunner])
class ResourceLoanTest extends WordSpec {

  "The `loan(resource) to { resource => ... }` statement" when {
    "not throwing a `Throwable` in its code block" should {
      "call `AutoCloseable.close()`" in {
        val resource = mock[AutoCloseable]
        loan(resource) to { param =>
          param should be theSameInstanceAs resource
        }
        verify(resource) close ()
        verifyNoMoreInteractions(resource)
      }
    }

    "throwing a `Throwable` in its code block" should {
      "call `AutoCloseable.close()`" in {
        val resource = mock[AutoCloseable]
        intercept[Throwable] {
          loan(resource) to { param =>
            param should be theSameInstanceAs resource
            throw new Throwable
          }
        }
        verify(resource) close ()
        verifyNoMoreInteractions(resource)
      }
    }

    "catching an `Exception` from `AutoCloseable.close()`" should {
      "pass on the exception" in {
        val resource = mock[AutoCloseable]
        when(resource close ()) thenThrow new Exception
        intercept[Exception] {
          loan(resource) to { param =>
            param should be theSameInstanceAs resource
          }
        }
        verify(resource) close ()
        verifyNoMoreInteractions(resource)
      }
    }

    "throwing a `Throwable` in its code block and catching an `Exception` from `AutoCloseable.close()`" should {
      "chain the throwables via `Throwable.addSuppressed(Throwable)`" in {
        val resource = mock[AutoCloseable]
        val t1 = new Throwable
        val t2 = new Exception
        when(resource close ()) thenThrow t2
        intercept[Throwable] {
          loan(resource) to { param =>
            param should be theSameInstanceAs resource
            throw t1
          }
        } should be theSameInstanceAs t1
        t1.getSuppressed should equal (Array(t2))
        verify(resource) close ()
        verifyNoMoreInteractions(resource)
      }
    }
  }
}
