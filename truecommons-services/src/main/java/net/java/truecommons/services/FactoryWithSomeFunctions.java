/*
 * Copyright (C) 2012-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.Immutable;

/** @author Christian Schlichtherle */
@Immutable
final class FactoryWithSomeFunctions<P>
extends ProviderWithSomeFunctions<P> implements Factory<P> {
    FactoryWithSomeFunctions(Factory<P> factory, Function<P>[] functions) {
        super(factory, functions);
    }
}
