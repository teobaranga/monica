plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":feature:contact-api"))

                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.feature.user"
}

monica {
    optIn {
        flowPreview = false
    }
}
