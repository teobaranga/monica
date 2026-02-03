plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.feature.user"
    }
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:contact-api"))

                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

monica {
    optIn {
        flowPreview = false
    }
}
