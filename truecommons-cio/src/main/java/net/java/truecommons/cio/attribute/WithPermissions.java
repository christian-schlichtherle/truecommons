/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.nio.file.attribute.AclEntry;
import java.util.List;

/**
 * An object with {@link #permissions}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithPermissions {

    /**
     * Returns a view of the permissions of this object.
     *
     * @return A view of the permissions of this object.
     */
    AttributeView<Entity, List<AclEntry>> permissions();
}
