/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.shed

import net.java.truecommons3.shed.OnTryFinally.OnTryStatement

/**
 * A mix-in trait which provides an `onTry { ... } onFinally { ... }` and
 * `onTry { ... } onThrowable { ... }` statement.
 * These statements chain exceptions from both code blocks via
 * `Throwable.addSuppressed(Throwable)`.
 *
 * @author Christian Schlichtherle
 */
trait OnTryFinally {

  /**
   * Starts an `onTry { tryBlock } onFinally { ... }` or
   * `onTry { tryBlock } onThrowable { ... }` statement.
   *
   * @param tryBlock the code block to execute first.
   * @tparam A the return type of the code block.
   */
  def onTry[A](tryBlock: => A) = new OnTryStatement(tryBlock)
}

/**
 * An object which provides an `onTry { ... } onFinally { ... }` and
 * `onTry { ... } onThrowable { ... }` statement.
 * These statements chain exceptions from both code blocks via
 * `Throwable.addSuppressed(Throwable)`.
 *
 * @author Christian Schlichtherle
 */
object OnTryFinally extends OnTryFinally {

  class OnTryStatement[A](tryBlock: => A) {

    /**
     * Executes an `onTry { tryBlock } onFinally { finallyBlock }` statement.
     *
     * This statement has the same semantics like the built-in
     * `try { tryBlock } finally { finallyBlock }` statement except that if
     * both code blocks throw a [[Throwable]], then the throwable `t2` from the
     * `finallyBlock` gets added to the throwable `t1` from the `tryBlock` using
     * `t1.addSuppressed(t2)`.
     *
     * @param finallyBlock the code block to unconditionally execute after the
     *                     `tryBlock` code block.
     * @return the return value of the `tryBlock` code block.
     */
    final def onFinally(finallyBlock: => Any) = {
      var t1: Throwable = null
      try {
        tryBlock
      } catch {
        case t: Throwable =>
          t1 = t
          throw t
      } finally {
        try {
          finallyBlock
        } catch {
          case t2: Throwable =>
            if (null == t1)
              throw t2
            t1 addSuppressed t2
        }
      }
    }

    /**
     * Executes an `onTry { tryBlock } onThrowable { throwBlock }` statement.
     *
     * This statement executes the `throwBlock` if and only if the `tryBlock`
     * throws a [[Throwable]].
     * If both code blocks throw a throwable, then the throwable `t2` from the
     * `throwBlock` gets added to the throwable `t1` from the `tryBlock` using
     * `t1.addSuppressed(t2)`.
     *
     * @param throwBlock the code block to execute if and only if the `tryBlock`
     *                   code block throws some [[Throwable]].
     * @return the return value of the `tryBlock` code block.
     */
    final def onThrowable(throwBlock: => Any) = {
      try {
        tryBlock
      } catch {
        case t1: Throwable =>
          try {
            throwBlock
          } catch {
            case t2: Throwable =>
              t1 addSuppressed t2
          }
          throw t1
      }
    }
  }
}
