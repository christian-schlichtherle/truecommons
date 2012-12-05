/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

import java.io.IOException;

/**
 * A mutable attribute view for enumeratable keys.
 *
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public abstract class EnumKeyMutableAttributeView<K extends Enum<K>, V>
extends EnumKeyAttributeView<K, V> implements MutableAttributeView<K, V> {

    @Override
    public void delete() throws IOException { delete(keys()); }
}
