/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.concurrent.Immutable;

/**
 * An attribute view for enumeratable keys.
 *
 * @param  <K> The type of the attribute keys.
 * @param  <V> The type of the attribute values.
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
@Immutable
public class NullAttributeView<K, V>
implements MutableAttributeView<K, V> {

    private static final NullAttributeView<?, ?>
            INSTANCE = new NullAttributeView<>();

    @SuppressWarnings("unchecked")
    public static <K, V> MutableAttributeView<K, V> get() {
        return (MutableAttributeView<K, V>) INSTANCE;
    }

    private NullAttributeView() { }

    @Override
    public Map<K, V> read() throws IOException {
        return Collections.emptyMap();
    }

    @Override
    public Map<K, V> read(Set<? extends K> keys) throws IOException {
        return read();
    }

    @Override
    public void write(Map<? extends K, ? extends V> attributes) { }

    @Override
    public void delete() { }

    @Override
    public void delete(Set<? extends K> keys) { }
}
