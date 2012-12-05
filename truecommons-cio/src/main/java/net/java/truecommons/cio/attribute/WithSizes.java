/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with {@link #sizes}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithSizes {

    /**
     * Returns a view of the sizes of this object.
     * All sizes are represented in bytes.
     *
     * @return A view of the sizes of this object.
     */
    AttributeView<Size, Long> sizes();
}
