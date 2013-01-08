/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with mutable custom {@link #attributes}.
 *
 * @param  <K> The type of the custom attribute keys.
 * @param  <V> The type of the custom attribute values.
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface WithMutableAttributes<K, V> extends WithAttributes<K, V> {

    /**
     * Returns a mutable view of the custom attributes of this object.
     * This may be used to store user defined or application specific
     * attributes.
     *
     * @return A mutable view of the custom attributes of this object.
     */
    @Override
    MutableAttributeView<K, V> attributes();
}
