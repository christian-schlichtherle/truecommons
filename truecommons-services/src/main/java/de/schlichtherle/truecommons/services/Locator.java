/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package de.schlichtherle.truecommons.services;

import java.text.MessageFormat;
import java.util.*;
import javax.annotation.CheckForNull;
import javax.annotation.concurrent.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * Creates containers or factories of products with some decorators.
 * <p>
 * If the class loader provided to the constructor is the current thread's
 * context class loader, then the methods of this class will locate services
 * using only this class loader.
 * Otherwise, the given class loader is used first. Second, the current
 * thread's context class loader is used.
 *
 * @author Christian Schlichtherle
 */
@ThreadSafe
public final class Locator {

    private static final ResourceBundle
            bundle = ResourceBundle.getBundle(Locator.class.getName());
    private static final Logger logger = LoggerFactory.getLogger(Locator.class);
    private static final String MSG = "{}";
    private static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");

    private final Loader loader;

    /**
     * Constructs a new locator which uses the class loader of the given client
     * class first.
     *
     * @param client the class which identifies the calling client.
     */
    public Locator(final Class<?> client) {
        this.loader = new Loader(client.getClassLoader());
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
     * @param  decorator the class of the decoractor service for the product.
     * @return A new container of a single product.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Container<P> container(
            final Class<? extends ProviderService<P>> provider,
            final @CheckForNull Class<? extends DecoratorService<P>> decorator)
    throws ServiceConfigurationError {
        final ProviderService<P> p = service(provider);
        final DecoratorService<P>[] d = null == decorator ? null
                : decorators(decorator);
        return new Store<P>(null == d || 0 == d.length ? p
                : new ProviderWithSomeDecorators<P>(p, d));
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
     * @param  decorator the class of the decoractor service for the products.
     * @return A new factory of products.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    public <P> Factory<P> factory(
            final Class<? extends FactoryService<P>> factory,
            final @CheckForNull Class<? extends DecoratorService<P>> decorator)
    throws ServiceConfigurationError {
        final FactoryService<P> f = service(factory);
        final DecoratorService<P>[] d = null == decorator ? null
                : decorators(decorator);
        return null == d || 0 == d.length ? f
                : new FactoryWithSomeDecorators<P>(f, d);
    }

    /**
     * Instantiates a service implementation class of the given specification
     * class.
     * If multiple service implementation classes are locatable on the class
     * path at run time, the service with the greatest
     * {@linkplain ProviderService#getPriority() priority} gets selected.
     * If multiple decorator services are locatable on the class path at run
     * time, they are applied in ascending order of their
     * {@linkplain DecoratorService#getPriority() priority} so that the product
     * of the decorator service with the greatest number becomes the head of
     * the resulting product chain.
     * 
     * @param  <S> the type of the service.
     * @param  spec the specification class of the service.
     * @return The prioritized instance of the service.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a service implementation class fails for some reason.
     */
    private <S extends Service> S service(final Class<S> spec)
    throws ServiceConfigurationError {
        S service = loader.instanceOf(spec, null);
        if (null == service) {
            for (final Iterator<S> i = loader.allInstancesOf(spec); i.hasNext(); ) {
                final S newService = i.next();
                logger.debug(CONFIG, MSG, new Msg("located", newService));
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
                            logger.warn(MSG, new Msg("collision",
                                    op, service, newService));
                    }
                }
            }
        }
        if (null == service)
            throw new ServiceConfigurationError(
                    MessageFormat.format(bundle.getString("null"), spec));
        logger.debug(CONFIG, MSG, new Msg("selecting", service));
        return service;
    }

    /**
     * Instantiates all decorator service implementation classes of the given
     * specification class.
     * If multiple decorator services are locatable on the class path at run
     * time, they are sorted in ascending order of their
     * {@linkplain DecoratorService#getPriority() priority} so that when
     * applied in order, the product of the decorator service with the greatest
     * priority would become the head of the resulting product chain.
     * 
     * @param  <P> the type of the product.
     * @param  spec the specification class of the decorator service.
     * @return The ordered instances of the decorator service.
     * @throws ServiceConfigurationError if loading or instantiating
     *         a decorator service implementation class fails for some reason.
     */
    private <P> DecoratorService<P>[] decorators(
            final Class<? extends DecoratorService<P>> spec)
    throws ServiceConfigurationError {
        final Collection<? extends DecoratorService<P>> collection
                = Loader.collectionWithUniqueClassNames(
                    loader.allInstancesOf(spec));
        @SuppressWarnings("unchecked")
        final DecoratorService<P>[] array = (DecoratorService<P>[])
                collection.toArray(new DecoratorService<?>[collection.size()]);
        Arrays.sort(array, new ServiceComparator());
        for (final DecoratorService<P> service : array)
            logger.debug(CONFIG, MSG, new Msg("selecting", service));
        return array;
    }

    private static final class Msg extends Message {
        Msg(String key, Object... args) { super(bundle, key, args); }
    }
}
