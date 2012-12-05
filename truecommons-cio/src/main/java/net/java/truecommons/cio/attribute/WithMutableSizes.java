/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with mutable {@link #sizes}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithMutableSizes extends WithSizes {

    /**
     * Returns a mutable view of the sizes of this object.
     * All sizes are represented in bytes.
     *
     * @return A mutable view of the sizes of this object.
     */
    @Override
    MutableAttributeView<Size, Long> sizes();
}
