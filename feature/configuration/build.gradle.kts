import com.teobaranga.monica.InjectHandler
import com.teobaranga.monica.optInExperimentalCoroutinesApi

plugins {
    alias(libs.plugins.monica.cmp)
    alias(libs.plugins.monica.kotlin.inject)
}

kotlin {
    android {
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
    compilerOptions {
        optInExperimentalCoroutinesApi()
    }
}

monica {
    inject {
        injectIn.set(InjectHandler.Target.SEPARATE)
    }
}
