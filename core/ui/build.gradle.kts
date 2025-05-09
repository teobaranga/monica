plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
}

kotlin {
    androidTarget()
}

android {
    namespace = "com.teobaranga.monica.core.ui"
}
