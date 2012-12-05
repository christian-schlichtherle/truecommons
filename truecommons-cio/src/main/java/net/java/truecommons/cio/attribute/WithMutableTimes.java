/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with mutable access {@link #times}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithMutableTimes extends WithTimes {

    /**
     * Returns a mutable view of the access times of this object.
     * All access times are represented in milliseconds since the epoch.
     *
     * @return A mutable view of the access times of this object.
     */
    @Override
    MutableAttributeView<Access, Long> times();
}
