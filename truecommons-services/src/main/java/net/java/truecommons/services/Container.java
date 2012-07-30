/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Provider;

/**
 * Contains a single product.
 * <p>
 * Implementations should be thread-safe.
 *
 * @param  <P> the type of the product to contain.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Container<P> extends Provider<P> {

    /**
     * Returns the <em>same</em> contained product upon each call.
     *
     * @return the <em>same</em> contained product.
     */
    @Override
    P get();
}