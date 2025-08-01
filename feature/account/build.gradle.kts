plugins {
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
}

kotlin {

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":feature:certificate"))

                implementation(libs.datastore.preferences)

                implementation(libs.room.runtime)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.account"
}
