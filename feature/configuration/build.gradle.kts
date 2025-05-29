import com.teobaranga.monica.InjectHandler

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.android.library)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    androidTarget()

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))

                implementation(libs.datastore.preferences)
            }
        }
    }
}

android {
    namespace = "com.teobaranga.monica.configuration"
}

monica {
    inject {
        injectIn = InjectHandler.Target.SEPARATE
    }
}
