plugins {
    alias(libs.plugins.monica.android.feature)
}

android {
    namespace = "com.teobaranga.monica.user"
}

monica {
    optIn {
        flowPreview = false
    }
}

dependencies {
    api(project(":feature:contact-api"))

    implementation(libs.kotlinx.coroutines.core)
}
