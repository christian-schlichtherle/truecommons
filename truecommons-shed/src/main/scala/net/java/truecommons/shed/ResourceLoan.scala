/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed

import net.java.truecommons.shed.ResourceLoan.LoanStatement

/** A mix-in trait which provides Java's basic "try-with-resources" statement.
  *
  * Usage example:
  * {{{
  * import net.java.truecommons.shed.ResourceLoan_
  * val out: OutputStream = ...
  * loan(new PrintWriter(out)) to { w: PrintWriter => w.println("Hello world!") }
  * }}}
  * In this example, `w.close()` is guaranteed to get called even if the
  * to-function terminates with a [[Throwable]].
  *
  * In general, if the to-function throws a throwable `t1` and the
  * `AutoCloseable.close()` method throws another throwable `t2`, then the
  * throwable `t2` gets added to the throwable `t1` using
  * `t1.addSuppressed(t2)`.
  *
  * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">The Java Language Specification: Java SE 7 Edition: 14.20.3.1 Basic try-with-resources</a>
  * @author Christian Schlichtherle
  */
trait ResourceLoan {

  /** Starts a `loan(resource) to { ... }` statement.
   *
   * @param resource the autocloseable resource.
   * @tparam A the type of the autocloseable resource.
   */
  def loan[A <: AutoCloseable](resource: A) = new LoanStatement(resource)
}

/** An object which provides Java's basic "try-with-resources" statement.
  *
  * Usage example:
  * {{{
  * import net.java.truecommons.shed.ResourceLoan_
  * val out: OutputStream = ...
  * loan(new PrintWriter(out)) to { w: PrintWriter => w.println("Hello world!") }
  * }}}
  * In this example, `w.close()` is guaranteed to get called even if the
  * to-function terminates with a [[Throwable]].
  *
  * In general, if the to-function throws a throwable `t1` and the
  * `AutoCloseable.close()` method throws another throwable `t2`, then the
  * throwable `t2` gets added to the throwable `t1` using
  * `t1.addSuppressed(t2)`.
  *
  * @see <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">The Java Language Specification: Java SE 7 Edition: 14.20.3.1 Basic try-with-resources</a>
  * @author Christian Schlichtherle
  */
object ResourceLoan extends ResourceLoan {

  class LoanStatement[A <: AutoCloseable](resource: A) {

    /** Applies the loan pattern to the given function.
      * This is a straightforward translation of Java's
      * <a href="http://docs.oracle.com/javase/specs/jls/se7/html/jls-14.html#jls-14.20.3.1">basic <code>try</code>-with-resources</a>
      * statement.
      *
      * @param fun the function with the nullable resource parameter.
      */
    final def to[B](fun: A => B) = {
      var t1: Throwable = null
      try {
        fun(resource)
      } catch {
        case t: Throwable =>
          t1 = t
          throw t
      } finally {
        if (null != resource) {
          try {
            resource close()
          } catch {
            case t2: Throwable =>
              if (null == t1)
                throw t2
              t1 addSuppressed t2
          }
        }
      }
    }
  }
}
