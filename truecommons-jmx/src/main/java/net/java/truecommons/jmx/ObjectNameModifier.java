/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.jmx;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

/**
 * An encoder and decoder for JMX {@linkplain ObjectName object names}.
 * <p>
 * Implementations need to be safe for multi-threading.
 *
 * @since  TrueCommons 2.3
 * @author Christian Schlichtherle
 */
public interface ObjectNameModifier {

    /**
     * Encodes the given object name.
     *
     * @param name the object name to encode.
     * @return The encoded object name.
     */
    @Nullable ObjectName apply(@CheckForNull ObjectName name);

    /**
     * Decodes the given object name.
     *
     * @param name the object name to decode.
     * @return The decoded object name.
     */
    @Nullable ObjectName unapply(@CheckForNull ObjectName name);

    /**
     * Encodes the object name in the given object instance.
     *
     * @param instance the object instance with the object name to encode.
     * @return The transformed object instance.
     */
    @Nullable ObjectInstance apply(@CheckForNull ObjectInstance instance);

    /**
     * Decodes the object name in the given object instance.
     *
     * @param instance the object instance with the object name to decode.
     * @return The transformed object instance.
     */
    @Nullable ObjectInstance unapply(@CheckForNull ObjectInstance instance);
}
