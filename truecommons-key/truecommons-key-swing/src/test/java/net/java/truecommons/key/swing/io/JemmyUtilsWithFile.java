/*
 * Copyright (C) 2005-2013 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.key.swing.io;

import java.io.*;
import javax.swing.filechooser.FileSystemView;
import net.java.truecommons.key.swing.KeyPanelTestSuite;
import net.java.truecommons.key.swing.util.JemmyUtils;
import org.junit.After;

/**
 * @author Christian Schlichtherle
 */
public abstract class JemmyUtilsWithFile extends JemmyUtils {

    protected final File file;

    public JemmyUtilsWithFile() throws IOException {
        file = File.createTempFile(
                KeyPanelTestSuite.class.getSimpleName(),
                null,
                FileSystemView.getFileSystemView().getDefaultDirectory());
    }

    @After public void deleteFile() throws IOException {
        if (!file.delete()) throw new FileNotFoundException(file.getPath());
    }
}
