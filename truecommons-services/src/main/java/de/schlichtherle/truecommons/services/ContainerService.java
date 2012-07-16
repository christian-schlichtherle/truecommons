/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service which contains a single product.
 * Container services are subject to service location by {@link Locator}s.
 * If multiple container services are locatable on the class path at run time,
 * the service with the greatest {@linkplain #getPriority() priority} gets
 * selected.
 * <p>
 * Implementations should be thread-safe.
 *
 * @param  <P> the type of the product to contain.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class ContainerService<P>
extends ProviderService<P> implements Container<P> {
}
