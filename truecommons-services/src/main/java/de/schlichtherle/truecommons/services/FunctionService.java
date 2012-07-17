/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for mapping products.
 * Function services are subject to service location by {@link Locator}s.
 * <p>
 * If multiple function services are locatable on the class path at run time,
 * they are applied in ascending order of their
 * {@linkplain #getPriority() priority} so that the result of the function
 * service with the greatest number becomes the result of the entire
 * function chain.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to map.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class FunctionService<P>
extends Service implements Function<P>{
}
