/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import javax.lang.model.element.Name;

/**
 * An object with a mutable {@link #name}.
 *
 * @param  <N> The type of the object name.
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithMutableName<N extends Name> extends WithName<N> {

    /**
     * Updates the name of this object.
     *
     * @param  name the new name of this object.
     * @throws IllegalArgumentException if the object name is invalid.
     */
    void name(N name) throws IllegalArgumentException;
}
