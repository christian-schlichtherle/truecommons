/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
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
 * Option<String> option = Option.some("Hello world!"); // or Option.none()
 * if (!option.isEmpty()) System.out.println(option.get());
 * }</pre>
 * <p>
 * If you use this class in these ways, your code clearly expresses the
 * intention that its prepared to deal with the absence of an object in a
 * collection and won't throw a {@link NullPointerException} in this case.
 * Here's a more complex example with composed options:
 * <p>
 * <pre>{@code
 * class Container {
 *     Option<String> getMessage() { return Option.some("Hello world!"); }
 * }
 *
 * Option<Container> option = Option.some(new Container()); // or Option.none()
 * for (Container c : option)
 *     for (String s : c.getMessage())
 *         System.out.println(s);
 * }</pre>
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
 * @since  TrueCommons 2.4
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
    @SuppressWarnings("unchecked")
    public static <T> Option<T> apply(@CheckForNull T element) {
        return null == element
                ? (Option<T>) None.INSTANCE
                : new Some<>(element);
    }

    /**
     * Returns an option with no element.
     *
     * @param <T> the type of the absent element.
     * @return An option with no element.
     */
    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() { return (Option<T>) None.INSTANCE; }

    /**
     * Returns an option with the given element.
     *
     * @param <T> the type of the element.
     * @param element the element in this option.
     * @return An option with the given element.
     * @throws NullPointerException if {@code element} is {@code null}.
     */
    public static <T> Option<T> some(T element) {
        return new Some<>(Objects.requireNonNull(element));
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
