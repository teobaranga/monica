plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.navigation"
    }
    sourceSets {
        commonMain {
            dependencies {
                api(libs.navigation3.runtime)
                api(libs.kotlinx.serialization)
            }
        }
    }
}
