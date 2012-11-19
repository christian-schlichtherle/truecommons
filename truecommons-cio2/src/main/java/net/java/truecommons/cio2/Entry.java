/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio2;

import java.util.Map;
import java.util.Set;
import net.java.truecommons.shed.Option;

/**
 * Represents an entry in a container.
 * <p>
 * Some constants of this interface are unmodifiable sets of enums.
 * These are convenient to use for loops like this:
 * <pre>{@code
 * for (Type type : ALL_TYPES)
 *     ...;
 * }</pre>
 *
 * @see    MutableEntry
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
@SuppressWarnings("PublicInnerClass")
public interface Entry {

    /**
     * Returns a set with the types of this entry.
     * <p>
     * Modifying the returned set may not be supported.
     * If it is, then doing so has no effect on this entry.
     *
     * @return A set with the types of this entry.
     */
    Set<Type> types();

    /**
     * Updates the types of this entry.
     *
     * @param  values a set with the types for this entry.
     * @param  strategy the strategy for updating this entry.
     * @return See {@link UpdateOption}.
     * @throws IllegalArgumentException See {@link UpdateOption}.
     */
    void types(Set<Type> types)
    throws UnsupportedOperationException, IllegalArgumentException;

    /**
     * Returns an option with the name of this entry.
     *
     * @return An option with the name of this entry.
     */
    Option<EntryName> name();

    /**
     * Updates the name of this entry.
     *
     * @param  name an option with the name for this entry.
     */
    void name(Option<EntryName> name)
    throws UnsupportedOperationException, IllegalArgumentException;

    /**
     * Returns a view of the types of this entry.
     *
     * @return A view of the types of this entry.
     */
    Attribute.View<Attribute.Type, Boolean> types();

    /**
     * Returns a view of the sizes of this entry.
     * All sizes are represented in bytes.
     *
     * @return A view of the sizes of this entry.
     */
    Attribute.View<Attribute.Size, Long> sizes();

    /**
     * Returns a view of the access times of this entry.
     * All access times are represented in milliseconds since the epoch.
     *
     * @return A view of the access times of this entry.
     */
    Attribute.View<Attribute.Access, Long> times();

    /**
     * Returns a map with the permissions of this entry.
     * <p>
     * Modifying the returned map may not be supported.
     * If it is, then doing so has no effect on this entry.
     *
     * @return A map with the permissions of this entry.
     */
    Map<Entity, Map<Access, Boolean>> permissions(Set<Entity> keys);

    /**
     * Updates the permission of this entry.
     *
     * @param  values a map with the permissions for this entry.
     * @param  strategy the strategy for updating this entry.
     * @return See {@link UpdateOption}.
     * @throws IllegalArgumentException See {@link UpdateOption}.
     */
    void permissions(Map<Entity, Map<Access, Boolean>> values)
    throws UnsupportedOperationException, IllegalArgumentException;
}
