package com.teobaranga.monica.core.ui.admonition

data class AdmonitionState(
    val type: AdmonitionType,
    val title: String,
    val description: String,
    val dismissAction: DismissAction?,
) {
    data class DismissAction(
        val label: String,
        val onDismiss: () -> Unit,
    )
}
