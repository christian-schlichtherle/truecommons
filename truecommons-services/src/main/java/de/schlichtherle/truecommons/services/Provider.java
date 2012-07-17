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
     *
     * @return A product.
     */
    P apply();
}
