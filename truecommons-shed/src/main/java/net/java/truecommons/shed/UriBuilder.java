/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import static net.java.truecommons.shed.UriEncoder.Encoding.*;

/**
 * A mutable JavaBean for composing URIs according to
 * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
 * and its updates in
 * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
 * for IPv6 addresses.
 * <p>
 * This class complements the immutable {@link URI} class by enabling its
 * clients to compose a URI from its components which can get read or written
 * as independent properties.
 * Each URI is composed of the five components scheme, authority, path, query
 * and fragment.
 * When done with setting the properties for the URI components, the resulting
 * URI can be composed by calling any of the methods {@link #buildUri()},
 * {@link #toUri()}, {@link #buildString()} or {@link #toString()}.
 * <p>
 * This class quotes illegal characters wherever required for the respective
 * URI component.
 * As a deviation from RFC&nbsp;2396, non-US-ASCII characters get preserved
 * when encoding.
 * <p>
 * Note that using this class is superior to the five argument URI constructor
 * <code>new {@link URI#URI(String, String, String, String, String) URI(scheme, authority, path, query, fragment)}</code>
 * because the URI constructor does not quote all paths correctly.
 * For example, {@code new URI(null, null, "foo:bar", null, null)} does not
 * quote the colon before parsing so the resulting URI will have a scheme
 * component {@code foo} and a path component {@code bar} instead of just a
 * path component {@code foo:bar}.
 *
 * <h3>Identities</h3>
 * For any {@link URI} {@code u} it is generally true that
 * <pre>{@code new UriBuilder(u).toUri().equals(u);}</pre>
 * and
 * <pre>{@code new UriBuilder().uri(u).toUri().equals(u);}</pre>
 * and
 * <pre>{@code
 * new UriBuilder()
 *     .scheme(u.getScheme())
 *     .authority(u.getAuthority())
 *     .path(u.isOpaque() ? u.getSchemeSpecificPart() : u.getPath())
 *     .query(u.getQuery())
 *     .fragment(u.getFragment())
 *     .toUri()
 *     .equals(u);
 * }</pre>
 * These identity productions apply for the method {@link #toUri()} as well as
 * the method {@link #buildUri()}.
 *
 * @see    <a href="http://www.ietf.org/rfc/rfc2396.txt">
 *         RFC&nbsp;2396: Uniform Resource Identifiers (URI): Generic Syntax</a>
 * @see    <a href="http://www.ietf.org/rfc/rfc2732.txt">
 *         RFC&nbsp;2732: Format for Literal IPv6 Addresses in URL's</a>
 * @author Christian Schlichtherle
 */
public final class UriBuilder {

    private final UriEncoder encoder;
    private Option<StringBuilder> builder = Option.none();
    private Option<String> scheme = Option.none();
    private Option<String> authority = Option.none();
    private Option<String> path = Option.none();
    private Option<String> query = Option.none();
    private Option<String> fragment = Option.none();

    /**
     * Constructs a new URI builder.
     * Equivalent to {@link #UriBuilder(boolean) UriBuilder(false)}.
     */
    public UriBuilder() {
        this(false);
    }

    /**
     * Constructs a new URI builder.
     *
     * @param raw If {@code true}, then the {@code '%'} character doesn't get
     *        quoted.
     */
    public UriBuilder(boolean raw) {
        this.encoder = new UriEncoder(Option.<Charset>none(), raw);
    }

