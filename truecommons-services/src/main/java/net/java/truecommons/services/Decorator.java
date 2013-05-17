/*
 * Copyright (C) 2012-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Decorates products.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to decorate.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Decorator<P> extends Function<P> {

    /**
     * Possibly decorates the given {@code product} and returns the result,
     * leaving the given product unmodified.
     * 
     * @param  product the product to decorate.
     * @return A <em>new</em> decorating product or the same, but strictly
     *         unmodified, product.
     */
    @Override
    P apply(P product);
}
