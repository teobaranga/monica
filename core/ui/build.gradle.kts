plugins {
    alias(libs.plugins.monica.cmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.ui"

        androidResources {
            enable = true
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kmlogging)
            }
        }
    }
}
