/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.truecommons.shed;

import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

/**
 * Indicates a condition which requires non-local control flow.
 * Note that this class is an {@code Error} rather than a
 * {@link RuntimeException} in order to prevent it from being accidentally
 * catched.
 * 
 * @author Christian Schlichtherle
 */
@Immutable
@SuppressWarnings("serial") // serializing control flow exceptions is nonsense!
public class ControlFlowException extends Error {

    private static final String TRACEABLE_PROPERTY_KEY =
            ControlFlowException.class.getName() + ".traceable";
    private static final boolean TRACEABLE =
            Boolean.getBoolean(TRACEABLE_PROPERTY_KEY);

    public ControlFlowException() { this(null); }

    public ControlFlowException(final @CheckForNull Throwable cause) {
        this(null, false);
    }

    public ControlFlowException(boolean enableSuppression) {
        this(null, enableSuppression);
    }

    public ControlFlowException(final @CheckForNull Throwable cause, boolean enableSuppression) {
        super(null == cause ? null : cause.toString(), cause, enableSuppression, TRACEABLE);
    }

    /**
     * Returns {@code true} if and only if a control flow exception should have
     * a full stack trace instead of an empty stack trace.
     * If and only if the system property with the key string
     * {@code net.truevfs.kernel.spec.util.ControlFlowException.traceable}
     * is set to {@code true} (whereby case is ignored), then instances of this
     * class will have a regular stack trace, otherwise their stack trace will
     * be empty.
     * Note that this should be set to {@code true} for debugging purposes only.
     * 
     * @return {@code true} if and only if a control flow exception should have
     *         a full stack trace instead of an empty stack trace.
     */
    public static boolean isTraceable() {
        return TRACEABLE;
    }
}
