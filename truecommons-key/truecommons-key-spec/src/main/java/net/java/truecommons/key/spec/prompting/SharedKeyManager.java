/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec.prompting;

import net.java.truecommons.shed.UniqueObject;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @param  <K> the type of the prompting keys.
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@ThreadSafe
final class SharedKeyManager<K extends PromptingKey<K>>
extends UniqueObject {

    private final Map<URI, SharedKeyProvider<K>> providers = new HashMap<>();

    private @CheckForNull SharedKeyProvider<K> get(final URI resource) {
        return providers.get(Objects.requireNonNull(resource));
    }

    synchronized SharedKeyProvider<K> provider(final URI resource) {
        SharedKeyProvider<K> p = get(resource);
        if (null == p)
            providers.put(resource, p = new SharedKeyProvider<>());
        return p;
    }

    synchronized void link(
            final URI oldResource,
            final URI newResource) {
        Objects.requireNonNull(newResource);
        final SharedKeyProvider<K> p = get(oldResource);
        if (null != p)
            providers.put(newResource, p);
    }

    synchronized void unlink(URI resource) {
        providers.remove(Objects.requireNonNull(resource));
    }

    synchronized void release(final URI resource) {
        final SharedKeyProvider<K> p = get(resource);
        if (null != p)
            p.resetCancelledKey();
    }
}
