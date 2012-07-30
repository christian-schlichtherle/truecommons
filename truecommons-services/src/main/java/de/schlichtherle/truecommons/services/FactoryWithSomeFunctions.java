/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

/**
 * @author Christian Schlichtherle
 */
final class FactoryWithSomeFunctions<P>
extends ProviderWithSomeFunctions<P> implements Factory<P> {
    FactoryWithSomeFunctions(Factory<P> factory, Function<P>[] functions) {
        super(factory, functions);
    }
}
