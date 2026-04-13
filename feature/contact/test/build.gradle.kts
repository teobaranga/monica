plugins {
    alias(libs.plugins.monica.module.test.fixture)
}

kotlin {
    android {
        namespace = "com.teobaranga.monica.contact.test"
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":feature:contact:data"))
            }
        }
    }
}
