/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for creating products.
 * Factory services are subject to service location by {@link Locator}s.
 * If multiple factory services are locatable on the class path at run time,
 * the service with the greatest {@linkplain #getPriority() priority} gets
 * selected.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to create.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class FactoryService<P>
extends ProviderService<P> implements Factory<P> {
}
