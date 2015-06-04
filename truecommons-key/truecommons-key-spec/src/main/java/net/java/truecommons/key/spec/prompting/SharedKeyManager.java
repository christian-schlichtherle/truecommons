/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec.prompting;

import net.java.truecommons.shed.Option;
import net.java.truecommons.shed.UniqueObject;

import javax.annotation.concurrent.ThreadSafe;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static net.java.truecommons.shed.Option.apply;

/**
 * @param  <K> the type of the prompting keys.
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
@ThreadSafe
final class SharedKeyManager<K extends PromptingKey<K>>
extends UniqueObject {

    private final Map<URI, SharedKeyProvider<K>> providers = new HashMap<>();

    private Option<SharedKeyProvider<K>> get(URI resource) {
        return apply(providers.get(resource));
    }

    private Option<SharedKeyProvider<K>> put(URI resource, SharedKeyProvider<K> p) {
        return apply(providers.put(resource, p));
    }

    private Option<SharedKeyProvider<K>> remove(URI resource) {
        return apply(providers.remove(resource));
    }

    synchronized SharedKeyProvider<K> provider(final URI resource) {
        for (final SharedKeyProvider<K> p : get(resource))
            return p;
        final SharedKeyProvider<K> p = new SharedKeyProvider<K>();
        put(resource, p);
        p.link();
        return p;
    }

    synchronized void release(final URI resource) {
        for (final SharedKeyProvider<K> p : get(resource))
            p.release();
    }

    synchronized void link(final URI originResource, final URI targetResource) {
        for (final SharedKeyProvider<K> originProvider : get(originResource)) {
            for (final SharedKeyProvider<K> targetProvider : put(targetResource, originProvider)) {
                if (targetProvider == originProvider)
                    return;
                targetProvider.unlink();
            }
            originProvider.link();
        }
    }

    synchronized void unlink(final URI resource) {
        for (final SharedKeyProvider<K> p : remove(resource))
            p.unlink();
    }
}
