/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.key.swing.util;

import java.awt.Component;
import static java.awt.EventQueue.*;
import java.lang.reflect.*;
import javax.swing.*;
import org.netbeans.jemmy.*;
import org.netbeans.jemmy.drivers.*;
import org.netbeans.jemmy.drivers.buttons.ButtonMouseDriver;
import org.netbeans.jemmy.drivers.text.SwingTextKeyboardDriver;
import org.netbeans.jemmy.operators.*;

/**
 * A helper class for JUnit tests with JemmyUtils.
 * Note that using this class has permanent side effects on
 * {@link JemmyProperties}!
 * In particular, the current properties are modified to output nothing and
 * some composite drivers get installed.
 *
 * @author Christian Schlichtherle
 */
public class JemmyUtils {

    public static final long WAIT_EMPTY_MILLIS = 100;

    static {
        // TODO: Make pushJemmyProperties() work and remove this static
        // initializer!
        setUpJemmyProperties();
    }

    //@BeforeClass
    public static void pushJemmyProperties() throws InterruptedException {
        // TODO: This causes Operator.getDefaultVerification() to throw an NPE
        // because the property "Operator.Verification" somehow hasn't been
        // copied.
        // Therefore the @BeforeClass annotation is currently commented out.
        runOnEdtNow(new Runnable() {
            @Override public void run() {
                JemmyProperties.push();
                setUpJemmyProperties();
            }
        });
    }

    //@AfterClass
    public static void popJemmyProperties() throws InterruptedException {
        // TODO: This causes Operator.getDefaultVerification() to throw an NPE
        // because the property "Operator.Verification" somehow hasn't been
        // copied.
        // Therefore the @AfterClass annotation is currently commented out.
        runOnEdtNow(new Runnable() {
            @Override public void run() {
                JemmyProperties.pop();
            }
        });
    }

    public static void setUpJemmyProperties() {
        JemmyProperties.setCurrentOutput(TestOut.getNullOutput()); // shut up!
        // These calls use the pushed properties.
        DriverManager.setButtonDriver(new AtomicButtonDriver());
        //DriverManager.setTextDriver(new AtomicTextDriver());
    }

    protected JemmyUtils() { }

    /**
     * Displays a new {@link JFrame} with the given {@code component} and
     * returns an operator for it.
     *
     * @param  component the component to display in the new frame.
     * @return An operator for the new frame which shows the given component.
     * @throws InterruptedException if the current thread is interrupted while
     */
    public static JFrameOperator showFrameWith(final Component component)
    throws InterruptedException {
        class Show implements Runnable {
            JFrame frame;

            @Override
            public void run() {
                frame = new JFrame();
                frame.add(component);
                frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        } // Show

        final Show show = new Show();
        runOnEdtNow(show);
        return new JFrameOperator(show.frame);
    }

    public static void runOnEdt(final Runnable task) {
        if (isDispatchThread()) task.run();
        else invokeLater(task);
    }

    public static void runOnEdtNow(final Runnable task)
    throws InterruptedException {
        if (isDispatchThread()) {
            task.run();
        } else {
            try {
                invokeAndWait(task);
            } catch (final InvocationTargetException ex) {
                throw new UndeclaredThrowableException(ex);
            }
        }
    }

    /**
     * This driver posts an event for execution on the EDT which will in turn
     * create a series of AWT events to simulate the respective GUI gesture
     * (press, release and push).
     * In multithreaded environments, this will help to prevent mixing of AWT
     * events when two or more threads are concurrently clicking on some
     * {@link ComponentOperator}s.
     * The AWT events must not get mixed because otherwise a component would
     * not recognize the respective GUI gesture and ignore the AWT events.
     */
    private static final class AtomicButtonDriver
    extends LightSupportiveDriver
    implements ButtonDriver {
        final ButtonDriver driver = new ButtonMouseDriver();

        AtomicButtonDriver() {
            super(new String[] { ComponentOperator.class.getName() });
        }

        @Override
        public void press(final ComponentOperator op) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.press(op);
                }
            });
        }

        @Override
        public void release(final ComponentOperator op) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.release(op);
                }
            });
        }

        @Override
        public void push(final ComponentOperator op) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.push(op);
                }
            });
        }
    } // AtomicButtonDriver

    /**
     * The idea of this driver is conceptually the same as with
     * {@link AtomicButtonDriver}.
     * However, using it may dead lock the tests for some reason, so it's
     * currently unused.
     */
    private static final class AtomicTextDriver
    extends LightSupportiveDriver
    implements TextDriver {
        final TextDriver driver = new SwingTextKeyboardDriver();

        AtomicTextDriver() {
            super(new String[] { JTextComponentOperator.class.getName() });
        }

        @Override
        public void changeCaretPosition(
                final ComponentOperator op,
                final int position) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.changeCaretPosition(op, position);
                }
            });
        }

        @Override
        public void selectText(
                final ComponentOperator op,
                final int startPosition,
                final int finalPosition) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.selectText(op, startPosition, finalPosition);
                }
            });
        }

        @Override
        public void clearText(final ComponentOperator op) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.clearText(op);
                }
            });
        }

        @Override
        public void typeText(
                final ComponentOperator op,
                final String text,
                final int caretPosition) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.typeText(op, text, caretPosition);
                }
            });
        }

        @Override
        public void changeText(
                final ComponentOperator op,
                final String text) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.changeText(op, text);
                }
            });
        }

        @Override
        public void enterText(
                final ComponentOperator op,
                final String text) {
            op.getQueueTool().invoke(new Runnable() {
                @Override public void run() {
                    driver.enterText(op, text);
                }
            });
        }
    } // AtomicTextDriver
}
