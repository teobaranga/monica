plugins {
    alias(libs.plugins.monica.cmp)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.component.user_avatar"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))

                implementation(libs.coil)

                implementation(libs.material.kolor)
            }
        }
    }
}
