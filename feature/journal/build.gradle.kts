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
                implementation(project(":core:account"))
                implementation(project(":core:data"))
                implementation(project(":core:datetime"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":core:paging"))
                implementation(project(":core:ui"))
                implementation(project(":component:user_avatar"))
                implementation(project(":feature:account"))
                implementation(project(":feature:user-api"))

                // Storage
                implementation(libs.room.runtime)

                implementation(libs.kmlogging)

                implementation(libs.jetbrains.navigation)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.journal"
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
