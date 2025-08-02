package com.teobaranga.monica.certificate.util

/**
 * Standard hex format across the app used for display purposes.
 * Uses all caps and colons as separators.
 */
val hexFormatDisplay = HexFormat {
    upperCase = true
    bytes {
        byteSeparator = ":"
    }
}
