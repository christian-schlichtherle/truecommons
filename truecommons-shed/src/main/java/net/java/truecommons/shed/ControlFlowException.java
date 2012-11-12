/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.java.truecommons.shed;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.Immutable;

/**
 * Indicates a condition which requires non-local control flow.
 * A control flow exception needs to get catched and resolved by the same
 * thread which has thrown it, ideally within the same sub-system so that it
 * doesn't accidentally "surprise" any foreign code.
 * This implies that a control flow exception <em>must not</em> get passed on
 * to other threads or even other JVMs, so serialization is deliberately not
 * supported.
 * <p>
 * Note that although the name of this class suggests that it's an exception
 * type, it deliberately subclasses {@code Error} in order to prevent its
 * instances from being accidentally catched as an ordinary {@link Exception}.
 * This is done to particularly protect against code which catches and
 * erroneously suppresses any {@link RuntimeException} instead of propagating
 * them to the caller.
 * <p>
 * As a general recommendation, if you need to ensure resource cleanup
 * regardless of any {@code Throwable} then please place the cleanup code in a
 * finally-block like this:
 * <pre>{@code
 * Resource resource = new Resource();
 * try {
 *     // May terminate with any Throwable, including ControlFlowException.
 *     resource.use();
 * } finally {
 *     resource.cleanup(); // finally-block ensures cleanup
 * }
 * }</pre>
 *
 * @author Christian Schlichtherle
 */
@Immutable
@SuppressWarnings("serial") // serializing control flow exceptions is nonsense!
@SuppressFBWarnings("NM_CLASS_NOT_EXCEPTION")
public class ControlFlowException extends Error {

    private static final String TRACEABLE_PROPERTY_KEY =
            ControlFlowException.class.getName() + ".traceable";
    private static final boolean TRACEABLE =
            Boolean.getBoolean(TRACEABLE_PROPERTY_KEY);

    public ControlFlowException() { this(null, true); }

    public ControlFlowException(@CheckForNull Throwable cause) {
        this(cause, true);
    }

    public ControlFlowException(boolean enableSuppression) {
        this(null, enableSuppression);
    }

    public ControlFlowException(@CheckForNull Throwable cause, boolean enableSuppression) {
        super(null == cause ? null : cause.toString(), cause, enableSuppression, TRACEABLE);
    }

    /**
     * Returns {@code true} if and only if a control flow exception should have
     * a full stack trace instead of an empty stack trace.
     * If and only if the system property with the key string
     * {@code net.java.truecommons.shed.ControlFlowException.traceable}
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
