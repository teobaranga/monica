package com.teobaranga.monica.contacts

import ContactsNavGraph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination

@ContactsNavGraph(start = true)
@Destination
@Composable
fun Contacts() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
    )
}
