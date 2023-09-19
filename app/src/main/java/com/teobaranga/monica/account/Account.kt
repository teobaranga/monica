package com.teobaranga.monica.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.spec.DestinationStyleBottomSheet
import com.teobaranga.monica.ui.PreviewPixel4
import com.teobaranga.monica.ui.theme.MonicaTheme

@Destination(style = DestinationStyleBottomSheet::class)
@Composable
fun ColumnScope.Account() {
    val viewModel = hiltViewModel<AccountViewModel>()
    AccountScreen(
        onClearAuthorization = viewModel::onClearAuthorization,
    )
}

@Composable
fun ColumnScope.AccountScreen(
    onClearAuthorization: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .clip(ShapeDefaults.Large)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Button(
            onClick = onClearAuthorization,
        ) {
            Text(text = "Sign out")
        }
    }
    Spacer(
        modifier = Modifier
            .weight(1f),
    )
}

@PreviewPixel4
@Composable
fun PreviewAccountScreen() {
    MonicaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            AccountScreen(
                onClearAuthorization = { },
            )
        }
    }
}
