/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.nio.file.attribute.AclEntry;
import java.util.List;

/**
 * An object with mutable {@link #permissions}.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface WithMutablePermissions extends WithPermissions {

    /**
     * Returns a mutable view of the permissions of this object.
     *
     * @return A mutable view of the permissions of this object.
     */
    @Override
    MutableAttributeView<Entity, List<AclEntry>> permissions();
}
