plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.activity.nav"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization)
            }
        }
    }
}
