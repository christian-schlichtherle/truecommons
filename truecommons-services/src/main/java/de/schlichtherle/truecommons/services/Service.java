/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

/**
 * Defines common properties of locatable services.
 * 
 * @see    Locator
 * @author Christian Schlichtherle
 */
public abstract class Service extends UniqueObject {

    /**
     * Returns a priority to help {@link Locator}s to prioritize this service.
     * <p>
     * The implementation in the abstract class {@link Service} returns zero.
     * 
     * @return A priority to help {@link Locator}s to prioritize this service.
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Returns a string representation of this object for debugging and logging
     * purposes.
     */
    @Override
    public String toString() {
        return String.format("%s[priority=%d]",
                getClass().getName(),
                getPriority());
    }
}
