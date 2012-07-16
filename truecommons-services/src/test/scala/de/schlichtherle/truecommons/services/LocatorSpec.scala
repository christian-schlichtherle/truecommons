/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services

import java.util._
import org.junit.runner._
import org.scalatest._
import org.scalatest.junit._
import org.scalatest.matchers._

@RunWith(classOf[JUnitRunner])
class LocatorSpec extends WordSpec with ShouldMatchers {

  import LocatorSpec._

  def locator[P] = new LocatorSugar

  "A locator" when {
    val l = locator

    "asked to create a container" should {
      "report a service configuration error if it can't locate a factory" in {
        intercept[ServiceConfigurationError] {
          l.container[String, UnknownFactory]
        }
      }

      "not report a service configuration error if it can't locate a decorator" in {
        val c = l.container[String, FactoryService[String], UnknownDecorator]
        c.apply should not be (null)
      }
    }

    "asked to create a container" should {
      val c = l.container[String, FactoryService[String], DecoratorService[String]]

      "always reproduce the expected product" in {
        val p = "Hello Christian! How do you do?"
        c.apply should equal (p)
        c.apply should equal (p)
      }

      "provide the same product" in {
        val p1 = c.apply
        val p2 = c.apply
        p1 should be theSameInstanceAs(p2)
      }
    }

    "asked to create a factory" should {
      val f = l.factory[String, FactoryService[String], DecoratorService[String]]

      "always reproduce the expected product" in {
        val p = "Hello Christian! How do you do?"
        f.apply should equal (p)
        f.apply should equal (p)
      }

      "provide an equal, but not same product" in {
        val p1 = f.apply
        val p2 = f.apply
        p1 should equal (p2)
        p1 should not be theSameInstanceAs(p2)
      }
    }
  }
}

object LocatorSpec {
  final class LocatorSugar {
    private[this] val l = new Locator(classOf[LocatorSpec])

    def container[P, F <: FactoryService[P] : Manifest] =
      l container (implicitly[Manifest[F]].erasure.asInstanceOf[Class[F]])

    def container[P, F <: FactoryService[P] : Manifest, D <: DecoratorService[P] : Manifest] =
      l container (implicitly[Manifest[F]].erasure.asInstanceOf[Class[F]],
                   implicitly[Manifest[D]].erasure.asInstanceOf[Class[D]])

    def factory[P, F <: FactoryService[P] : Manifest] =
      l factory (implicitly[Manifest[F]].erasure.asInstanceOf[Class[F]])

    def factory[P, F <: FactoryService[P] : Manifest, D <: DecoratorService[P] : Manifest] =
      l factory (implicitly[Manifest[F]].erasure.asInstanceOf[Class[F]],
                 implicitly[Manifest[D]].erasure.asInstanceOf[Class[D]])
  }
}

abstract class UnknownFactory extends FactoryService[String]
abstract class UnknownDecorator extends DecoratorService[String]

final class World extends FactoryService[String] {
  def apply = new String("World") // return a new string upon each call
  override def getPriority = -1
}

final class Christian extends FactoryService[String] {
  def apply = new String("Christian") // return a new string upon each call
}

final class Salutation extends DecoratorService[String] {
  def apply(text: String) = "Hello %s!" format text
  override def getPriority = -1
}

final class Smalltalk extends DecoratorService[String] {
  def apply(text: String) = text + " How do you do?"
}
