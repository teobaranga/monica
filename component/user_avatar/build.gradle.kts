plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.android.compose)
}

android {
    namespace = "com.teobaranga.monica.component.user_avatar"
}

dependencies {
    implementation(project(":core:ui"))

    implementation(libs.coil.compose)

    implementation(libs.material.kolor)
}
