plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.inject.viewmodel.runtime.compose)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.core.inject"
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}
