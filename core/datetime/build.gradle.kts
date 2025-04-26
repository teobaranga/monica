plugins {
    alias(libs.plugins.monica.jvm.library)
    alias(libs.plugins.monica.kotlin.inject)
}

group = "com.teobaranga.monica.core.datetime"

monica {
    optIn {
        experimentalCoroutinesApi = false
        flowPreview = false
    }
}

dependencies {
    implementation(project(":core:inject"))

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    api(libs.kotlinx.datetime)
}
