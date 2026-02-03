import com.teobaranga.monica.InjectHandler

plugins {
    alias(libs.plugins.monica.kmp)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
}

kotlin {
    androidLibrary {
        namespace = "com.teobaranga.monica.core.network"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":core:dispatcher"))
                implementation(project(":core:inject"))
                implementation(libs.kmlogging)
            }
        }
    }
}

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
    inject {
        injectIn = InjectHandler.Target.SEPARATE
    }
}
