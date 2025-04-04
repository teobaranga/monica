plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.kotlin.inject)
}

android {
    namespace = "com.teobaranga.monica.account"
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:dispatcher"))
    implementation(project(":core:inject"))

    implementation(libs.activity.compose)

    implementation(libs.datastore.preferences)

    implementation(libs.room.runtime)
}
