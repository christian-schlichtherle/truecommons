/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.key.swing;

import java.util.Objects;
import java.util.ResourceBundle;
import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import net.java.truecommons3.key.spec.KeyStrength;
import net.java.truecommons3.key.swing.util.EnhancedPanel;

/**
 * A panel which allows the user to select the key strength for a cipher.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@NotThreadSafe
@SuppressWarnings({ "UseOfObsoleteCollectionType", "unchecked", "rawtypes" })
final class KeyStrengthPanel<S extends KeyStrength> extends EnhancedPanel {
    private static final long serialVersionUID = 5629581723148235643L;

    private static final ResourceBundle resources
            = ResourceBundle.getBundle(KeyStrengthPanel.class.getName());

    private final S[] availableKeyStrengths;

    /**
     * Constructs a new panel using a protective copy of the given array
     * of available key strengths.
     */
    KeyStrengthPanel(final S[] availableKeyStrenghts) {
        this.availableKeyStrengths = availableKeyStrenghts.clone();
        initComponents();
    }

    private ComboBoxModel<S> newModel() {
        return new DefaultComboBoxModel<>(availableKeyStrengths);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel keyStrengthLong = new javax.swing.JLabel();
        final javax.swing.JLabel keyStrengthShort = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        keyStrength.setModel(newModel());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        add(keyStrength, gridBagConstraints);

        keyStrengthLong.setLabelFor(keyStrength);
        keyStrengthLong.setText(resources.getString("prompt")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(keyStrengthLong, gridBagConstraints);

        keyStrengthShort.setLabelFor(keyStrength);
        keyStrengthShort.setText(resources.getString("keyStrength")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 5);
        add(keyStrengthShort, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Returns the value of the property {@code keyStrength}.
     *
     * @return The value of the property {@code keyStrength}.
     */
    public S getKeyStrength() {
        return (S) keyStrength.getSelectedItem();
    }

    /**
     * Sets the value of the property {@code keyStrength}.
     *
     * @param keyStrength the new value of the property {@code keyStrength}.
     */
    public void setKeyStrength(final S keyStrength) {
        Objects.requireNonNull(keyStrength);
        this.keyStrength.setSelectedItem(keyStrength);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JComboBox keyStrength = new javax.swing.JComboBox();
    // End of variables declaration//GEN-END:variables
}
