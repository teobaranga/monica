package com.teobaranga.monica.certificate.util

import kotlinx.io.bytestring.ByteString

expect fun ByteString.sha256(): ByteString

expect fun ByteString.sha1(): ByteString

fun ByteArray.toByteString(): ByteString {
    return ByteString(this)
}
