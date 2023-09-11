package com.teobaranga.monica.journal

import JournalNavGraph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@JournalNavGraph(start = true)
@Destination
@Composable
fun Journal() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    )
}
