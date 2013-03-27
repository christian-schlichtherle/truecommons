/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Indicates that an entity (an entry or container) could not get written
 * because the entity or its container is busy.
 * This exception is recoverable, meaning it should be possible to repeat the
 * operation successfully as soon as the entity or its container is not busy
 * anymore and no other exceptional conditions apply.
 *
 * @see    InputBusyException
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
@ThreadSafe
public class OutputBusyException extends BusyException {
    private static final long serialVersionUID = 962318648273654198L;

    public OutputBusyException(@CheckForNull String message) {
        super(message);
    }

    public OutputBusyException(@CheckForNull Throwable cause) {
        super(cause);
    }
}
