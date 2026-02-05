import com.teobaranga.monica.InjectHandler

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.configuration"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:ui"))
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(project(":component:tips"))

                implementation(libs.datastore.preferences)
            }
        }
    }
}

monica {
    inject {
        injectIn.set(InjectHandler.Target.SEPARATE)
    }
}
