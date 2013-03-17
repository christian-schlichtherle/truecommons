/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.swing;

import java.net.URI;
import javax.annotation.CheckForNull;
import net.java.truecommons.key.spec.prompting.PromptingPbeParameters;
import net.java.truecommons.key.swing.feedback.Feedback;
import net.java.truecommons.key.swing.util.EnhancedPanel;
import net.java.truecommons.key.swing.util.PanelEvent;
import net.java.truecommons.key.swing.util.PanelListener;

/**
 * Abstract panel for prompting for authentication keys.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
abstract class KeyPanel extends EnhancedPanel {

    private static final long serialVersionUID = 0L;

    private @CheckForNull Feedback feedback;

    KeyPanel() { super.addPanelListener(new KeyPanelListener()); }

    /**
     * Returns the feedback to run when this panel is shown in its ancestor
     * window.
     */
    public @CheckForNull Feedback getFeedback() { return feedback; }

    /**
     * Sets the feedback to run when this panel is shown in its ancestor
     * window.
     */
    public void setFeedback(final @CheckForNull Feedback feedback) {
        this.feedback = feedback;
    }

    private void runFeedback() {
        final Feedback feedback = getFeedback();
        if (null != feedback) feedback.run(this);
    }

    /**
     * Getter for property {@code resource}.
     *
     * @return Value of property {@code resource}.
     */
    public abstract URI getResource();

    /**
     * Setter for property {@code resource}.
     *
     * @param resource New value of property {@code resource}.
     */
    public abstract void setResource(final URI resource);

    /**
     * Getter for property {@code error}.
     */
    public abstract @CheckForNull String getError();

    /**
     * Setter for property error.
     *
     * @param error New value of property error.
     */
    public abstract void setError(@CheckForNull String error);

    final boolean updateParam(final PromptingPbeParameters<?, ?> param) {
        try {
            updateParamChecked(param);
            return true;
        } catch (final AuthenticationException ex) {
            setError(ex.getLocalizedMessage());
            return false;
        }
    }

    abstract void updateParamChecked(PromptingPbeParameters<?, ?> param)
    throws AuthenticationException;

    private static class KeyPanelListener implements PanelListener {
        @Override
        public void ancestorWindowShown(final PanelEvent evt) {
            ((KeyPanel) evt.getSource()).runFeedback();
        }

        @Override public void ancestorWindowHidden(PanelEvent evt) { }
    }
}
