/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

/**
 * @author Christian Schlichtherle
 */
final class FactoryWithSomeDecorators<P>
extends ProviderWithSomeDecorators<P> implements Factory<P> {
    FactoryWithSomeDecorators(Factory<P> factory, Decorator<P>[] decorators) {
        super(factory, decorators);
    }
}
