package com.teobaranga.monica.core.ui.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
)
@Preview(
    device = Devices.PIXEL_4_XL,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class PreviewPixel4
