plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.component.tips"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(libs.room.runtime)
            }
        }
    }
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
