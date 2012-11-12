/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.console;

import java.util.Map;
import javax.annotation.concurrent.Immutable;
import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.key.spec.KeyManager;
import net.java.truecommons.key.spec.common.AesPbeParameters;
import net.java.truecommons.key.spec.prompting.PromptingKeyManager;
import net.java.truecommons.key.spec.spi.KeyManagerMapModifier;

/**
 * A service provider for a console prompting key manager for
 * {@link AesPbeParameters}.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceImplementation
public final class ConsoleAesPbeKeyManagerMapModifier
extends KeyManagerMapModifier {

    @Override
    public Map<Class<?>, KeyManager<?>> apply(final Map<Class<?>, KeyManager<?>> map) {
        map.put(AesPbeParameters.class,
                new PromptingKeyManager<>(new ConsoleAesPbeParametersView()));
        return map;
    }

    /** @return -100 if console I/O is available, -300 otherwise. */
    @Override
    public int getPriority() {
        return null == System.console() ? -300 : -100;
    }
}
