/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.inject.Provider;

/**
 * @author Christian Schlichtherle
 */
class ProviderWithSomeFunctions<P> implements Provider<P> {
    private final Provider<P> provider;
    private final Function<P>[] functions;

    ProviderWithSomeFunctions(
            final Provider<P> provider,
            final Function<P>[] functions) {
        assert null != provider;
        assert 0 != functions.length;
        this.provider = provider;
        this.functions = functions;
    }

    @Override
    public P get() {
        P product = provider.get();
        for (Function<P> function : functions)
            product = function.apply(product);
        return product;
    }
}
