plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.data"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)

                implementation(libs.paging.common)
            }
        }
    }
}

monica {
    optIn {
        flowPreview = false
    }
}
