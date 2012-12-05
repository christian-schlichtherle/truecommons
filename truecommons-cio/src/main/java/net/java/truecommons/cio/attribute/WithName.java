/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import javax.lang.model.element.Name;

/**
 * An object with a {@link #name}.
 *
 * @param  <N> The type of the object name.
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithName<N extends Name> {

    /**
     * Returns the name of this object.
     *
     * @return The name of this object.
     */
    N name();
}
