/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Modifies products.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to modify.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public interface Modifier<P> extends Function<P> {

    /**
     * Possibly modifies the given product and returns it again.
     * 
     * @param  product the product to modify.
     * @return The <em>same</em>, but possibly modified, {@code product}.
     */
    @Override
    P apply(P product);
}
