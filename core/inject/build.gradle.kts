plugins {
    alias(libs.plugins.monica.kmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.inject"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.inject.viewmodel.runtime.compose)
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
