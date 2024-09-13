plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.hilt)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.teobaranga.monica.genders"
}

dependencies {
    implementation(project(":core:dispatcher"))

    // Storage
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
