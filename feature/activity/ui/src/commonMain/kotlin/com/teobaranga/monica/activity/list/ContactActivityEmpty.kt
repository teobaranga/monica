package com.teobaranga.monica.activity.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.activity.list.icon.CalendarDayBlank
import com.teobaranga.monica.core.ui.theme.MonicaTheme

@Composable
internal fun ContactActivityEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            modifier = Modifier
                .sizeIn(maxWidth = 190.dp),
            painter = rememberVectorPainter(CalendarDayBlank),
            contentDescription = null,
        )
        Text(
            modifier = Modifier
                .padding(top = 40.dp),
            text = "No activities yet",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun PreviewContactActivityEmpty() {
    MonicaTheme {
        ContactActivityEmpty()
    }
}
