/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.disable;

import java.util.Arrays;
import net.java.truecommons.key.spec.common.AesPbeParameters;
import net.java.truecommons.key.spec.spi.KeyManagerMapModifier;
import net.java.truecommons.key.spec.spi.KeyManagerMapModifierTestSuite;
import static org.junit.Assert.*;

/**
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
public class DisableAesPbeKeyManagerMapModifierTest
extends KeyManagerMapModifierTestSuite {

    @Override
    @SuppressWarnings("unchecked")
    protected Iterable<Class<?>> getClasses() {
        return (Iterable<Class<?>>) Arrays.asList((Class<?>) AesPbeParameters.class);
    }

    @Override
    protected KeyManagerMapModifier newModifier() {
        return new DisableAesPbeKeyManagerMapModifier();
    }

    @Override
    public void testPriority() {
        assertTrue(modifier.getPriority() == Integer.MAX_VALUE);
    }
}
