/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.macosx;

import net.java.truecommons.key.macosx.keychain.DuplicateItemException;
import net.java.truecommons.key.macosx.keychain.Keychain;
import net.java.truecommons.key.macosx.keychain.Keychain.AttributeClass;
import net.java.truecommons.key.macosx.keychain.Keychain.Item;
import net.java.truecommons.key.macosx.keychain.Keychain.Visitor;
import net.java.truecommons.key.macosx.keychain.KeychainException;
import net.java.truecommons.key.spec.AbstractKeyManager;
import net.java.truecommons.key.spec.KeyManager;
import net.java.truecommons.key.spec.KeyProvider;
import net.java.truecommons.key.spec.prompting.AbstractPromptingPbeParameters;
import net.java.truecommons.logging.LocalizedLogger;
import net.java.truecommons.shed.Option;
import org.slf4j.Logger;

import javax.annotation.concurrent.ThreadSafe;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static net.java.truecommons.key.macosx.keychain.Keychain.AttributeClass.GENERIC;
import static net.java.truecommons.key.macosx.keychain.Keychain.AttributeClass.SERVICE;
import static net.java.truecommons.key.macosx.keychain.Keychain.ItemClass.GENERIC_PASSWORD;
import static net.java.truecommons.shed.Buffers.*;

