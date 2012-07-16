/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

/**
 * @author Christian Schlichtherle
 */
final class Store<P> implements Container<P> {
    final P product;

    Store(final Provider<P> provider) {
        this.product = provider.apply();
    }

    @Override
    public P apply() {
        return product;
    }
}
