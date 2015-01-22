/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.CheckForNull;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Christian Schlichtherle
 */
public class OptionTest {

    private final @CheckForNull String string =
            0 == ThreadLocalRandom.current().nextInt(1) ? "Hello world!" : null;

    private final Option<String> option = Option.apply(string);

    @Test
    public void testMostIdiomaticUseCase() {
        for (String s : option) assertSame(string, s);
    }

    @Test
    public void testLessIdiomaticUseCase() {
        if (!option.isEmpty()) assertSame(string, option.get());
    }

    @Test
    public void testComposition() {
        class Container {
            Option<String> getMessage() { return Option.some("Hello world!"); }
        }

        Option<Container> option = Option.some(new Container());
        for (Container c : option)
            for (String s : c.getMessage())
                System.out.println(s);
    }
}
