/*
 * Copyright (C) 2012-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io

import java.io._
import javax.annotation.concurrent._

/** Simulates Java's basic `try`-with-resources statement to implement the loan
  * pattern.
  *
  * When used like this
  * {{{
  * import net.java.truecommons.io.Loan
  * val out: OutputStream = ...
  * Loan (new PrintWriter(out)) { w: PrintWriter => w.println("Hello world!") }
  * }}}
  * then `w.close()` is guaranteed to get called, even if the function block
  * terminates with a [[java.lang.Throwable]].
  *
  * If the function block throws a `Throwable` `ex` and the `close` method
  * throws another `Throwable` `ex2`, then the exception of the `close` method
  * gets added to the exception of the function block using
  * `ex.addSuppressed(ex2)`.
  *
  * If you prefer a more readable syntax, please check the companion object of
  * this class.
  *
  * @param resource the nullable resource to close upon a call to `apply`.
  * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">The Java Language Specification: Java SE 7 Edition: 14.20.3.1 Basic try-with-resources</a>
  * @author Christian Schlichtherle
  */
@Immutable
final class Loan[A <: AutoCloseable](resource: A) {

  /** Applies the loan pattern to the given function.
    * This is pretty much a literal translation of Java's
    * <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">basic <code>try</code>-with-resources</a>
    * statement.
    *
    * @param block the function with the nullable resource parameter.
    */
  def to[B](block: A => B) = {
    var t: Throwable = null
    try {
      block(resource)
    } catch {
      case x: Throwable => t = x; throw x
    } finally {
      if (resource != null) {
        if (t != null) {
          try {
            resource.close()
          } catch {
            case y: Throwable => t.addSuppressed(y)
          }
        } else {
          resource.close()
        }
      }
    }
  }

  /** Equivalent to `to`. */
  def apply[B](block: A => B) = to(block)
}

/** Simulates Java's basic `try`-with-resources statement to implement the loan
  * pattern.
  *
  * When used like this
  * {{{
  * import net.java.truecommons.io.Loan._
  * val out: OutputStream = ...
  * loan (new PrintWriter(out)) to { w: PrintWriter => w.println("Hello world!") }
  * }}}
  * then `w.close()` is guaranteed to get called, even if the function block
  * terminates with a [[java.lang.Throwable]].
  *
  * If the function block throws a `Throwable` `ex` and the `close` method
  * throws another `Throwable` `ex2`, then the exception of the `close` method
  * gets added to the exception of the function block using
  * `ex.addSuppressed(ex2)`.
  *
  * If you prefer a more concise syntax, please check the companion class of
  * this object.
  *
  * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">The Java Language Specification: Java SE 7 Edition: 14.20.3.1 Basic try-with-resources</a>
  * @author Christian Schlichtherle
  */
object Loan {
  /** Constructs a new loan.
    * @param resource the nullable resource to close upon a call to `to`.
    */
  def loan[A <: AutoCloseable](resource: A) = new Loan(resource)

  /** Equivalent to `loan`. */
  def apply[A <: AutoCloseable](resource: A) = loan(resource)
}
