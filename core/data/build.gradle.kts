plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.kotlinx.serialization)
}

group = "com.teobaranga.monica.core.data"

monica {
    optIn {
        flowPreview = false
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization)

                implementation(libs.kmlogging)

                implementation(libs.paging.common)
            }
        }
    }
}
