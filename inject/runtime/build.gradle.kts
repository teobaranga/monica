plugins {
    alias(libs.plugins.monica.jvm.library)
    alias(libs.plugins.compose.compiler)
}

group = "com.teobaranga.monica.inject.runtime"

dependencies {
    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.anvil.runtime)
    implementation(libs.lifecycle.viewmodel.compose)
}
