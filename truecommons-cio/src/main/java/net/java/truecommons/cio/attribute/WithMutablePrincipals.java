/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.nio.file.attribute.UserPrincipal;

/**
 * An object with mutable user {@link #principals}.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface WithMutablePrincipals extends WithPrincipals {

    /**
     * Returns a mutable view of the user principals of this object.
     *
     * @return A mutable view of the user principals of this object.
     */
    @Override
    MutableAttributeView<Entity, UserPrincipal> principals();
}
