/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * An immutable collection of at most one element.
 * As with any collection, the most idiomatic way to use it is as follows:
 * <pre>{@code
 * Option<String> option = Option.apply("Hello world!"); // or Option.apply(null)
 * for (String string : option) System.out.println(string);
 * }</pre>
 * <p>
 * A less idiomatic way is the following:
 * <pre>{@code
 * Option<String> option = Option.apply("Hello world!"); // or Option.apply(null)
 * if (!option.isEmpty()) System.out.println(option.get());
 * }</pre>
 * <p>
 * If you use this class in these ways, your code clearly expresses the
 * intention that its prepared to deal with the absence of an object in a
 * collection and won't throw a {@link NullPointerException} in this case.
 * <p>
 * This class is inspired by the Scala Library and checked with Google's Guava
 * Library:
 * A noteable difference to Scala's {@code Option} class is that this
 * collection class cannot contain null elements because I can't imagine a
 * valid use case - not even when considering interoperability with other
 * collections of nullable items.
 * <p>
 * A noteable difference to Guava's {@code Optional} class is that this class
 * is a collection while {@code Optional} is not, so you can't use a for-loop
 * with the latter.
 * <p>
 * A noteable difference to both libraries is that this class doesn't support
 * a generic Function interface.
 * This is because without support for closures in Java 7, using a generic
 * functional interface in Java is not as convenient as the for-loop.
 *
 * @param  <E> The type of the optional element in this container.
 * @author Christian Schlichtherle
 */
@Immutable
public abstract class Option<E>
extends AbstractCollection<E> implements Serializable {

    private static final long serialVersionUID = 0L;

    private Option() { }

    /**
     * Returns an option for the given nullable element.
     *
     * @param <T> The type of the nullable element.
     * @param element the element.
     * @return An option for the given nullable element.
     */
    public static <T> Option<T> apply(@CheckForNull T element) {
        return null == element ? (Option<T>) none() : some(element);
    }

    private static <T> Option<T> none() { return (Option<T>) None.INSTANCE; }

    private static <T> Option<T> some(final T element) {
        assert null != element;
        return new Some<>(element);
    }

    /**
     * If present, returns the single element contained in this collection,
     * or otherwise throws an exception.
     *
     * @return The single element in this collection.
     * @throws NoSuchElementException if no element is present in this
     *         collection.
     */
    public abstract E get() throws NoSuchElementException;

    /**
     * If present, returns the single element contained in this collection,
     * or otherwise the given alternative.
     *
     * @param alternative the alternative element.
     */
    public abstract @Nullable E getOrElse(@CheckForNull E alternative);

    /**
     * Equivalent to {@link #getOrElse getOrElse(null)}, but probably more
     * efficient.
     */
    public abstract @Nullable E orNull();

    /**
     * If an element is present in this collection, then this collection is
     * returned, or otherwise the given alternative.
     *
     * @param alternative the alternative option.
     */
    public Option<E> orElse(Option<E> alternative) {
        return isEmpty() ? alternative : this;
    }

    @Override
    public abstract boolean equals(final Object other);

    @Override
    public abstract int hashCode();

    private static final class None<T> extends Option<T> {

        static final None<Object> INSTANCE = new None<>();

        @Override
        public Iterator<T> iterator() { return Collections.emptyIterator(); }

        @Override
        public int size() { return 0; }

        @Override
        public boolean isEmpty() { return true; }

        @Override
        public T get() { throw new NoSuchElementException(); }

        @Override
        public T getOrElse(T alternative) { return alternative; }

        @Override
        public T orNull() { return null; }

        @Override
        public boolean equals(final Object other) {
            return other instanceof None;
        }

        @Override
        public int hashCode() { return 42; }
    } // None

    private static final class Some<T> extends Option<T> {

        final T element;

        Some(final T element) {
            assert null != element;
            this.element = element;
        }

        @Override
        public Iterator<T> iterator() {
            return Collections.singleton(element).iterator();
        }

        @Override
        public int size() { return 1; }

        @Override
        public boolean isEmpty() { return false; }

        @Override
        public T get() { return element; }

        @Override
        public T getOrElse(T alternative) { return this.element; }

        @Override
        public T orNull() { return element; }

        @Override
        public boolean equals(final Object that) {
            return this == that
                    || that instanceof Some
                    && this.element.equals(((Some<?>) that).element);
        }

        @Override
        public int hashCode() { return element.hashCode(); }
    } // Some
}
