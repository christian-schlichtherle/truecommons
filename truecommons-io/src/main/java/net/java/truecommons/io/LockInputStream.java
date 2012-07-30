/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.io;

import edu.umd.cs.findbugs.annotations.DischargesObligation;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import javax.annotation.WillCloseWhenClosed;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

/**
 * A decorator which protects all access to its input stream
 * via a {@link Lock}.
 *
 * @see    LockOutputStream
 * @author Christian Schlichtherle
 */
@ThreadSafe
public class LockInputStream extends DecoratingInputStream {

    /** The lock on which this object synchronizes. */
    private final Lock lock;

    protected LockInputStream(final Lock lock) {
        this.lock = Objects.requireNonNull(lock);
    }

    public LockInputStream(
            final Lock lock,
            final @WillCloseWhenClosed InputStream in) {
        this(lock);
        this.in = Objects.requireNonNull(in);
    }

    @Override
    @GuardedBy("lock")
    public int read() throws IOException {
        lock.lock();
        try {
            return in.read();
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public int read(byte[] b, int off, int len) throws IOException {
        lock.lock();
        try {
            return in.read(b, off, len);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public long skip(long n) throws IOException {
        lock.lock();
        try {
            return in.skip(n);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public int available() throws IOException {
        lock.lock();
        try {
            return in.available();
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public void mark(int readlimit) {
        lock.lock();
        try {
            in.mark(readlimit);
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public void reset() throws IOException {
        lock.lock();
        try {
            in.reset();
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    public boolean markSupported() {
        lock.lock();
        try {
            return in.markSupported();
        } finally {
            lock.unlock();
        }
    }

    @Override
    @GuardedBy("lock")
    @DischargesObligation
    public void close() throws IOException {
        lock.lock();
        try {
            in.close();
        } finally {
            lock.unlock();
        }
    }
}