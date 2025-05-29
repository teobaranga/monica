package com.teobaranga.monica.setup

import android.content.Intent
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.core.net.toUri
import androidx.core.util.Consumer
import com.teobaranga.monica.data.PARAM_CODE
import kotlinx.coroutines.flow.collectLatest
import monica.app.generated.resources.Res
import monica.app.generated.resources.eb_garamond_regular
import monica.app.generated.resources.eb_garamond_variable
import org.jetbrains.compose.resources.Font

@Composable
actual fun logoFontFamily() = FontFamily(
    Font(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Res.font.eb_garamond_variable
        } else {
            Res.font.eb_garamond_regular
        },
    )
)

@Composable
actual fun SetupLaunchEffect(viewModel: SetupViewModel) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.setupUri
            .collectLatest { url ->
                val intent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()
                intent.launchUrl(context, url.toUri())
            }
    }
}

@Composable
actual fun SetupListenEffect(viewModel: SetupViewModel) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val componentActivity = context as ComponentActivity
        val listener = Consumer<Intent> { intent ->
            val code = intent.data?.getQueryParameter(PARAM_CODE)
            viewModel.onAuthorizationCode(code)
        }
        componentActivity.addOnNewIntentListener(listener)
        onDispose {
            componentActivity.removeOnNewIntentListener(listener)
        }
    }
}

@Composable
internal actual fun SetupInfoButton() {
    val context = LocalContext.current
    IconButton(
        onClick = {
            val intent = CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build()
            intent.launchUrl(context, SETUP_INFO_URL.toUri())
        },
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "OAuth 2.0 setup information",
        )
    }
}
