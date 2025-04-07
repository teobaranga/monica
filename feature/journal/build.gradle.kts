import com.teobaranga.monica.implementation

plugins {
    alias(libs.plugins.monica.android.feature)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.teobaranga.monica.journal"
}

dependencies {
    implementation(project(":core:account"))
    implementation(project(":core:data"))
    implementation(project(":core:dispatcher"))
    implementation(project(":core:inject"))
    implementation(project(":core:ui"))
    implementation(project(":component:user_avatar"))
    implementation(project(":feature:account"))
    implementation(project(":feature:user-api"))

    // Storage
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.timber)

    implementation(libs.paging.compose)

    implementation(libs.jetbrains.navigation)
}
