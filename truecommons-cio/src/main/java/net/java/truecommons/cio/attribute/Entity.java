/*
 * Copyright (C) 2005-2012 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package net.java.truecommons.cio.attribute;

/**
 * Attribute keys for permissions and principals.
 * The enum names are a subset of the identifiers defined in RFC 3530,
 * section 5.11.4 "ACE who".
 *
 * @see    <a href="http://www.ietf.org/rfc/rfc3530.txt">RFC 3530: Network File System (NFS) version 4 Protocol</a>
 * @see    WithPermissions
 * @see    WithPrincipals
 * @since  TrueCommons 3.0
 * @author Christian Schlichtherle
 */
public enum Entity { OWNER, GROUP, EVERYONE }
