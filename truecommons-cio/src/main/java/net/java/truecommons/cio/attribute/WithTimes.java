/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * An object with access {@link #times}.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface WithTimes {

    /**
     * Returns a view of the access times of this object.
     * All access times are represented in milliseconds since the epoch.
     *
     * @return A view of the access times of this object.
     */
    AttributeView<Access, Long> times();
}
