package com.teobaranga.monica.core.paging

internal actual fun getPagingPlaceholderKey(index: Int): Any = PagingPlaceholderKey(index)

private data class PagingPlaceholderKey(private val index: Int)
