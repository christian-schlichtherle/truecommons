/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * A mutable view of the attributes of an underlying object.
 * This abstraction enables to access, copy and modify the attributes of an
 * underlying object with few bulk I/O operations in an implementation
 * independent way.
 * To facilitate interoperability, an implementation should <em>not</em> throw
 * an exception when it encounters an invalid attribute key or value.
 * This implies that writing attributes is not necessarily an atomic file
 * system operation.
 * <p>
 * Implementations should be safe for multithreading.
 * However, reading or writing attributes is not necessarily an isolated
 * file system operation so as to facilitate processing them in serialized
 * order.
 *
 * @param  <K> The type of the attribute keys.
 * @param  <V> The type of the attribute values.
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface MutableAttributeView<K, V> extends AttributeView<K, V> {

    /**
     * Write some attributes to the underlying object (optional operation).
     * In order to facilitate interoperability, implementations should meet the
     * following requirements:
     * <ul>
     * <li>Each attribute should get processed independently of any other.
     * <li>If an attribute with an invalid key is encountered
     *     then it should not get processed.
     * <li>If an attribute with an invalid value is encountered then
     *     either the invalid value should get mapped to a valid value
     *     or otherwise the attribute should not get processed.
     * </ul>
     * <p>
     * For example, when copying the attributes of a file system inode to a
     * ZIP entry, then on Unix the file system inode provides timestamps for
     * the last access time, the last modification time and the creation time
     * of the file system node where a timestamp is a 64 bit integer
     * representing the milliseconds since the Unix epoch, which is January
     * 1st, 1970.
     * However, a typical ZIP implementation may only support the last
     * modification time, where a timestamp is a 32 bit integer representing
     * the number of two-second periods since the MS-DOS epoch, which is
     * January 1st, 1980.
     * To facilitate interoperability, The ZIP implementation should then
     * silently igore the last access time and the creation time, adjust the
     * precision of the last modification time to a two-second period and if
     * required, adjust it to the minimum value for the MS-DOS epoch.
     *
     * @param  attributes a map with the keys and values of the attributes to
     *         write.
     * @throws IOException if writing the attributes <em>completely</em>
     *         failed for some I/O related reason.
     * @throws UnsupportedOperationException if and only if this attribute
     *         store is immutable.
     */
    void write(Map<? extends K, ? extends V> attributes) throws IOException;

    /**
     * Deletes all attributes from the underlying object (optional operation).
     * In order to facilitate interoperability, implementations should meet the
     * following requirements:
     * <ul>
     * <li>Each attribute should get processed independently of any other.
     * <li>If an attribute is not deletable then
     *     either the attribute value should get mapped to a default value
     *     or otherwise the attribute should not get processed.
     * </ul>
     *
     * @throws IOException if deleting all attributes <em>completely</em>
     *         failed for some I/O related reason.
     * @throws UnsupportedOperationException if and only if this attribute
     *         store is immutable.
     */
    void delete() throws IOException;

    /**
     * Deletes some attributes from the underlying object (optional operation).
     * In order to facilitate interoperability, implementations should meet the
     * following requirements:
     * <ul>
     * <li>Each attribute should get processed independently of any other.
     * <li>If an attribute with an invalid key is encountered
     *     then it should not get processed.
     * <li>If an attribute is not deletable then
     *     either the attribute value should get mapped to a default value
     *     or otherwise the attribute should not get processed.
     * </ul>
     *
     * @param  keys a set with the keys of the attributes to delete.
     * @throws IOException if deleting the attributes <em>completely</em>
     *         failed for some I/O related reason.
     * @throws UnsupportedOperationException if and only if this attribute
     *         store is immutable.
     */
    void delete(Set<? extends K> keys) throws IOException;
}
