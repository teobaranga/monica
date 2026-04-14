import com.teobaranga.monica.optInExperimentalCoroutinesApi

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.journal"

        withHostTest {
            isIncludeAndroidResources = true
        }

        androidResources {
            enable = true
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:account"))
                implementation(project(":core:data"))
                implementation(project(":core:datetime"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":core:network"))
                implementation(project(":core:ui"))
                implementation(project(":component:tips"))
                implementation(project(":component:user_avatar"))
                implementation(project(":feature:account"))
                implementation(project(":feature:user-api"))

                // Storage
                implementation(libs.room.runtime)

                implementation(libs.jetbrains.navigation)

                implementation(libs.paging.compose)
            }
        }
    }

    compilerOptions {
        optInExperimentalCoroutinesApi()
    }
}

dependencies {
    kspCommonMainMetadata(libs.room.compiler)
}
