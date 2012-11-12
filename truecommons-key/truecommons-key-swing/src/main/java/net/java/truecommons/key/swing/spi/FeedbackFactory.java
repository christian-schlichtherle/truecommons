/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.swing.spi;

import javax.annotation.concurrent.Immutable;
import net.java.truecommons.annotations.ServiceImplementation;
import net.java.truecommons.annotations.ServiceSpecification;
import net.java.truecommons.key.swing.feedback.BeepFeedback;
import net.java.truecommons.key.swing.feedback.Feedback;
import net.java.truecommons.services.LocatableFactory;

/**
 * A service for creating visual and/or audible feedback to the user
 * when prompting for unknown or invalid keys.
 * <p>
 * Note that you can't subclass this class for customization.
 * Instead, you should implement a custom {@link UnknownKeyFeedbackDecorator}
 * or {@link InvalidKeyFeedbackDecorator} and advertise them on the class path.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@Immutable
@ServiceSpecification
@ServiceImplementation
public class FeedbackFactory
extends LocatableFactory<Feedback> {

    /**
     * Returns a new {@link BeepFeedback}.
     *
     * @return A new {@link BeepFeedback}.
     */
    @Override
    public Feedback get() {
        return new BeepFeedback();
    }
}
