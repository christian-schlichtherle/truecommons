/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.shed;

import java.util.*;

/**
 * Assembles an {@link Exception} from one or more input exceptions by
 * {@linkplain Exception#addSuppressed(Throwable) suppressing} all but the
 * first input exception with the highest priority.
 * The priority of the exceptions is determined by the {@link Comparator}
 * provided to the constructor.
 * 
 * @param  <X> the type of the input and assembled (output) exceptions.
 * @author Christian Schlichtherle
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
public class PriorityExceptionBuilder<X extends Throwable>
extends AbstractExceptionBuilder<X, X> {

    private final List<X> exceptions = new LinkedList<>();

    private final Comparator<? super X> comparator;

    /**
     * Constructs a new priority exception builder.
     * This builder will use the first input exception as its initial assembly.
     * Whenever a new input exception gets added, the given comparator will get
     * used to determine if the new input exception shall get suppressed by the
     * current assembly or shall suppress the current assembly and take its
     * place.
     * The comparator will get called like this:
     * {@code comparator.compare(input, assembly)} where {@code input}
     * is the input exception to add and {@code assembly} is the current
     * assembly.
     * 
     * @param comparator the comparator used for prioritizing the exceptions in
     *        the assembly.
     */
    public PriorityExceptionBuilder(final Comparator<? super X> comparator) {
        this.comparator = Objects.requireNonNull(comparator);
    }

    @Override
    protected final X update(final X input, final Option<X> assembly) {
        exceptions.add(input);
        for (final X t : assembly)
            return comparator.compare(input, t) > 0 ? input : t;
        return input;
    }

    @Override
    protected final X post(final X selection) {
        for (   final Iterator<X> i = exceptions.iterator();
                i.hasNext();
                i.remove()) {
            final X exception = i.next();
            if (selection != exception)
                selection.addSuppressed(exception);
        }
        assert exceptions.isEmpty();
        return selection;
    }
}
