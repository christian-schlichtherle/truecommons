/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service for creating products.
 * Factory services are subject to service location by {@link Locator}s.
 * For best results, clients should create another abstract subclass which just
 * specifies the type parameter {@code P}.
 * In the following example the type parameter is specified as
 * {@link StringBuilder}:
 * <pre>{@code
 * package com.company.spec;
 * 
 * import net.java.truecommons.services.FactoryService;
 * 
 * public abstract class StringBuilderFactory
 * extends FactoryService<StringBuilder> {
 * }
 * }</pre>
 * <p>
 * An implementation could now implement this service as follows:
 * <pre>{@code
 * package com.company.impl;
 * 
 * import com.company.spec.StringBuilderFactory;
 * 
 * public class GreetingFactory extends StringBuilderFactory {
 *     \@Override
 *     public StringBuilder get() {
 *         // Return a new instance on each call!
 *         return new StringBuilder("Hello Christian!");
 *     }
 * }
 * }</pre>
 * <p>
 * Next, the implementation needs to advertise its service by providing a file
 * with the name {@code META-INF/services/com.company.spec.StringBuilderFactory}
 * on the run time class path with the following single line content:
 * <pre>{@code
 * com.company.impl.GreetingFactory
 * }</pre>
 * <p>
 * If multiple factory services are locatable on the class path at run time,
 * the service with the greatest {@linkplain #getPriority() priority} gets
 * selected.
 * <p>
 * Finally, a client could now simply compose a factory according to the
 * {@code StringBuilderFactory} specification by calling:
 * <pre>{@code
 * package com.company.client;
 * 
 * import net.java.truecommons.services.Locator;
 * import com.company.spec.StringBuilderFactory;
 * 
 * public class Main {
 *     public static void main(String[] args) {
 *         Locator l = new Locator(Main.class); // specify calling class
 *         Factory<StringBuilder> f = l.factory(StringBuilderFactory.class);
 *         StringBuilder b = f.get(); // create product
 *         System.out.println(b.toString()); // use product
 *     }
 * }
 * }</pre>
 * <p>
 * Note that multiple calls to {@code f.get()} would always return a new
 * product because {@code f} is a factory, not a container.
 * <p>
 * Implementations should be thread-safe.
 * 
 * @param  <P> the type of the products to create.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class FactoryService<P>
extends ProviderService<P> implements Factory<P> {
}
