package com.teobaranga.monica.core.ui.admonition

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

private val admonitionColorInfo = Color(0xFF00B8D4)
private val admonitionColorWarning = Color(0xFFFF9100)

enum class AdmonitionType(
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color,
) {
    INFO(
        icon = Icons.Outlined.Info,
        color = admonitionColorInfo,
        backgroundColor = admonitionColorInfo.copy(alpha = 0.1f),
    ),
    WARNING(
        icon = Icons.Outlined.WarningAmber,
        color = admonitionColorWarning,
        backgroundColor = admonitionColorWarning.copy(alpha = 0.1f),
    );
}
