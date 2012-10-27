/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ServiceLoader;

/**
 * Indicates that the annotated class implements a locatable service.
 *
 * @see    ServiceSpecification
 * @see    ServiceLoader
 * @see    Locator
 * @author Christian Schlichtherle
 */
@Target(ElementType.TYPE)
@Documented
public @interface ServiceImplementation {

    /**
     * Returns the implemented specification classes.
     * If empty, all superclasses and implemented interfaces get scanned for
     * {@link ServiceSpecification} annotations.
     *
     * @return The implemented specification classes.
     */
    Class<?>[] value() default {};
}
