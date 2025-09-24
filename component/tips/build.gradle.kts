plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(libs.room.runtime)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.component.tips"
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
