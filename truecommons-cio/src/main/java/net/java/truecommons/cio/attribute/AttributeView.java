/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * A view of the attributes of an underlying object.
 * This abstraction enables to access and copy the attributes of an
 * underlying object with few bulk I/O operations in an implementation
 * independent way.
 * To facilitate interoperability, an implementation should <em>not</em> throw
 * an exception when it encounters an invalid attribute key.
 * <p>
 * Implementations should be safe for multithreading.
 * However, reading attributes is not necessarily an isolated
 * file system operation so as to facilitate processing them in serialized
 * order.
 *
 * @param  <K> The type of the attribute keys.
 * @param  <V> The type of the attribute values.
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface AttributeView<K, V> {

    /**
     * Reads all attributes from the underlying object.
     * <p>
     * The returned map is modifiable and can get passed to
     * {@link AttributeStore#write} so as to facilitate changing the attributes
     * of the underlying object.
     *
     * @return A new map of all attributes read from the underlying object.
     * @throws IOException if reading all attributes <em>completely</em>
     *         failed for some I/O related reason.
     */
    Map<K, V> read() throws IOException;

    /**
     * Reads some attributes from the underlying object.
     * In order to facilitate interoperability, implementations should meet the
     * following requirements:
     * <ul>
     * <li>If an attribute with an invalid key is encountered
     *     then it should not get processed.
     * </ul>
     * <p>
     * <p>
     * The returned map is modifiable and can get passed to
     * {@link AttributeStore#write} so as to facilitate changing the attributes
     * of the underlying object.
     *
     * @param  keys a set with the keys of the attributes to read.
     * @return A new map of the attributes read from the underlying object.
     * @throws IOException if reading the attributes <em>completely</em>
     *         failed for some I/O related reason.
     */
    Map<K, V> read(Set<? extends K> keys) throws IOException;
}
