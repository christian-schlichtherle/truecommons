/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.util.Iterator;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Christian Schlichtherle
 */
public class OptionSomeTest {

    private final String string = "Hello world!";
    private final Option<String> option = Option.apply(string);

    @Test
    public void testImmutableIteratorWithOneElement() {
        final Iterator<String> it = option.iterator();
        assertTrue(it.hasNext());
        assertThat(it.next(), is(sameInstance(string)));
        try {
            it.remove();
            fail();
        } catch (final UnsupportedOperationException ex) {
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void testSizeIsOne() {
        assertThat(option.size(), is(1));
    }

    @Test
    public void testIsNotEmpty() {
        assertFalse(option.isEmpty());
    }

    @Test
    public void testGet() {
        assertSame(string, option.get());
    }

    @Test
    public void testGetOrElse() {
        assertSame(string, option.getOrElse("foo"));
    }

    @Test
    public void testOrNull() {
        assertSame(string, option.orNull());
    }

    @Test
    @SuppressWarnings("RedundantStringConstructorCall")
    public void testEquals() {
        final Option<String> o1 = option;
        final Option<String> o2 = Option.apply(new String(string));
        assertEquals(o1, o2);
        assertEquals(o2, o1);
        assertFalse(o1.equals(Option.apply(null)));
        assertFalse(Option.apply(null).equals(o1));
    }
}
