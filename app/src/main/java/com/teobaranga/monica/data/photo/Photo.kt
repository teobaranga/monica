package com.teobaranga.monica.data.photo

import java.nio.ByteBuffer

data class Photo(
    val fileName: String,
    val data: ByteBuffer,
)
