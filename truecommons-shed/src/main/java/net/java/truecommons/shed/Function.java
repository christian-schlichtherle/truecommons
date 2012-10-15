/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

/**
 * A completely generic function interface.
 * 
 * @param  <I> The parameter type of {@link #apply(Object)}
 * @param  <O> The return type of {@link #apply(Object)}.
 * @param  <X> The exception type of {@link #apply(Object)}.
 * @since  TrueCommons 1.0.15
 * @author Christian Schlichtherle
 */
public interface Function<I, O, X extends Throwable> {

    /**
     * Applies this function.
     * 
     * @param  param the function parameter.
     * @return the result of the function.
     * @throws Throwable if the function fails for some reason.
     */
    O apply(I param) throws X;
}
