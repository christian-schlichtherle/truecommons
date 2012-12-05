/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * Defines the type of an entry.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public enum Type {

    /**
     * Regular file.
     * A file usually has some content associated to it which can be read
     * and written using a stream.
     */
    FILE,

    /**
     * Regular directory.
     * A directory can have other file system entries as members.
     */
    DIRECTORY,

    /**
     * Symbolic (named) link.
     * A symbolic link refers to another file system entry which could even
     * be located outside the current file system.
     */
    SYMLINK,

    /**
     * Special file.
     * A special file is a byte or block oriented interface to an arbitrary
     * I/O device, e.g. a hard disk or a network service.
     */
    SPECIAL
}
