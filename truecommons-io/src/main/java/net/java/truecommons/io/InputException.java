/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

/**
 * Thrown if an error happened on the input side rather than the output side
 * when copying an {@link InputStream} to an {@link OutputStream}.
 * 
 * @see     Streams#cat(InputStream, OutputStream)
 * @author  Christian Schlichtherle
 */
@Immutable
public class InputException extends IOException {
    private static final long serialVersionUID = 1287654325546872424L;

    /**
     * Constructs a new {@code InputException}.
     *
     * @param cause the nullable cause for this exception.
     */
    public InputException(@CheckForNull IOException cause) {
        super(cause);
    }

    @Override
    public IOException getCause() {
        return (IOException) super.getCause();
    }
}