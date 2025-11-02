package com.teobaranga.monica.core.ui.topappbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.core.ui.icon.MonicaLogo
import com.teobaranga.monica.core.ui.theme.MonicaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonicaTopAppBar(
    actions: @Composable RowScope.() -> Unit = { },
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = "Monica",
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { }
            ) {
                Image(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = MonicaLogo,
                    contentDescription = null,
                )
            }
        },
        actions = actions,
    )
}

@Composable
fun SearchIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Default.Search),
            contentDescription = "Search",
        )
    }
}

@Preview
@Composable
private fun PreviewMonicaTopAppBar() {
    MonicaTheme {
        MonicaTopAppBar()
    }
}
