/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.qonm

import net.java.truecommons.jmx._
import net.java.truecommons.jmx.spi.ObjectNameModifierFactory

/**
 * Okay, this class name is completely nuts!
 *
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@deprecated("This class is reserved for exclusive use by the [[net.java.truecommons.jmx.sl.ObjectNameModifierLocator.SINGLETON]]!", "1")
final class ClassLoaderQualifierObjectNameModifierFactory
extends ObjectNameModifierFactory with Immutable {

  override def get: ObjectNameModifier = {
    val cl = getClass.getClassLoader
    new QualifierObjectNameModifier(
      "CLASS_LOADER",
      cl.getClass.getName + '@' +
      (Integer toHexString (System identityHashCode cl)))
  }

  override def getPriority = -100
}
