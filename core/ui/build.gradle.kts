plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.cmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmlogging)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.core.ui"
}
