plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.metro)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.core.inject"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.metrox.viewmodel)
                implementation(libs.metrox.viewmodel.compose)
            }
        }
    }
}
