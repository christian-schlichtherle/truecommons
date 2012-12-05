/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * An attribute view for enumeratable keys.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public abstract class EnumKeyAttributeView<K extends Enum<K>, V>
implements AttributeView<K, V> {

    protected abstract Class<K> clazz();

    protected Set<K> keys() { return EnumSet.allOf(clazz()); }

    @Override
    public Map<K, V> read() throws IOException { return read(keys()); }
}
