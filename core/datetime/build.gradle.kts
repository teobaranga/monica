plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.datetime"
    }
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

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
