plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.hilt)
}

android {
    namespace = "com.teobaranga.monica.configuration"
}

dependencies {
    implementation(project(":core:ui"))

    implementation(libs.activity.compose)

    implementation(libs.datastore.preferences)

    implementation(libs.hilt.navigation.compose)
}