    /**
     * Returns a new URI string which conforms to the syntax constraints
     * defined in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
     * and its updates in
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
     * for IPv6 addresses.
     *
     * @return A valid URI string which is composed from the properties of
     *         this URI builder.
     * @throws IllegalStateException if composing a valid URI is not possible.
     * @see    #buildString()
     */
    @Override
    public String toString() {
        try {
            return buildString();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Returns a new URI string which conforms to the syntax constraints
     * defined in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
     * and its updates in
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
     * for IPv6 addresses.
     *
     * @return A valid URI string which is composed from the properties of
     *         this URI builder.
     * @throws URISyntaxException if composing a valid URI is not possible due
     *         to an invalid scheme.
     * @see    #toString()
     */
    public String buildString() throws URISyntaxException {
        final StringBuilder r = resetBuilder(); // result
        final Option<StringBuilder> or = Option.some(r);
        int errIdx = -1;                        // error index
        Option<String> errMsg = Option.none();  // error message
        final Option<String> s = scheme, a = authority, p = path, q = query, f = fragment;
        final boolean absUri = !s.isEmpty();
        if (absUri)
            r.append(s.get()).append(':');
        final int ssp = r.length();             // index of scheme specific part
        final boolean hasAuth = !a.isEmpty();
        if (hasAuth) {
            r.append("//");
            encoder.encode(AUTHORITY, a.get(), or);
        }
        boolean absPath = false;
        if (!p.isEmpty() && !p.get().isEmpty()) {
            if (p.get().startsWith("/")) {
                absPath = true;
                encoder.encode(ABSOLUTE_PATH, p.get(), or);
            } else if (hasAuth) {
                absPath = true;
                errIdx = r.length();
                errMsg = Option.some("Relative path with " + (a.isEmpty() ? "" : "non-") + "empty authority");
                encoder.encode(ABSOLUTE_PATH, p.get(), or);
            } else if (absUri) {
                encoder.encode(QUERY, p.get(), or);
            } else {
                encoder.encode(PATH, p.get(), or);
            }
        }
        if (!q.isEmpty()) {
            r.append('?');
            if (absUri && !absPath) {
                errIdx = r.length();
                errMsg = Option.some("Query in opaque URI");
            }
            encoder.encode(QUERY, q.get(), or);
        }
        assert absUri == 0 < ssp;
        if (absUri && ssp >= r.length()){
            errIdx = r.length();
            errMsg = Option.some("Empty scheme specific part in absolute URI");
        }
        if (!f.isEmpty()) {
            r.append('#');
            encoder.encode(FRAGMENT, f.get(), or);
        }
        if (absUri)
            validateScheme((CharBuffer) CharBuffer.wrap(r).limit(s.get().length()));
        final String u = r.toString();
        if (0 <= errIdx)
            throw new QuotedUriSyntaxException(u, errMsg.get(), errIdx);
        return u;
    }

    private StringBuilder resetBuilder() {
        Option<StringBuilder> builder = this.builder;
        if (builder.isEmpty())
            this.builder = builder = Option.some(new StringBuilder());
        else
            builder.get().setLength(0);
        return builder.get();
    }

    /**
     * Checks the given string to conform to the syntax constraints for URI
     * schemes in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
     *
     * @param  scheme the string to validate.
     * @throws URISyntaxException if {@code scheme} does not conform to the
     *         syntax constraints for URI schemes in
     *         <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>.
     */
    public static void validateScheme(final String scheme)
    throws URISyntaxException {
        validateScheme(CharBuffer.wrap(scheme));
    }

    private static void validateScheme(final CharBuffer input)
    throws URISyntaxException {
        if (!input.hasRemaining())
            throw newURISyntaxException(input, "Empty URI scheme");
        char c = input.get();
        // TODO: Character class is no help here - consider table lookup!
        if ((c < 'a' || 'z' < c) && (c < 'A' || 'Z' < c))
            throw newURISyntaxException(input, "Illegal character in URI scheme");
        while (input.hasRemaining()) {
            c = input.get();
            if ((c < 'a' || 'z' < c) && (c < 'A' || 'Z' < c)
                    && (c < '0' || '9' < c)
                    && c != '+' && c != '-' && c != '.')
                throw newURISyntaxException(input, "Illegal character in URI scheme");
        }
    }

    private static URISyntaxException newURISyntaxException(CharBuffer input, String reason) {
        int p = input.position() - 1;
        return new QuotedUriSyntaxException(input.rewind().limit(input.capacity()), reason, p);
    }

    /**
     * Initializes all URI components from the given URI string.
     *
     * @param  uri the URI string.
     * @throws IllegalArgumentException if {@code uri} does not conform to the
     *         syntax constraints of the {@link URI} class.
     * @return {@code this}
     */
    public UriBuilder string(String uri) { return uri(URI.create(uri)); }

    /**
     * Returns a new URI which conforms to the syntax constraints
     * defined in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
     * and its updates in
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
     * for IPv6 addresses.
     *
     * @return A valid URI which is composed from the properties of
     *         this URI builder.
     * @throws IllegalStateException if composing a valid URI is not possible.
     * @see    #buildUri()
     */
    public URI toUri() {
        try {
            return buildUri();
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Returns a new URI which conforms to the syntax constraints
     * defined in
     * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
     * and its updates in
     * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
     * for IPv6 addresses.
     *
     * @return A valid URI which is composed from the properties of
     *         this URI builder.
     * @throws URISyntaxException if composing a valid URI is not possible.
     * @see    #toUri()
     */
    public URI buildUri() throws URISyntaxException {
        final String s = buildString();
        try {
            return new URI(s);
        } catch (URISyntaxException ex) {
            // See http://java.net/jira/browse/TRUEZIP-180
            throw new AssertionError(ex);
        }
    }

    /**
     * Initializes all URI components from the given URI.
     *
     * @param  uri the URI.
     * @return {@code this}
     */
    public UriBuilder uri(URI uri) {
        if (encoder.isRaw()) {
            return scheme(uri.getScheme())
                    .authority(uri.getRawAuthority())
                    .path(uri.isOpaque() ? uri.getRawSchemeSpecificPart() : uri.getRawPath())
                    .query(uri.getRawQuery())
                    .fragment(uri.getRawFragment());
        } else {
            return scheme(uri.getScheme())
                    .authority(uri.getAuthority())
                    .path(uri.isOpaque() ? uri.getSchemeSpecificPart() : uri.getPath())
                    .query(uri.getQuery())
                    .fragment(uri.getFragment());
        }
    }

    /**
     * Sets the nullable URI scheme component.
     *
     * @param  scheme the nullable URI scheme component.
     * @return {@code this}
     */
    public UriBuilder scheme(String scheme) {
        this.scheme = Option.apply(scheme);
        return this;
    }

    /**
     * Sets the nullable URI authority component.
     *
     * @param  authority the nullable URI authority component.
     * @return {@code this}
     */
    public UriBuilder authority(String authority) {
        this.authority = Option.apply(authority);
        return this;
    }

    /**
     * Sets the nullable URI path component.
     *
     * @param  path the nullable URI path component.
     * @return {@code this}
     */
    public UriBuilder path(String path) {
        this.path = Option.apply(path);
        return this;
    }

    /**
     * Sets the nullable URI query component.
     *
     * @param  query the nullable URI query component.
     * @return {@code this}
     */
    public UriBuilder query(String query) {
        this.query = Option.apply(query);
        return this;
    }

    /**
     * Sets the URI path and query components by splitting the given nullable
     * string at the first occurence of the query separator {@code '?'}.
     *
     * @param  pathQuery the nullable combined URI path and query components.
     * @return {@code this}
     */
    public UriBuilder pathQuery(String pathQuery) {
        final Option<String> pathQuery1 = Option.apply(pathQuery);
        final int i;
        if (!pathQuery1.isEmpty() && 0 <= (i = pathQuery1.get().indexOf('?'))) {
            this.path = Option.some(pathQuery1.get().substring(0, i));
            this.query = Option.some(pathQuery1.get().substring(i + 1));
        } else {
            this.path = pathQuery1;
            this.query = Option.none();
        }
        return this;
    }

    /**
     * Sets the nullable URI fragment component.
     *
     * @param  fragment the nullable URI fragment component.
     * @return {@code this}
     */
    public UriBuilder fragment(String fragment) {
        this.fragment = Option.apply(fragment);
        return this;
    }
}
