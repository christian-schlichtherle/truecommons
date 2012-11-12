/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec.spi;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.concurrent.Immutable;
import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.annotations.ServiceSpecification;
import net.java.truecommons.key.spec.KeyManager;
import net.java.truecommons.key.spec.sl.KeyManagerMapLocator;
import net.java.truecommons.services.LocatableFactory;

/**
 * A service for creating maps of classes to nullable key managers.
 * Note that this class solely exists in order to support the
 * {@link KeyManagerMapLocator#SINGLETON}, which will use it to create the
 * manager map and subsequently modify it by the
 * {@link KeyManagerMapModifier} implementations found on the class path.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceSpecification
@ServiceImplementation
public class KeyManagerMapFactory
extends LocatableFactory<Map<Class<?>, KeyManager<?>>> {

    /**
     * Returns a new empty map for subsequent modification.
     *
     * @return A new empty map for subsequent modification.
     */
    @Override
    public Map<Class<?>, KeyManager<?>> get() {
        return new HashMap<>(32);
    }
}
