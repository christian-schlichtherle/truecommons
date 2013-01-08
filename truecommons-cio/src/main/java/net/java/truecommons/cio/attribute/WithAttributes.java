/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with generic {@link #attributes}.
 *
 * @param  <K> The type of the custom attribute keys.
 * @param  <V> The type of the custom attribute values.
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface WithAttributes<K, V> {

    /**
     * Returns a view of the generic attributes of this object.
     *
     * @return A view of the generic attributes of this object.
     */
    AttributeView<K, V> attributes();
}
