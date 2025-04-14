plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.android.compose)
}

android {
    namespace = "com.teobaranga.monica.core.ui"
}

dependencies {
    implementation(project(":core:datetime"))

    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.jetbrains.navigation)
}
