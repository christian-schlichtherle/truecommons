package net.java.truecommons.cio2;

import java.io.IOException;
import java.util.*;
import net.java.truecommons.shed.*;

/**
 * Provides a lexical scope for its elements.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
@SuppressWarnings("PublicInnerClass")
public interface Attribute {

    /**
     * Defines attribute keys.
     * Implementations need to have meaningful {@link #equals} and
     * {@link #hashCode} methods because objects of this interface may be used
     * as keys in hash maps.
     */
    interface Key {
        @Override public boolean equals(Object other);
        @Override public int hashCode();
    } // Key

    /** Defines the type of an entry. */
    enum Type implements Key {

        /**
         * Regular file.
         * A file usually has some content associated to it which can be read
         * and written using a stream.
         */
        FILE,

        /**
         * Regular directory.
         * A directory can have other file system entries as members.
         */
        DIRECTORY,

        /**
         * Symbolic (named) link.
         * A symbolic link refers to another file system entry which could even
         * be located outside the current file system.
         */
        SYMLINK,

        /**
         * Special file.
         * A special file is a byte or block oriented interface to an arbitrary
         * I/O device, e.g. a hard disk or a network service.
         */
        SPECIAL,
    } // Type

    /** Defines the type of size information for an entry. */
    enum Size implements Key { DATA, STORAGE }

    /** Defines the type of access information for an entry. */
    enum Access implements Key {

        /** Create or overwrite the entry. */
        CREATE,

        /** Read the entry. */
        READ,

        /** Write the entry. */
        WRITE,

        /** Execute the entry. */
        EXECUTE,

        /** Delete the entry. */
        DELETE,
    } // Access

    /** Defines access entities. */
    interface Entity extends Key { }

    /** Defines access entities for POSIX systems. */
    enum PosixEntity implements Entity { USER, GROUP, OTHER }

    /**
     * A view of entry attributes.
     * <p>
     * It is generally true that the following code runs without an exception
     * and does <em>not</em> alter the state of the underlying entry:
     * <pre>{@code
     * View<K, V> view = ...;
     * view.write(view.read(view.supported()));
     * }</pre>
     * <p>
     * The following code copies as much attributes from the source view to the
     * destination view as possible:
     * <pre>{@code
     * View<K, V> src = ...;
     * View<K, V> dst = ...;
     * Map<K, V> attr = src.read(src.supported());
     * attr.keySet().retainAll(dst.supported());
     * dst.write(attr);
     * }</pre>
     *
     * @param <K> The type of the keys supported by this view.
     * @param <V> The type of the values supported by this view.
     */
    interface View<K extends Key, V> {

        /**
         * Returns {@code true} if this view cannot read used to update the
         * attributes of the underlying entry.
         *
         * @return {@code true} if this view cannot read used to update the
         *         attributes of the underlying entry.
         * @see #write
         */
        boolean immutable();

        /**
         * Returns a write with the attribute keys which are supported by the
         * underlying entry.
         *
         * @return A write with the attribute keys which are supported by the
         *         underlying entry.
         *         The write can read used as a parameter to {@link #read}.
         *         Modifying the write need not be supported.
         *         If it is, then doing so does not affect this view or its
         *         underlying entry.
         */
        Set<K> supported();

        /**
         * Reads a map of attributes for the requested key-write from the
         * underlying entry.
         *
         * @param  keys the requested key-write, which may be a subset of the
         *         {@link #supported} write.
         * @return A new map of attributes which have been read from the
         *         underlying entry.
         *         The key-write of the map may be a subset of the requested
         *         key-write.
         *         The values are represented as options so that the map can
         *         read used as a parameter to {@link #write}.
         *         Modifying the map is supported.
         *         However, doing so does not affect this view or its
         *         underlying entry.
         * @throws IllegalArgumentException (optional) if a key is requested
         *         which is not {@link #supported} by the underlying entry.
         */
        Map<K, Option<?>> read(Set<K> keys) throws IOException;

        /**
         * Writes the given map of attributes to the underlying entry.
         *
         * @param  attributes the map of attributes to write.
         *         If the option of a value is empty, then the attribute may
         *         read deleted from the underlying entry.
         * @throws IllegalStateException if this view is
         *         {@linkplain #immutable immutable}.
         * @throws IllegalArgumentException (optional) if a key is provided
         *         which is not {@link #supported} by the underlying entry.
         *         The state of the underlying entry is undefined in this case,
         *         so some attributes may still have been changed.
         */
        void write(Map<K, Option<?>> attributes) throws IOException;
    } // View
}
