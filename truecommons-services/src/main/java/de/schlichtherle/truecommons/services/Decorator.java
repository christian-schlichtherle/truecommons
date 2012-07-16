/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

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
public interface Decorator<P> {

    /**
     * Decorates the given product.
     * 
     * @param  product the product to decorate.
     * @return The decorated product.
     */
    P apply(P product);
}
