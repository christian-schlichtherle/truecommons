/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services.annotations.processing

import scala.tools.nsc._
import scala.tools.nsc.plugins._

/** @author Christian Schlichtherle */
class ServiceSpecificationPlugin(val global: Global) extends Plugin { plugin =>

  val name = "ServiceSpecificationPlugin"
  val description = "Processes the @ServiceSpecification annotation."
  val components = List[PluginComponent](Component)

  object Component extends PluginComponent {

    val global: plugin.global.type = plugin.global
    val phaseName = "processServiceSpecificationAnnotation"
    val runsAfter = List("refchecks")

    def newPhase(phase: Phase): Phase = new ServiceSpecificationPhase(phase)

    class ServiceSpecificationPhase(phase: Phase) extends StdPhase(phase) {

      import global._

      def apply(unit: CompilationUnit) {
        println("Hello World!")
      }
    }
  }
}
