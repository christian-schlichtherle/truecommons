/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import java.util.Formatter;
import java.util.ResourceBundle;

/**
 * Lazily resolves and formats localized messages for logging purposes.
 * 
 * @author Christian Schlichtherle
 */
class Message extends UniqueObject {
    private final ResourceBundle bundle;
    private final String key;
    private final Object[] args;

    Message(final ResourceBundle bundle, final String key, final Object... args) {
        this.bundle = bundle;
        this.key = key;
        this.args = args;
    }

    @Override
    public String toString() {
        return new Formatter().format(bundle.getString(key), args).toString();
    }
}
