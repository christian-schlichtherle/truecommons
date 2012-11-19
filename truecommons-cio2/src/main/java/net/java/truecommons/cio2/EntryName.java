package net.java.truecommons.cio2;

import javax.lang.model.element.Name;

/**
 * An immutable and comparable sequence of characters which represents the name
 * of a container entry.
 * When parsed, a name is interpreted as follows:
 * <ol>
 * <li>An entry name is a sequence of <i>segments</i> which are
 *     separated by one or more <i>separator characters</i>, which may be
 *     {@code '/'} or {@code '\\'} or both, according to the rules of the
 *     {@linkplain Container}.
 *     This implies that a segment cannot contain separator characters.
 * <li>An entry name may contain one or more dot ({@code "."}) or
 *     dot-dot ({@code ".."}) segments which represent the current or
 *     parent segment respectively.
 * <li>An entry name may start with one or more separator characters.
 *     In this case, its said to be <i>absolute</i>.
 *     Otherwise, its said to be <i>relative</i>.
 * <li>An entry name may end with one or more separator characters
 *     (e.g. to identify a directory entry).
 *  </ol>
 * For example, the entry names {@code "foo/bar/"} and
 * {@code "./abc/../foo/./def/./../bar/."} both refer to the same entry
 * when being parsed.
 *
 * @since  TrueCommons 2.4
 * @author Christian Schlichtherle
 */
public interface EntryName extends Comparable<EntryName>, Name {
}
