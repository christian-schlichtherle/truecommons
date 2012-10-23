/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import java.io.FileNotFoundException;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Indicates that an entity (an entry or container) could not get
 * read or written
 * because the entity or its container is busy.
 * This exception is recoverable, meaning it should be possible to repeat the
 * operation successfully as soon as the entity or container is not busy
 * anymore and no other exceptional conditions apply.
 *
 * @author Christian Schlichtherle
 */
@ThreadSafe
public class BusyException extends FileNotFoundException {
    private static final long serialVersionUID = 2056108562576389242L;

    public BusyException(@CheckForNull String message) {
        super(message);
    }

    public BusyException(@CheckForNull Throwable cause) {
        super(null == cause ? null : cause.toString());
        super.initCause(cause);
    }
}
