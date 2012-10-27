/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services.annotations.spec;

import net.java.truecommons.services.annotations.ServiceSpecification;

/**
 * Yet another service specification.
 *
 * @author Christian Schlichtherle
 */
@ServiceSpecification
public abstract class YetAnotherServiceSpecification {
    protected YetAnotherServiceSpecification() { }

    @ServiceSpecification
    public static abstract class BadPracticeSpecification { }
}
