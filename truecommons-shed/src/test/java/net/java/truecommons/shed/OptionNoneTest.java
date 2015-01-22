/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.util.Iterator;
import java.util.NoSuchElementException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Christian Schlichtherle
 */
public class OptionNoneTest {

    private final String string = null;
    private final Option<String> option = Option.apply(string);

    @Test
    public void testEmptyIterator() {
        final Iterator<String> it = option.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void testSizeIsZero() {
        assertThat(option.size(), is(0));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(option.isEmpty());
    }

    @Test(expected=NoSuchElementException.class)
    public void testGet() {
        option.get();
    }

    @Test
    public void testGetOrElse() {
        assertSame(option.getOrElse("foo"), "foo");
    }

    @Test
    public void testOrNull() {
        assertNull(option.orNull());
    }

    @Test
    @SuppressWarnings("RedundantStringConstructorCall")
    public void testEquals() {
        final Option<String> o1 = option;
        final Option<String> o2 = Option.apply(null);
        assertEquals(o1, o2);
        assertEquals(o2, o1);
        assertFalse(o1.equals(Option.apply("foo")));
        assertFalse(Option.apply("foo").equals(o1));
    }
}
