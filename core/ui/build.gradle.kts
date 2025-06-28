plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
}

kotlin {
    androidTarget()

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
