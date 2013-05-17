/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.swing;

import javax.annotation.CheckForNull;

/**
 * Thrown to indicate that password authentication failed for some reason.
 *
 * @since  TrueCommons 2.2.2
 * @author Christian Schlichtherle
 */
final class AuthenticationException extends Exception {

    private static final long serialVersionUID = 0L;

    AuthenticationException(@CheckForNull String message) { super(message); }

    AuthenticationException(@CheckForNull Throwable cause) { super(cause); }

    AuthenticationException(
            @CheckForNull String message,
            @CheckForNull Throwable cause ) {
        super(message, cause);
    }
}
