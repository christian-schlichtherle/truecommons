/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Provides products.
 * <p>
 * Implementations should be thread-safe.
 *
 * @param  <P> the type of the products to provide.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Provider<P> {

    /**
     * Provides a product.
     * <p>
     * Implementations are free to return the same instance (property method)
     * or a new instance (factory method) upon each call.
     * So clients may need to cache the result for future reuse.
     *
     * @return A product.
     */
    P apply();
}
