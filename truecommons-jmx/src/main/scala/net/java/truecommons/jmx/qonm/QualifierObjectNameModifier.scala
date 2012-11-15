/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.qonm

import java.util._
import javax.management._
import net.java.truecommons.jmx._

/**
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
final class QualifierObjectNameModifier
(key: String, value: String)
extends AbstractObjectNameModifier with Immutable {

  private def getKeyPropertyList(name: ObjectName) = name.getKeyPropertyList

  private def copyKeyPropertyList(name: ObjectName) =
    new Hashtable[String, String](name.getKeyPropertyList)

  private[this] val keyPropertyList = {
    val name = new ObjectName(":test=foo")
    try {
      val table = getKeyPropertyList(name)
      table remove "test"
      getKeyPropertyList _
    } catch {
      case _: UnsupportedOperationException =>
        copyKeyPropertyList _
    }
  }

  override def apply(name: ObjectName) = {
    if (null eq name) null
    else {
      val domain = name.getDomain
      val table = keyPropertyList(name)
      if (null ne (table put (key, value))) name
      else new ObjectName(domain, table)
    }
  }

  override def unapply(name: ObjectName) = {
    if (null eq name) null
    else {
      val domain = name.getDomain
      val table = keyPropertyList(name)
      if (null eq (table remove key)) name
      else new ObjectName(domain, table)
    }
  }
}
