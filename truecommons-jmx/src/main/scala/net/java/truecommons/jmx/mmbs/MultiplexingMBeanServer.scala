/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx.mmbs

import collection.JavaConverters._
import javax.management._
import net.java.truecommons.jmx._

/**
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
final class MultiplexingMBeanServer
(override val mbs: MBeanServer, modifier: ObjectNameModifier)
extends DecoratingMBeanServer with Immutable {

  override def createMBean(className: String, name: ObjectName) =
    modifier.unapply(mbs createMBean (className, modifier.apply(name)))

  override def createMBean(className: String, name: ObjectName, loaderName: ObjectName) =
    modifier.unapply(mbs createMBean (className, modifier.apply(name), loaderName))

  override def createMBean(className: String, name: ObjectName, params: Array[AnyRef], signature: Array[String]) =
    modifier.unapply(mbs createMBean (className, modifier.apply(name), params, signature))

  override def createMBean(className: String, name: ObjectName, loaderName: ObjectName, params: Array[AnyRef], signature: Array[String]) =
    modifier.unapply(mbs createMBean (className, modifier.apply(name), loaderName, params, signature))

  override def registerMBean(`object`: Any, name: ObjectName) =
    mbs registerMBean (`object`, modifier.apply(name))

  override def unregisterMBean(name: ObjectName) {
    mbs unregisterMBean modifier.apply(name)
  }

  override def getObjectInstance(name: ObjectName) =
    modifier.unapply(mbs getObjectInstance modifier.apply(name))

  override def queryMBeans(name: ObjectName, query: QueryExp) =
    ((mbs queryMBeans (modifier.apply(name), query)).asScala map modifier.unapply).asJava

  override def queryNames(name: ObjectName, query: QueryExp) =
    ((mbs queryNames (modifier.apply(name), query)).asScala map modifier.unapply).asJava

  override def isRegistered(name: ObjectName) =
    mbs isRegistered modifier.apply(name)

  override def getAttribute(name: ObjectName, attribute: String) =
    mbs getAttribute (modifier.apply(name), attribute)

  override def getAttributes(name: ObjectName, attributes: Array[String]) =
    mbs getAttributes (modifier.apply(name), attributes)

  override def setAttribute(name: ObjectName, attribute: Attribute) {
    mbs setAttribute (modifier.apply(name), attribute)
  }

  override def setAttributes(name: ObjectName, attributes: AttributeList) =
    mbs setAttributes (modifier.apply(name), attributes)

  override def invoke(name: ObjectName, operationName: String, params: Array[AnyRef], signature: Array[String]) =
    mbs invoke (modifier.apply(name), operationName, params, signature)

  override def addNotificationListener(name: ObjectName, listener: NotificationListener, filter: NotificationFilter, handback: Any) {
    mbs addNotificationListener (modifier.apply(name), listener, filter, handback)
  }

  override def addNotificationListener(name: ObjectName, listener: ObjectName, filter: NotificationFilter, handback: Any) {
    mbs addNotificationListener (modifier.apply(name), listener, filter, handback)
  }

  override def removeNotificationListener(name: ObjectName, listener: ObjectName) {
    mbs removeNotificationListener (modifier.apply(name), listener)
  }

  override def removeNotificationListener(name: ObjectName, listener: ObjectName, filter: NotificationFilter, handback: Any) {
    mbs removeNotificationListener (modifier.apply(name), listener, filter, handback)
  }

  override def removeNotificationListener(name: ObjectName, listener: NotificationListener) {
    mbs removeNotificationListener (modifier.apply(name), listener)
  }

  override def removeNotificationListener(name: ObjectName, listener: NotificationListener, filter: NotificationFilter, handback: Any) {
    mbs removeNotificationListener (modifier.apply(name), listener, filter, handback)
  }

  override def getMBeanInfo(name: ObjectName) =
    mbs getMBeanInfo modifier.apply(name)

  override def isInstanceOf(name: ObjectName, className: String) =
    mbs isInstanceOf (modifier.apply(name), className)

  @deprecated("", "")
  override def deserialize(name: ObjectName, data: Array[Byte]) =
    mbs deserialize (modifier.apply(name), data)

  override def getClassLoaderFor(mbeanName: ObjectName) =
    mbs getClassLoaderFor modifier.apply(mbeanName)
}
