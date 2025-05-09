package com.teobaranga.monica.util.compose

import androidx.compose.ui.Modifier

fun Modifier.thenIf(predicate: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (predicate) {
        modifier()
    } else {
        this
    }
}
