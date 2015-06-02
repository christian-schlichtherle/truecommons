package net.java.truecommons.shed

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import OnTryFinally._
import org.scalatest.Matchers._

/** @author Christian Schlichtherle */
@RunWith(classOf[JUnitRunner])
class OnTryFinallyTest extends WordSpec {

  "The `onTry { ... } onFinally { ... }` statement" when {
    "not throwing a Throwable" should {
      "execute both code blocks" in {
        var list = List.empty[Int]
        onTry {
          list ::= 1
          list
        } onFinally {
          list ::= 2
          list
        } should equal (List(1))
        list should equal (List(2, 1))
      }
    }

    "throwing a Throwable in the first code block" should {
      "execute both code blocks" in {
        var list = List.empty[Int]
        val t = new Throwable
        intercept[Throwable] {
          onTry {
            list ::= 1
            throw t
          } onFinally {
            list ::= 2
          }
        } should be theSameInstanceAs t
        t.getSuppressed should equal (Array())
        list should equal (List(2, 1))
      }
    }

    "throwing a Throwable in the second code block" should {
      "execute both code blocks" in {
        var list = List.empty[Int]
        val t = new Throwable
        intercept[Throwable] {
          onTry {
            list ::= 1
          } onFinally {
            list ::= 2
            throw t
          }
        } should be theSameInstanceAs t
        t.getSuppressed should equal (Array())
        list should equal (List(2, 1))
      }
    }

    "throwing a Throwable in both code blocks" should {
      "chain the Throwables via `Throwable.addSuppressed(Throwable)`" in {
        var list = List.empty[Int]
        val t1 = new Throwable
        val t2 = new Throwable
        intercept[Throwable] {
          onTry {
            list ::= 1
            throw t1
          } onFinally {
            list ::= 2
            throw t2
          }
        } should be theSameInstanceAs t1
        t1.getSuppressed should equal (Array(t2))
        list should equal (List(2, 1))
      }
    }
  }

  "The `onTry { ... } onThrowable { ... }` statement" when {
    "not throwing a Throwable" should {
      "execute only the first code block" in {
        var list = List.empty[Int]
        onTry {
          list ::= 1
          list
        } onThrowable {
          list ::= 2
          list
        } should equal (List(1))
        list should equal (List(1))
      }
    }

    "throwing a Throwable in the first code block" should {
      "execute both code blocks" in {
        var list = List.empty[Int]
        val t = new Throwable
        intercept[Throwable] {
          onTry {
            list ::= 1
            throw t
          } onThrowable {
            list ::= 2
          }
        } should be theSameInstanceAs t
        t.getSuppressed should equal (Array())
        list should equal (List(2, 1))
      }
    }

    "throwing a Throwable in both code blocks" should {
      "chain the Throwables via `Throwable.addSuppressed(Throwable)`" in {
        var list = List.empty[Int]
        val t1 = new Throwable
        val t2 = new Throwable
        intercept[Throwable] {
          onTry {
            list ::= 1
            throw t1
          } onThrowable {
            list ::= 2
            throw t2
          }
        } should be theSameInstanceAs t1
        t1.getSuppressed should equal (Array(t2))
        list should equal (List(2, 1))
      }
    }
  }
}
