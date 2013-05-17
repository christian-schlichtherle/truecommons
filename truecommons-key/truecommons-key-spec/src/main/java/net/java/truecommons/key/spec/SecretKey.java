/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.spec;

import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import net.java.truecommons.shed.ImplementationsShouldExtend;

/**
 * A key with properties for secret key management.
 * <p>
 * Implementations do not need to be safe for multi-threading.
 *
 * @param  <K> the type of this secret key.
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@ImplementationsShouldExtend(AbstractSecretKey.class)
public interface SecretKey<K extends SecretKey<K>> extends Key<K> {

    /** Returns a protective copy of the secret data. */
    @CheckForNull ByteBuffer getSecret();

    /**
     * Clears the current secret data and sets it to a protective copy of the
     * given secret data.
     *
     * @param secret the secret data to copy and set.
     */
    void setSecret(@CheckForNull ByteBuffer secret);
}
