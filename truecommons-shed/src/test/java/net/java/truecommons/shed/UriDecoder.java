/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.shed;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;

import static java.nio.charset.CoderResult.OVERFLOW;
import static java.nio.charset.CoderResult.UNDERFLOW;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Unescapes characters in URI components according to
 * <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC&nbsp;2396</a>
 * and its updates in
 * <a href="http://www.ietf.org/rfc/rfc2732.txt">RFC&nbsp;2732</a>
 * for IPv6 addresses.
 *
 * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">
 *      RFC&nbsp;2396: Uniform Resource Identifiers (URI): Generic Syntax</a>
 * @see <a href="http://www.ietf.org/rfc/rfc2732.txt">
 *      RFC&nbsp;2732: Format for Literal IPv6 Addresses in URL's</a>
 * @see UriEncoder
 * @author Christian Schlichtherle
 */
@SuppressWarnings("LoopStatementThatDoesntLoop")
final class UriDecoder {

    private final CharsetDecoder decoder;

    private Option<StringBuilder> stringBuilder = Option.none();

    /**
     * Constructs a new URI decoder which uses the UTF-8 character set to
     * decode non-US-ASCII characters.
     */
    UriDecoder() { this(Option.<Charset>none()); }

    /**
     * Constructs a new URI decoder which uses the given character set to
     * decode non-US-ASCII characters.
     * 
     * @param charset the character set to use for encoding non-US-ASCII
     *        characters.
     *        If this parameter is {@code null}, then it defaults to
     *        {@code UTF-8}.
     *        Note that providing any other value than {@code null} or
     *        {@code UTF-8} will void interoperability with most applications.
     */
    UriDecoder(Option<Charset> charset) {
        if (charset.isEmpty())
            charset = Option.some(UTF_8);
        this.decoder = charset.get().newDecoder();
    }

    /**
     * Decodes all escape sequences in the string {@code eS}, that is,
     * each occurence of "%<i>XX</i>", where <i>X</i> is a hexadecimal digit,
     * gets substituted with the corresponding single byte and the resulting
     * string gets decoded using the character set provided to the constructor.
     * 
     * @param  es the encoded string to decode.
     * @return The decoded string.
     * @throws IllegalArgumentException on any decoding error with a
     *         {@link URISyntaxException} as its
     *         {@link IllegalArgumentException#getCause() cause}.
     */
    String decode(String es) {
        try {
            for (StringBuilder dsb : decode(es, Option.<StringBuilder>none()))
                return dsb.toString();
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
        return es;
    }

    /**
     * Decodes all escape sequences in the string {@code eS}, that is,
     * each occurence of "%<i>XX</i>", where <i>X</i> is a hexadecimal digit,
     * gets substituted with the corresponding single byte and the resulting
     * string gets decoded to the string builder {@code dS} using the character
     * set provided to the constructor.
     * 
     * @param  es the encoded string to decode.
     * @param  dsb the optional string builder to which all decoded characters
     *             shall get appended.
     * @return If {@code es} contains no escape sequences, then {@code null}
     *         gets returned.
     *         Otherwise, if {@code dsb} is not {@code null}, then it gets
     *         returned with all decoded characters appended to it.
     *         Otherwise, a temporary string builder gets returned which solely
     *         contains all decoded characters.
     *         This temporary string builder may get cleared and reused upon
     *         the next call to <em>any</em> method of this object.
     * @throws URISyntaxException on any decoding error.
     *         This exception will leave {@code dsb} in an undefined state.
     */
    Option<StringBuilder> decode(
            final String es,
            Option<StringBuilder> dsb)
    throws URISyntaxException {
        final CharBuffer ecb = CharBuffer.wrap(es);  // encoded character buffer
        Option<ByteBuffer> ebb = Option.none();      // encoded byte buffer
        Option<CharBuffer> dcb = Option.none();      // decoded character buffer
        while (true) {
            ecb.mark();
            final int ec = ecb.hasRemaining() ? ecb.get() : -1; // encoded char is unsigned!
            if ('%' == ec) {
                if (ebb.isEmpty()) {
                    if (dsb.isEmpty()) {
                        if ((dsb = stringBuilder).isEmpty())
                            dsb = stringBuilder = Option.some(new StringBuilder());
                        else
                            dsb.get().setLength(0);
                        dsb.get().append(es, 0, ecb.position() - 1); // prefix until current character
                    }
                    int l = ecb.remaining();
                    l = (l + 1) / 3;
                    ebb = Option.some(ByteBuffer.allocate(l));
                    dcb = Option.some(CharBuffer.allocate(l));
                }
                final int eb = dequote(ecb);         // encoded byte
                if (eb < 0)
                    throw new URISyntaxException(es, "illegal escape sequence", ecb.reset().position());
                ebb.get().put((byte) eb);
            } else {
                if (!ebb.isEmpty() && 0 < ebb.get().position()) {
                    ebb.get().flip();
                    { // Decode ebb -> dcb.
                        CoderResult cr;
                        if (UNDERFLOW != (cr = decoder.reset().decode(ebb.get(), dcb.get(), true))
                                || UNDERFLOW != (cr = decoder.flush(dcb.get()))) {
                            assert OVERFLOW != cr;
                            throw new QuotedUriSyntaxException(es, cr.toString());
                        }
                    }
                    ebb.get().clear();
                    dcb.get().flip();
                    dsb.get().append(dcb.get());
                    dcb.get().clear();
                }
                if (0 > ec)
                    break;
                if (!dsb.isEmpty())
                    dsb.get().append((char) ec);
            }
        }
        return ebb.isEmpty() ? Option.<StringBuilder>none() : dsb;
    }

    private static int dequote(final CharBuffer ecb) {
        if (ecb.hasRemaining()) {
            final char ec0 = ecb.get();
            if (ecb.hasRemaining()) {
                final char ec1 = ecb.get();
                return (dequote(ec0) << 4) | dequote(ec1);
            }
        }
        return -1;
    }

    private static int dequote(char ec) {
        if ('0' <= ec && ec <= '9')
            return ec - '0';
        ec &= ~(2 << 4); // toUpperCase for 'a' to 'z'
        if ('A' <= ec && ec <= 'F')
            return ec - 'A' + 10;
        return -1;
    }
}