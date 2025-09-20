package com.teobaranga.monica.core.ui.admonition

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class AdmonitionType(
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color,
) {
    INFO(
        icon = Icons.Outlined.Info,
        color = Color(0xFF00B8D4),
        backgroundColor = Color(0xFF00B8D4).copy(alpha = 0.1f),
    ),
    WARNING(
        icon = Icons.Outlined.WarningAmber,
        color = Color(0xFFFF9100),
        backgroundColor = Color(0xFFFF9100).copy(alpha = 0.1f),
    );
}
