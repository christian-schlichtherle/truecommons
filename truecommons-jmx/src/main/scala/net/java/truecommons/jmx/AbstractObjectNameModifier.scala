/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx

import javax.management._

/**
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
abstract class AbstractObjectNameModifier extends ObjectNameModifier {

  override def apply(instance: ObjectInstance) = {
    if (null eq instance) null
    else new ObjectInstance(apply(instance.getObjectName), instance.getClassName)
  }

  override def unapply(instance: ObjectInstance) = {
    if (null eq instance) null
    else new ObjectInstance(unapply(instance.getObjectName), instance.getClassName)
  }
}
