plugins {
    alias(libs.plugins.monica.jvm.library)
    alias(libs.plugins.monica.kotlin.inject)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
