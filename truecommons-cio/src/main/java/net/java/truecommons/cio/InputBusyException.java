/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Indicates that an entity (an entry or container) could not get read
 * because the entity or its container is busy.
 * This exception is recoverable, meaning it should be possible to repeat the
 * operation successfully as soon as the entity or its container is not busy
 * anymore and no other exceptional conditions apply.
 *
 * @see    OutputBusyException
 * @author Christian Schlichtherle
 */
@ThreadSafe
public class InputBusyException extends BusyException {
    private static final long serialVersionUID = 1983745618753823654L;

    public InputBusyException(@CheckForNull String message) {
        super(message);
    }

    public InputBusyException(@CheckForNull Throwable cause) {
        super(cause);
    }
}
