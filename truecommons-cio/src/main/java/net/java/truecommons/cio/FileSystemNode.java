/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import net.java.truecommons.cio.attribute.*;

/**
 * Represents a node in a file system.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface FileSystemNode
extends Entry, WithTypes,
        WithMutableTimes,
        WithMutablePermissions, WithMutablePrincipals,
        WithMutableAttributes<Object, Object> {
}
