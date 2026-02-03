plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.feature.genders"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":core:network"))

                // Storage
                implementation(libs.room.runtime)
            }
        }
    }
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
