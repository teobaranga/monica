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

private val DefaultShape = RoundedCornerShape(8.dp)

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
                shape = DefaultShape,
            )
            .clip(DefaultShape)
    ) {
        TitleRow(
            modifier = Modifier
                .fillMaxWidth(),
            type = type,
            title = title,
        )
        BodyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            description = description,
            actions = actions,
        )
    }
}

@Composable
private fun TitleRow(
    type: AdmonitionType,
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(color = type.backgroundColor)
            .padding(start = 12.dp)
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
}

@Composable
private fun BodyColumn(
    description: String,
    actions: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
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
