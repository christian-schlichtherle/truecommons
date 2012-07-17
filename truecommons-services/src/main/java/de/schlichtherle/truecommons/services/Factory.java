/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Creates products.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to create.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Factory<P> extends Provider<P> {

    /**
     * Returns a <em>new</em> product upon each call.
     *
     * @return A <em>new</em> product.
     */
    @Override
    P apply();
}
