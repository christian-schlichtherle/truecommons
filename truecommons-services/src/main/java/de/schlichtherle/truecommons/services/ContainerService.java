/*
 * Copyright (C) 2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import javax.annotation.concurrent.ThreadSafe;

/**
 * An abstract service which contains a single product.
 * Container services are subject to service location by {@link Locator}s.
 * For best results, clients should create another abstract subclass which just
 * specifies the type parameter {@code P}.
 * In the following example the type parameter is specified as
 * {@link String}:
 * <pre>{@code
 * package com.company.spec;
 * 
 * import de.schlichtherle.truecommons.services.ContainerService;
 * 
 * public abstract class StringContainer
 * extends ContainerService<String> {
 * }
 * }</pre>
 * <p>
 * An implementation could now implement this service as follows:
 * <pre>{@code
 * package com.company.impl;
 * 
 * import com.company.spec.StringContainer;
 * 
 * public class GreetingContainer extends StringContainer {
 *     @Override
 *     public String get() {
 *         // Return the same instance on each call!
 *         return "Hello Christian!";
 *     }
 * }
 * }</pre>
 * <p>
 * Next, the implementation needs to advertise its service by providing a file
 * with the name {@code META-INF/services/com.company.spec.StringContainer}
 * on the run time class path with the following single line content:
 * <pre>{@code
 * com.company.impl.GreetingContainer
 * }</pre>
 * <p>
 * If multiple container services are locatable on the class path at run time,
 * the service with the greatest {@linkplain #getPriority() priority} gets
 * selected.
 * <p>
 * Finally, a client could now simply compose a container according to the
 * {@code StringContainer} specification by calling:
 * <pre>{@code
 * package com.company.client;
 * 
 * import de.schlichtherle.truecommons.services.Locator;
 * import com.company.spec.StringContainer;
 * 
 * public class Main {
 *     public static void main(String[] args) {
 *         Locator l = new Locator(Main.class); // specify calling class
 *         Container<String> c = l.container(StringContainer.class);
 *         String s = c.get(); // obtain product
 *         System.out.println(s); // use product
 *     }
 * }
 * <p>
 * Note that multiple calls to {@code c.get()} would always return the same
 * product again because {@code c} is a container, not a factory.
 * <p>
 * Implementations should be thread-safe.
 *
 * @param  <P> the type of the product to contain.
 * @author Christian Schlichtherle
 */
@ThreadSafe
public abstract class ContainerService<P>
extends ProviderService<P> implements Container<P> {
}
