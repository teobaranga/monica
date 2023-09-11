package com.teobaranga.monica.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.teobaranga.monica.util.compose.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var shouldBeActive by rememberSaveable { mutableStateOf(false) }
    val isImeVisible by keyboardAsState()
    DockedSearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        query = query,
        onQueryChange = {
            query = it
        },
        onSearch = {
            shouldBeActive = false
        },
        active = shouldBeActive && (query.isNotBlank() || isImeVisible),
        onActiveChange = {
            shouldBeActive = it
        },
        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(image = Icons.Default.Search),
                contentDescription = null,
            )
        },
        placeholder = {
            Text(text = "Search")
        }
    ) {
        // TODO
    }
}
