/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.macosx;

import java.util.Map;
import java.util.ServiceConfigurationError;
import javax.annotation.concurrent.Immutable;
import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.key.spec.KeyManager;
import net.java.truecommons.key.spec.common.AesPbeParameters;
import net.java.truecommons.key.spec.spi.KeyManagerMapModifier;

/**
 * Adds the {@link OsxKeyManager} to the map if and only if this JVM is running
 * on Mac OS X.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceImplementation
public final class OsxAesPbeKeyManagerMapModifier
extends KeyManagerMapModifier {

    @Override
    @SuppressWarnings("unchecked")
    public Map<Class<?>, KeyManager<?>> apply(
            final Map<Class<?>, KeyManager<?>> map) {
        if (!"Mac OS X".equals(System.getProperty("os.name"))) return map;
        final KeyManager<?> m = map.get(AesPbeParameters.class);
        if (null == m)
            throw new ServiceConfigurationError(
                "This module is a pure persistence service and depends on another key manager module to implement the user interface.");
        map.put(AesPbeParameters.class,
                new OsxKeyManager<>((KeyManager<AesPbeParameters>) m, AesPbeParameters.class));
        return map;
    }

    /** @return -100 */
    @Override
    public int getPriority() { return -100; }
}
