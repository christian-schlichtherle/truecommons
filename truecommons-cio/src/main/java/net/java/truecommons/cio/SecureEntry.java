/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import net.java.truecommons.cio.attribute.*;

/**
 * An entry with permissions and principals.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public interface SecureEntry extends Entry, WithPermissions, WithPrincipals { }
