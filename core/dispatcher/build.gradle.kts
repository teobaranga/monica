plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.dispatcher"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
