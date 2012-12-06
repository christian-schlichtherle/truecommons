/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import net.java.truecommons.cio.Entry;

/**
 * An object with {@link #types}.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface WithTypes extends Entry {

    /**
     * Returns a view of the types of this object.
     *
     * @return A view of the types of this object.
     */
    AttributeView<Type, Boolean> types();
}
