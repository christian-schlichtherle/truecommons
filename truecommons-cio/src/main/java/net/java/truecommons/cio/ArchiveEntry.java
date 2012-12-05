/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import net.java.truecommons.cio.attribute.*;

/**
 * An entry in an archive file.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface ArchiveEntry
extends Entry, WithName<EntryName>,
        WithMutableSizes, WithMutableTimes,
        WithMutablePermissions, WithMutablePrincipals,
        WithMutableAttributes<Object, Object> {
}
