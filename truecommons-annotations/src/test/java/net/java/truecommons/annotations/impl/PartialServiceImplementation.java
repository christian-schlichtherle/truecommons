/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.annotations.impl;

import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.annotations.spec.YetAnotherServiceSpecification;

/**
 * Is this all?!
 *
 * @author Christian Schlichtherle
 */
@ServiceImplementation
public class PartialServiceImplementation
extends YetAnotherServiceSpecification {

    @ServiceImplementation
    public static class BadPracticeImplementation
    extends BadPracticeSpecification {
    }
}
