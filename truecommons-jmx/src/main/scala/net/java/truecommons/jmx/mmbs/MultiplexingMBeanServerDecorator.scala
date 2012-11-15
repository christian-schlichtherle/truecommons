/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.mmbs

import javax.management._
import net.java.truecommons.jmx._
import net.java.truecommons.jmx.spi._
import net.java.truecommons.jmx.sl.ObjectNameModifierLocator

/**
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
@deprecated("This class is reserved for exclusive use by the [[net.java.truecommons.jmx.sl.MBeanServerLocator.SINGLETON]]!", "1")
final class MultiplexingMBeanServerDecorator
extends MBeanServerDecorator with Immutable {

  override def apply(mbs: MBeanServer): MBeanServer = {
    if (getClass.getClassLoader eq ClassLoader.getSystemClassLoader) mbs
    else new MultiplexingMBeanServer(mbs, ObjectNameModifierLocator.SINGLETON.get)
  }

  override def getPriority = -100
}
