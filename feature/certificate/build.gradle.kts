import com.teobaranga.monica.InjectHandler

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.certificate"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:inject"))
                implementation(project(":core:network"))
                implementation(project(":core:ui"))
                implementation(libs.jetbrains.navigation)
                implementation(libs.kotlinx.serialization)
                implementation(libs.kmlogging)
                implementation(libs.okio)
                implementation(libs.signum.indispensable)
            }
        }
    }
}

monica {
    inject {
        injectIn = InjectHandler.Target.SEPARATE
    }
}
