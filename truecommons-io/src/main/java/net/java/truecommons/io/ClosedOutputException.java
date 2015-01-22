/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Indicates that an output resource (stream, channel etc.) has been closed.
 *
 * @see    ClosedInputException
 * @author Christian Schlichtherle
 */
@ThreadSafe
public class ClosedOutputException extends ClosedStreamException {
    private static final long serialVersionUID = 4563928734723923649L;
}
