plugins {
    alias(libs.plugins.monica.module.test.fixture)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.test"
    }
    sourceSets {
        androidMain {
            dependencies {
                implementation(libs.kotest.extensions.junitxml)
                implementation(libs.kotest.extensions.htmlreporter)
            }
        }
        commonMain {
            dependencies {
                // This module replaces bindings (eg. dispatcher, network) therefore they must be accessible
                // to consumers for ksp to be able to run.
                api(project(":core:dispatcher"))
                api(project(":core:network"))
                api(project(":core:datetime"))

                implementation(libs.kotest.framework.engine)
            }
        }
    }
}
