/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Maps products.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to map.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Function<P> {

    /**
     * Maps the given product.
     * 
     * @param  product the product to map.
     * @return A new product or the same, possibly modified, product.
     */
    P apply(P product);
}
