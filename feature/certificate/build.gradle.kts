plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(libs.jetbrains.navigation)
                implementation(libs.kotlinx.serialization)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.certificate"
}
