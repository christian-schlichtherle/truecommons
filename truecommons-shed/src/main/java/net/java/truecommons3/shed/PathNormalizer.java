/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons3.shed;

/**
 * A normalizer for path names.
 * 
 * @author Christian Schlichtherle
 */
public class PathNormalizer {

    private final char separatorChar;
    private final StringBuilder builder = new StringBuilder();
    private String path;

    public PathNormalizer(final char separatorChar) {
        this.separatorChar = separatorChar;
    }

    /**
     * Removes all redundant separators, dot directories ({@code "."}) and
     * dot-dot directories ({@code ".."}) from the given path name and
     * returns the result.
     * If present, a single trailing separator character is retained,
     * except after a dot-dot directory which couldn't get erased.
     * A resulting single dot-directory is truncated to an empty path.
     * <p>
     * On Windows, a path may be prefixed by a drive letter followed by a
     * colon.
     * On all platforms, a path may be prefixed by two leading separators
     * to indicate a UNC, although this is currently only supported on
     * Windows.
     *
     * @param  path the non-{@code null} path name to normalize.
     * @return {@code path} if it was already in normalized form.
     *         Otherwise, a new string with the normalized form of the
     *         given path name.
     */
    public String normalize(final String path) {
        final int prefixLen = Paths.prefixLength(path, separatorChar, false);
        final int pathLen = path.length();
        this.path = path.substring(prefixLen, pathLen);
        builder.setLength(0);
        builder.ensureCapacity(pathLen);
        normalize(0, pathLen - prefixLen);
        builder.insert(0, path.substring(0, prefixLen));
        int bufferLen = builder.length();
        String result;
        if (pathLen > 0 && path.charAt(pathLen - 1) == separatorChar || pathLen > 1 && path.charAt(pathLen - 2) == separatorChar && path.charAt(pathLen - 1) == '.') {
            slashify();
            bufferLen = builder.length();
        }
        if (bufferLen == path.length()) {
            assert path.equals(builder.toString());
            result = path;
        } else {
            result = builder.toString();
            if (path.startsWith(result)) {
                result = path.substring(0, bufferLen);
            }
        }
        assert !result.equals(path) || result == path; // postcondition
        return result;
    }

    /**
     * This is a recursive call: The top level call should provide
     * {@code 0} as the {@code skip} parameter and the length
     * of the path as the {@code end} parameter.
     *
     * @param  collapse the number of adjacent <i>dir/..</i> segments in
     *         the path to collapse.
     *         This value must not be negative.
     * @param  end the current position in {@code path}.
     *         Only the string to the left of this index is considered.
     *         If not positive, nothing happens.
     * @return The number of adjacent segments in the path which have
     *         <em>not</em> been collapsed at this position.
     */
    private int normalize(final int collapse, final int end) {
        assert collapse >= 0;
        if (0 >= end)
            return collapse;
        final int next = path.lastIndexOf(separatorChar, end - 1);
        final String base = path.substring(next + 1, end);
        int notCollapsed;
        if (0 >= base.length() || ".".equals(base)) {
            return normalize(collapse, next);
        } else if ("..".equals(base)) {
            notCollapsed = normalize(collapse + 1, next) - 1;
            if (0 > notCollapsed) {
                return 0;
            }
        } else if (0 < collapse) {
            notCollapsed = normalize(collapse - 1, next);
            slashify();
            return notCollapsed;
        } else {
            assert 0 == collapse;
            notCollapsed = normalize(0, next);
            assert 0 == notCollapsed;
        }
        slashify();
        builder.append(base);
        return notCollapsed;
    }

    private void slashify() {
        final int bufferLen = builder.length();
        if (bufferLen > 0 && builder.charAt(bufferLen - 1) != separatorChar) {
            builder.append(separatorChar);
        }
    }
    
}
