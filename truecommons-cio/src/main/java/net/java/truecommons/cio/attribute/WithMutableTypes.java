/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with mutable {@link #types}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithMutableTypes extends WithTypes {

    /**
     * Returns a mutable view of the types of this object.
     *
     * @return A mutable view of the types of this object.
     */
    @Override
    MutableAttributeView<Type, Boolean> types();
}
