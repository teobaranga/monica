plugins {
    alias(libs.plugins.monica.cmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.ui"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmlogging)
            }
        }
    }
}
