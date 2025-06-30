package com.teobaranga.monica.core.network.util

import kotlinx.io.bytestring.ByteString
import java.security.MessageDigest

actual fun ByteString.sha256(): ByteString {
    return MessageDigest.getInstance("SHA-256").digest(toByteArray()).toByteString()
}

actual fun ByteString.sha1(): ByteString {
    return MessageDigest.getInstance("SHA-1").digest(toByteArray()).toByteString()
}
