/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec.prompting;

import net.java.truecommons.key.spec.prompting.PromptingKeyManager;
import net.java.truecommons.key.spec.KeyManagerTestSuite;

/**
 * @author Christian Schlichtherle
 */
public class PromptingKeyManagerTest
extends KeyManagerTestSuite<PromptingKeyManager<?>> {

    @Override
    protected PromptingKeyManager<?> newKeyManager() {
        return new PromptingKeyManager<>(new TestView<>());
    }
}
