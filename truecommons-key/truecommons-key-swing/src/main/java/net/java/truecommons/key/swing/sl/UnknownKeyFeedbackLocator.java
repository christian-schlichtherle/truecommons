/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.swing.sl;

import net.java.truecommons.key.swing.feedback.Feedback;
import net.java.truecommons.key.swing.spi.FeedbackFactory;
import net.java.truecommons.key.swing.spi.UnknownKeyFeedbackDecorator;
import net.java.truecommons.services.Container;
import net.java.truecommons.services.Locator;

/**
 * A container of the singleton visual and/or audible feedback to the user
 * when prompting for an unknown key for the first time.
 * The feedback is created by using a {@link Locator} to search for advertised
 * implementations of the factory service specification class
 * {@link FeedbackFactory}
 * and the decorator service specification class
 * {@link UnknownKeyFeedbackDecorator}.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
public final class UnknownKeyFeedbackLocator implements Container<Feedback> {

    /** The singleton instance of this class. */
    public static final UnknownKeyFeedbackLocator
            SINGLETON = new UnknownKeyFeedbackLocator();

    private UnknownKeyFeedbackLocator() { }

    @Override
    public Feedback get() {
        return Lazy.feedback;
    }

    /** A static data utility class used for lazy initialization. */
    private static final class Lazy {
        static final Feedback feedback =
                new Locator(UnknownKeyFeedbackLocator.class)
                .factory(FeedbackFactory.class, UnknownKeyFeedbackDecorator.class)
                .get();
    }
}
