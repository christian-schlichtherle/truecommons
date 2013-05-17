/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import net.java.truecommons.cio.attribute.*;

/**
 * An object which typically resides in a container or pool.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface Entry
extends WithSizes, WithTimes,
        WithPermissions, WithPrincipals,
        WithAttributes<Object, Object> { }
