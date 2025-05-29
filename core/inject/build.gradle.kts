plugins {
    alias(libs.plugins.monica.kmp)
}

group = "com.teobaranga.monica.core.inject"

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
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
