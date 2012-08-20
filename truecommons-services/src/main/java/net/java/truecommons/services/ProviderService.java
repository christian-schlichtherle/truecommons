/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Provider;

/**
 * An abstract service which provides products.
 * Provider services are subject to service location by {@link Locator}s.
 * <p>
 * If multiple provider services are locatable on the class path at run
 * time, a locator will select the provider service with the greatest
 * {@linkplain #getPriority() priority}.
 * <p>
 * Implementations should be thread-safe.
 *
 * @param  <P> the type of the products to provide.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class ProviderService<P>
extends Service implements Provider<P> {
}
