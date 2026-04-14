plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.datetime"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:inject"))

                implementation(libs.jetbrains.compose.runtime)
                api(libs.kotlinx.datetime)

                implementation(libs.kotlinx.serialization)
            }
        }
    }
}
