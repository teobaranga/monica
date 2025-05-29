plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))

                // Storage
                implementation(libs.room.runtime)
            }
        }
    }
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
