plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

group = "com.teobaranga.monica.core.datetime"

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
                implementation(project(":core:inject"))

                implementation(libs.compose.runtime)
                api(libs.kotlinx.datetime)
            }
        }
    }
}
