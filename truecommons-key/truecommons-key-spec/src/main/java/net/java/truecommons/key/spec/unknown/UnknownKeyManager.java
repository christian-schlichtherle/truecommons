/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec.unknown;

import java.net.URI;
import javax.annotation.concurrent.ThreadSafe;
import net.java.truecommons.key.spec.AbstractKeyManager;
import net.java.truecommons.key.spec.KeyManager;
import net.java.truecommons.key.spec.KeyProvider;

/**
 * This key manager fails to resolve any keys.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@ThreadSafe
public final class UnknownKeyManager extends AbstractKeyManager<Object> {

    /** The singleton instance of this class. */
    public static final KeyManager<Object> SINGLETON = new UnknownKeyManager();

    private UnknownKeyManager() { }

    @Override
    public KeyProvider<Object> provider(URI resource) {
        return UnknownKeyProvider.SINGLETON;
    }

    @Override
    public void link(URI oldResource, URI newResource) { }

    @Override
    public void unlink(URI resource) { }

    @Override
    public void release(URI resource) { }
}
