plugins {
    alias(libs.plugins.monica.jvm.library)
    alias(libs.plugins.monica.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
