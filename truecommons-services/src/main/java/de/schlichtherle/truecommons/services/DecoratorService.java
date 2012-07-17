/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for decorating products.
 * Decorator services are subject to service location by {@link Locator}s.
 * If multiple decoration services are locatable on the class path at run
 * time, they are applied in ascending order of their
 * {@linkplain #getPriority() priority} so that the result of the decorator
 * service with the greatest number becomes the result of the entire
 * decorator chain.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to decorate.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class DecoratorService<P>
extends FunctionService<P> implements Decorator<P>{
}
