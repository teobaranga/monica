package com.teobaranga.monica.setup

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.flow.collectLatest
import monica.app.generated.resources.Res
import monica.app.generated.resources.eb_garamond_regular
import org.jetbrains.compose.resources.Font

@Composable
actual fun logoFontFamily() = FontFamily(
    Font(Res.font.eb_garamond_regular)
)

@Composable
actual fun SetupLaunchEffect(viewModel: SetupViewModel) {
    LaunchedEffect(Unit) {
        viewModel.setupUri
            .collectLatest { url ->
//                val intent = CustomTabsIntent.Builder()
//                    .setShowTitle(true)
//                    .build()
//                intent.launchUrl(context, url.toUri())
            }
    }
}

@Composable
actual fun SetupListenEffect(viewModel: SetupViewModel) {
    DisposableEffect(Unit) {
//        val componentActivity = context as ComponentActivity
//        val listener = Consumer<Intent> { intent ->
//            val code = intent.data?.getQueryParameter(PARAM_CODE)
//            viewModel.onAuthorizationCode(code)
//        }
//        componentActivity.addOnNewIntentListener(listener)
        onDispose {
//            componentActivity.removeOnNewIntentListener(listener)
        }
    }
}

@Composable
internal actual fun SetupInfoButton() {
    IconButton(
        onClick = {
//            val intent = CustomTabsIntent.Builder()
//                .setShowTitle(true)
//                .build()
//            intent.launchUrl(context, SETUP_INFO_URL.toUri())
        },
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "OAuth 2.0 setup information",
        )
    }
}
