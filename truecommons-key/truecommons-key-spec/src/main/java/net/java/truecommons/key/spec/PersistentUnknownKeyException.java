/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec;

import javax.annotation.CheckForNull;

/**
 * Thrown to indicate that the retrieval of the key to (over)write or read a
 * protected resource has failed and that this exception is cacheable.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
public class PersistentUnknownKeyException extends UnknownKeyException {

    private static final long serialVersionUID = 2463586348235337265L;

    public PersistentUnknownKeyException() { }

    public PersistentUnknownKeyException(@CheckForNull String msg) {
        super(msg);
    }

    public PersistentUnknownKeyException(@CheckForNull Throwable cause) {
        super(cause);
    }

    public PersistentUnknownKeyException(@CheckForNull String msg, @CheckForNull Throwable cause) {
        super(msg, cause);
    }
}