/**
 * Uses Apple's Keychain Services API to persist passwords.
 *
 * @since  TrueCommons 2.2
 * @author Christian Schlichtherle
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
@ThreadSafe
public final class OsxKeyManager<P extends AbstractPromptingPbeParameters<P, ?>>
extends AbstractKeyManager<P> {

    private static final String KEYCHAIN = "TrueCommons KeyManager";
    private static final String ACCOUNT = KEYCHAIN;
    private static final Logger logger = new LocalizedLogger(OsxKeyManager.class);

    private final KeyManager<P> manager;
    private final Class<P> keyClass;
    private Option<Keychain> keychain = Option.none();
    private volatile boolean skip;

    public OsxKeyManager(final KeyManager<P> manager, final Class<P> keyClass) {
        this.manager = Objects.requireNonNull(manager);
        this.keyClass = Objects.requireNonNull(keyClass);
    }

    @Override
    public KeyProvider<P> provider(URI uri) {
        return new OsxKeyProvider<>(this, uri, manager.provider(uri));
    }

    @Override
    public void release(URI uri) {
        skip = false;
        manager.release(uri);
    }

    @Override
    public void link(final URI originUri, final URI targetUri) {
        final Option<P> param = getKey(originUri);
        manager.link(originUri, targetUri);
        setKey(targetUri, param);
    }

    @Override
    public void unlink(final URI uri) {
        manager.unlink(uri);
        setKey(uri, Option.<P>none());
    }

    Option<P> getKey(final URI uri) {
        return access(uri, new Action<Option<P>>() {
            @Override
            public Option<P> call(
                    final Keychain keychain,
                    final Map<AttributeClass, ByteBuffer> attributes)
            throws KeychainException {

                class Read implements Visitor {

                    Option<P> param = Option.none();

                    @Override
                    @SuppressWarnings("unchecked")
                    public void visit(final Item item) throws KeychainException {
                        param = (Option<P>) deserialize(Option.apply(item.getAttribute(GENERIC)));
                        if (param.isEmpty()) {
                            try {
                                param = Option.some(keyClass.newInstance());
                            } catch (final InstantiationException | IllegalAccessException ex) {
                                logger.debug("getKey.exception", ex);
                                return;
                            }
                        }
                        assert null == param.get().getSecret();
                        final ByteBuffer secret = item.getSecret();
                        try {
                            param.get().setSecret(secret);
                        } finally {
                            fill(secret, (byte) 0);
                        }
                    }
                }

                final Read read = new Read();
                keychain.visitItems(GENERIC_PASSWORD, attributes, read);
                return read.param;
            }
        });
    }

    void setKey(
            final URI resource,
            final Option<P> optionalParam) {
        access(resource, new Action<Option<Void>>() {
            @Override
            public Option<Void> call(
                    final Keychain keychain,
                    final Map<AttributeClass, ByteBuffer> attributes)
            throws KeychainException {

                for (final P param : optionalParam) {
                    for (final ByteBuffer newSecret : Option.apply(param.getSecret())) {
                        try {
                            final Option<ByteBuffer> newXml = serialize(optionalParam);
                            @SuppressWarnings("unchecked")
                            final Option<P> newParam = (Option<P>) deserialize(newXml); // rip off transient fields

                            class Update implements Visitor {
                                @Override
                                public void visit(final Item item)
                                        throws KeychainException {
                                    {
                                        final ByteBuffer oldSecret =
                                                item.getSecret();
                                        if (!newSecret.equals(oldSecret))
                                            item.setSecret(newSecret);
                                    }
                                    {
                                        final Option<ByteBuffer> oldXml =
                                                Option.apply(item.getAttribute(GENERIC));
                                        @SuppressWarnings("unchecked")
                                        final Option<P> oldParam =
                                                (Option<P>) deserialize(oldXml);
                                        if (!newParam.equals(oldParam))
                                            item.setAttribute(GENERIC, newXml.get());
                                    }
                                }
                            }

                            try {
                                attributes.put(GENERIC, newXml.get());
                                keychain.createItem(GENERIC_PASSWORD, attributes, newSecret);
                            } catch (final DuplicateItemException ex) {
                                attributes.remove(GENERIC);
                                keychain.visitItems(GENERIC_PASSWORD, attributes, new Update());
                            }
                        } finally {
                            fill(newSecret, (byte) 0);
                        }

                        return Option.none();
                    }
                    throw new IllegalArgumentException();
                }

                class Delete implements Visitor {
                    @Override
                    public void visit(Item item) throws KeychainException {
                        item.delete();
                    }
                }

                keychain.visitItems(GENERIC_PASSWORD, attributes, new Delete());

                return Option.none();
            }
        });
    }

    static Option<ByteBuffer> serialize(final Option<?> optionalObject) {
        for (final Object object : optionalObject) {
            try (final ByteArrayOutputStream bos = new ByteArrayOutputStream(512)) {
                try (final XMLEncoder _ = new XMLEncoder(bos)) {
                    _.writeObject(object);
                }
                bos.flush(); // redundant
                return Option.some(copy(ByteBuffer.wrap(bos.toByteArray())));
            } catch (final IOException ex) {
                logger.warn("serialize.exception", ex);
                return Option.none();
            }
        }
        return Option.none();
    }

    static Option<?> deserialize(final Option<ByteBuffer> optionalXml) {
        for (final ByteBuffer xml : optionalXml) {
            final byte[] array = new byte[xml.remaining()]; // cannot use bb.array()!
            xml.duplicate().get(array);
            try (final XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(array))) {
                return Option.apply(decoder.readObject());
            }
        }
        return Option.none();
    }

    private <T> Option<T> access(final URI uri, final Action<Option<T>> action) {
        if (skip)
            return Option.none();
        try {
            return action.call(open(), attributes(uri));
        } catch (final KeychainException ex) {
            skip = true;
            logger.debug("access.exception", ex);
            return Option.none();
        }
    }

    private static Map<AttributeClass, ByteBuffer> attributes(final URI uri) {
        final Map<AttributeClass, ByteBuffer>
                m = new EnumMap<>(AttributeClass.class);
        m.put(AttributeClass.ACCOUNT, byteBuffer(ACCOUNT));
        m.put(SERVICE, byteBuffer(uri.toString()));
        return m;
    }

    private synchronized Keychain open() throws KeychainException {
        for (Keychain kc : keychain)
            return kc;
        final Keychain kc = Keychain.open(KEYCHAIN, null);
        keychain = Option.some(kc);
        return kc;
    }

    private synchronized void close() {
        for (final Keychain kc : keychain) {
            keychain = Option.none();
            kc.close();
        }
    }

    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        try { close(); }
        finally { super.finalize(); }
    }

    private interface Action<T> {
        T call(Keychain keychain, Map<AttributeClass, ByteBuffer> attributes)
        throws KeychainException;
    }
}
