/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for modifying products.
 * Modifier services are subject to service location by {@link Locator}s.
 * If multiple modifier services are locatable on the class path at run
 * time, they are applied in ascending order of their
 * {@linkplain #getPriority() priority} so that the result of the modifier
 * service with the greatest number becomes the result of the entire
 * modifier chain.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to modify.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class ModifierService<P>
extends FunctionService<P> implements Modifier<P>{
}
