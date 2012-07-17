/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for modifying products.
 * Modifier services are subject to service location by {@link Locator}s.
 * For best results, clients should create another abstract subclass which just
 * specifies the type parameter {@code P}.
 * The following example accompanies the example for {@link FactoryService},
 * so the type parameter is specified as {@link StringBuilder} again:
 * <pre>{@code
 * package com.company.spec;
 * 
 * import de.schlichtherle.truecommons.services.ModifierService;
 * 
 * public abstract class StringBuilderModifier
 * extends ModifierService<StringBuilder> {
 * }
 * }</pre>
 * <p>
 * An implementation could now implement this service as follows:
 * <pre>{@code
 * package com.company.impl;
 * 
 * import com.company.spec.StringBuilderModifier;
 * 
 * public class SmalltalkModifier extends StringBuilderModifier {
 *     @Override
 *     public StringBuilder apply(StringBuilder b) {
 *         // Modify and return the same instance on each call!
 *         return b.append(" How do you do?");
 *     }
 * }
 * }</pre>
 * <p>
 * Next, the implementation needs to advertise its service by providing a file
 * with the name {@code META-INF/services/com.company.spec.StringBuilderModifier}
 * on the run time class path with the following single line content:
 * <pre>{@code
 * com.company.impl.SmalltalkModifier
 * }</pre>
 * <p>
 * If multiple modifier services are locatable on the class path at run
 * time, they are applied in ascending order of their
 * {@linkplain #getPriority() priority} so that the result of the modifier
 * service with the greatest number becomes the result of the entire
 * modifier chain.
 * <p>
 * Finally, a client could now simply compose a factory with some modifiers
 * according to the {@code StringBuilderFactory} and
 * {@link StringBuilderModifier} specification by calling:
 * <pre>{@code
 * package com.company.client;
 * 
 * import de.schlichtherle.truecommons.services.Locator;
 * import com.company.spec.StringBuilderFactory;
 * import com.company.spec.StringBuilderModifier;
 * 
 * public class Main {
 *     public static void main(String[] args) {
 *         Locator l = new Locator(Main.class); // specify calling class
 *         Factory<StringBuilder> f = l.factory(StringBuilderFactory.class,
 *                                              StringBuilderModifier.class);
 *         StringBuilder b = f.apply(); // create product
 *         System.out.println(b.toString()); // use product
 *     }
 * }
 * <p>
 * Note that multiple calls to {@code f.apply()} would always return a new
 * product because {@code f} is a factory, not a container.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to modify.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class ModifierService<P>
extends FunctionService<P> implements Modifier<P>{
}
