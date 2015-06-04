/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

/**
 * @author Christian Schlichtherle
 */
public abstract class OptionTestSuite {

    private static final Logger logger =
            LoggerFactory.getLogger(OptionTestSuite.class);

    private Option<String> optionalString;

    abstract @Nullable String string();

    final Option<String> optionalString() {
        return null != optionalString
                ? optionalString
                : (optionalString = Option.apply(string()));
    }

    @Test
    public void testMostIdiomaticUseCase() {
        for (String s : optionalString())
            assertSame(string(), s);
    }

    @Test
    public void testLessIdiomaticUseCase() {
        if (!optionalString().isEmpty())
            assertSame(string(), optionalString().get());
    }

    @Test
    public void testObjectSerialization() throws Exception {
        assertOptionEquals(optionalString(), cloneViaObjectSerialization(optionalString()));
    }

    @Test
    public void testXmlSerialization() throws Exception {
        assertOptionEquals(optionalString(), cloneViaXmlSerialization(optionalString()));
    }

    void assertOptionEquals(Option<String> expected, Option<String> actual) {
        assertEquals(expected, actual);
    }

    @SuppressWarnings("unchecked")
    private static <T> T cloneViaObjectSerialization(final T original)
    throws Exception {
        final byte[] serialized;
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream _ = new ObjectOutputStream(bos)) {
                _.writeObject(original);
            }
            bos.flush(); // redundant
            serialized = bos.toByteArray();
        }

        logger.trace("Serialized object to {} bytes.", serialized.length);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serialized))) {
            return (T) ois.readObject();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cloneViaXmlSerialization(final T original)
    throws Exception {
        final byte[] serialized;
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (XMLEncoder encoder = new XMLEncoder(bos)) {
                encoder.writeObject(original);
            }
            bos.flush(); // redundant
            serialized = bos.toByteArray();
        }

        logger.debug("Serialized object to {} bytes.", serialized.length);
        logger.debug("Serialized form:\n{}",
                new String(serialized, StandardCharsets.UTF_8));

        try (XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(serialized))) {
            return (T) decoder.readObject();
        }
    }

    @Test
    public void testHashCode() {
        final Option<String> o1 = optionalString();
        final Option<String> o2 = Option.apply(string());
        assertEquals(o1.hashCode(), o2.hashCode());
    }

    @Test
    public abstract void testEquals();

    @Test
    public abstract void testIterator();

    @Test
    public abstract void testSize();

    @Test
    public abstract void testIsEmpty();

    @Test
    public abstract void testGet();

    @Test
    public abstract void testGetOrElse();

    @Test
    public abstract void testOrNull();

    @Test
    public abstract void testOrElse();
}
