/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io

import javax.annotation.concurrent._

/** @author Christian Schlichtherle */
@deprecated("Use `net.java.truecommons.shed.ResourceLoan` instead.", "TrueCommons 2.5")
object Loan {

  def loan[A <: AutoCloseable](resource: A) = new Loan(resource)

  def apply[A <: AutoCloseable](resource: A) = loan(resource)
}

/** @author Christian Schlichtherle */
@Immutable
@deprecated("Use `net.java.truecommons.shed.ResourceLoan` instead.", "TrueCommons 2.5")
final class Loan[A <: AutoCloseable](resource: A) {

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

  def apply[B](block: A => B) = to(block)
}
