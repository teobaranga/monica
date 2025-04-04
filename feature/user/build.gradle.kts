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
    implementation(project(":feature:user-api"))

    implementation(libs.kotlinx.coroutines.core)
}
