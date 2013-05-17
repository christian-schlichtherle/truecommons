/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.inject.Provider;

/** @author Christian Schlichtherle */
final class Store<P> implements Container<P> {
    final P product;

    Store(final Provider<P> provider) {
        this.product = provider.get();
    }

    @Override
    public P get() { return product; }
}
