plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:inject"))

                implementation(libs.jetbrains.compose.runtime)
                api(libs.kotlinx.datetime)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.core.datetime"
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
