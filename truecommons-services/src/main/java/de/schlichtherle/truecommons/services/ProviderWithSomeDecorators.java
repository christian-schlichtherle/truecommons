/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

/**
 * @author Christian Schlichtherle
 */
class ProviderWithSomeDecorators<P> implements Provider<P> {
    final Provider<P> provider;
    final Decorator<P>[] decorators;

    ProviderWithSomeDecorators(
            final Provider<P> provider,
            final Decorator<P>[] decorators) {
        assert null != provider;
        assert 0 != decorators.length;
        this.provider = provider;
        this.decorators = decorators;
    }

    @Override
    public P apply() {
        P product = provider.apply();
        for (Decorator<P> decorator : decorators)
            product = decorator.apply(product);
        return product;
    }
}
