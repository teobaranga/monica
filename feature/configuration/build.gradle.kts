plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.kotlin.inject)
}

android {
    namespace = "com.teobaranga.monica.configuration"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:dispatcher"))
    implementation(project(":core:inject"))

    implementation(libs.activity.compose)

    implementation(libs.datastore.preferences)
}
