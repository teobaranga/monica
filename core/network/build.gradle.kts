plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.metro)
    alias(libs.plugins.monica.network)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.network"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
            }
        }
    }
}
