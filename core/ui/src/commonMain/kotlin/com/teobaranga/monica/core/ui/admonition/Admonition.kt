package com.teobaranga.monica.core.ui.admonition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.theme.MonicaTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Admonition(
    type: AdmonitionType,
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actions: (@Composable RowScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = type.color,
                shape = RoundedCornerShape(8.dp),
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = type.backgroundColor,
                )
                .padding(
                    start = 12.dp,
                )
                .heightIn(min = 32.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp),
                imageVector = type.icon,
                contentDescription = null,
                tint = type.color,
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 6.dp),
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 6.dp,
                ),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                    ),
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            if (actions != null) {
                Row {
                    actions()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdmonitionInfoPreview() {
    MonicaTheme {
        Admonition(
            type = AdmonitionType.INFO,
            title = "Title",
            description = "This is a description",
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AdmonitionWarningPreview() {
    MonicaTheme {
        Admonition(
            type = AdmonitionType.WARNING,
            title = "Title",
            description = "This is a description",
        )
    }
}
