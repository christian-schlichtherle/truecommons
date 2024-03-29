/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.key.def;

import net.java.truecommons3.annotations.ServiceImplementation;
import net.java.truecommons3.key.spec.KeyManager;
import net.java.truecommons3.key.spec.common.AesPbeParameters;
import net.java.truecommons3.key.spec.spi.KeyManagerMapModifier;
import net.java.truecommons3.key.spec.unknown.UnknownKeyManager;

import javax.annotation.concurrent.Immutable;
import java.util.Map;

/**
 * This modifier maps the {@link AesPbeParameters} class to the
 * {@linkplain UnknownKeyManager#SINGLETON unknown key manager} singleton
 * which fails to resolve any keys.
 * Note that the {@link #getPriority} of this modifier is
 * {@link Integer#MIN_VALUE}, so any other modifier on the class path takes
 * precedence.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceImplementation
public final class DefaultAesPbeKeyManagerMapModifier
extends KeyManagerMapModifier {

    @Override
    public Map<Class<?>, KeyManager<?>> apply(final Map<Class<?>, KeyManager<?>> map) {
        map.put(AesPbeParameters.class, UnknownKeyManager.SINGLETON);
        return map;
    }

    /** @return {@link Integer#MIN_VALUE} */
    @Override
    public int getPriority() { return Integer.MIN_VALUE; }
}
