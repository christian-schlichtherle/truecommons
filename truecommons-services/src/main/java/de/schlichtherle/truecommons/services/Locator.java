/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import de.schlichtherle.truecommons.logging.BundledMessage;
import de.schlichtherle.truecommons.logging.LocalizedLogger;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.ServiceConfigurationError;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Creates containers or factories of products with some applied decorators.
 * Resolving service instances is done in several steps:
 * <p>
 * First, the name of a given <em>provider</em> service class is used as the
 * key string to lookup a {@link System#getProperty system property}.
 * If this yields a value then it's supposed to name a class which gets loaded
 * and instantiated by calling its public no-argument constructor.
 * <p>
 * Otherwise, the class path is searched for any resource files with the name
 * {@code "META-INF/services/"} plus the name of the given provider service
 * class.
 * If this yields no results, a {@link ServiceConfigurationError} is thrown.
 * <p>
 * Otherwise the classes with the names contained in these files get loaded and
 * instantiated by calling their public no-argument constructor.
 * Next, the service instances are filtered according to their
 * {@linkplain Service#getPriority() priority}.
 * Only the service instance with the highest priority is kept for future use.
 * <p>
 * Next, the class is searched again for any resource files with the name
 * {@code "META-INF/services/"} plus the name of a given <em>decorator</em>
 * service class.
 * If this yields some results, the classes with the names contained in these
 * files get loaded and instantiated by calling their public no-argument
 * constructor.
 * Next, the service instances get sorted in ascending order of their
 * {@linkplain DecoratorService#getPriority() priority} and kept for future use.
 * <p>
 * Finally, depending on the requesting method either a container or a factory
 * gets created which will use the located provider service instance and the
 * decorator service instances to obtain a product and decorate it in order.
 *
 * @author Christian Schlichtherle
 */
@ThreadSafe
public final class Locator {

    private static final Logger logger = new LocalizedLogger(Locator.class);
    private static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");

    private final Loader loader;

    /**
     * Constructs a new locator which uses the class loader of the given client
     * class before using the current thread context's class loader unless the
     * latter is identical to the former.
     *
     * @param client the class which identifies the calling client.
     */
    public Locator(final Class<?> client) {
        this.loader = new Loader(client.getClassLoader());
    }

    /**
     * Creates a new factory of products.
     * 
     * @param  <P> the type of the products to create.
     * @param  factory the class of the factory service for the products.
     * @return A new factory of products.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Factory<P> factory(Class<? extends FactoryService<P>> factory)
    throws ServiceConfigurationError {
        return factory(factory, null);
    }

    /**
     * Creates a new factory of products.
     * 
     * @param  <P> the type of the products to create.
     * @param  factory the class of the factory service for the products.
     * @param  functions the class of the function services for the products.
     * @return A new factory of products.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Factory<P> factory(
            final Class<? extends FactoryService<P>> factory,
            final @CheckForNull Class<? extends FunctionService<P>> functions)
    throws ServiceConfigurationError {
        final FactoryService<P> p = provider(factory);
        final FunctionService<P>[] f = null == functions ? null
                : functions(functions);
        return null == f || 0 == f.length ? p
                : new FactoryWithSomeFunctions<P>(p, f);
    }

    /**
     * Creates a new container of a single product.
     * 
     * @param  <P> the type of the product to contain.
     * @param  provider the class of the provider service for the product.
     * @return A new container of a single product.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Container<P> container(Class<? extends ProviderService<P>> provider)
    throws ServiceConfigurationError {
        return container(provider, null);
    }

    /**
     * Creates a new container of a single product.
     * 
     * @param  <P> the type of the product to contain.
     * @param  provider the class of the provider service for the product.
     * @param  decorator the class of the decoractor services for the product.
     * @return A new container of a single product.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Container<P> container(
            final Class<? extends ProviderService<P>> provider,
            final @CheckForNull Class<? extends DecoratorService<P>> decorator)
    throws ServiceConfigurationError {
        final ProviderService<P> p = provider(provider);
        final DecoratorService<P>[] d = null == decorator ? null
                : functions(decorator);
        return new Store<P>(null == d || 0 == d.length ? p
                : new ProviderWithSomeFunctions<P>(p, d));
    }

    private <S extends ProviderService<?>> S provider(final Class<S> spec)
    throws ServiceConfigurationError {
        S service = loader.instanceOf(spec, null);
        if (null == service) {
            for (final S newService : loader.instancesOf(spec)) {
                logger.debug(CONFIG, "located", newService);
                if (null == service) {
                    service = newService;
                } else {
                    final int op = service.getPriority();
                    final int np = newService.getPriority();
                    if (op < np) {
                        service = newService;
                    } else if (op == np) {
                        // Mind you that the loader may return multiple class
                        // instances with an equal name which are loaded by
                        // different class loaders.
                        if (!service.getClass().getName()
                                .equals(newService.getClass().getName()))
                            logger.warn("collision",
                                    new Object[] { op, service, newService });
                    }
                }
            }
        }
        if (null == service)
            throw new ServiceConfigurationError(
                    new BundledMessage(Locator.class, "null", spec).toString());
        logger.debug(CONFIG, "selecting", service);
        return service;
    }

    private <S extends FunctionService<?>> S[] functions(final Class<S> spec)
    throws ServiceConfigurationError {
        final Collection<S> c = new LinkedList<S>();
        for (final S service : loader.instancesOf(spec)) c.add(service);
        @SuppressWarnings("unchecked")
        final S[] a = c.toArray((S[]) Array.newInstance(spec, c.size()));
        Arrays.sort(a, new ServiceComparator());
        for (final S service : a) logger.debug(CONFIG, "selecting", service);
        return a;
    }
}
