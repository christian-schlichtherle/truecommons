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
        c.get should not be (null)
      }
    }

    "asked to create a container" should {
      val c = l.container[String, FactoryService[String], DecoratorService[String]]

      "always reproduce the expected product" in {
        c.get should equal (expected)
        c.get should equal (expected)
      }

      "provide the same product" in {
        val p1 = c.get
        val p2 = c.get
        p1 should be theSameInstanceAs(p2)
      }
    }

    "asked to create a factory" should {
      val f = l.factory[String, FactoryService[String], DecoratorService[String]]

      "always reproduce the expected product" in {
        f.get should equal (expected)
        f.get should equal (expected)
      }

      "provide an equal, but not same product" in {
        val p1 = f.get
        val p2 = f.get
        p1 should equal (p2)
        p1 should not be theSameInstanceAs(p2)
      }
    }
  }
}

object LocatorSpec {

  val expected  = "Hello Christian! How do you do?"

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
  def get = new String("World") // return a new string upon each call
  override def getPriority = -1
}

final class Christian extends FactoryService[String] {
  def get = new String("Christian") // return a new string upon each call
}

final class Salutation extends DecoratorService[String] {
  def apply(text: String) = "Hello %s!" format text
  override def getPriority = -1
}

final class Smalltalk extends DecoratorService[String] {
  def apply(text: String) = text + " How do you do?"
}
