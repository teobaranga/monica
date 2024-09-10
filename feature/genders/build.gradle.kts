plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.teobaranga.monica.genders"
}

dependencies {
    implementation(project(":core:dispatcher"))

    // Network
    implementation(libs.moshi)
    implementation(libs.moshi.adapters)
    implementation(libs.moshi.converter)
    ksp(libs.moshi.kotlin.codegen)

    implementation(libs.retrofit)

    implementation(libs.sandwich)
    implementation(libs.sandwich.retrofit)

    // Storage
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
}
