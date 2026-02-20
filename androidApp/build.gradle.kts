import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.teobaranga.monica.libs

plugins {
    alias(libs.plugins.monica.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sentry.android)
}

android {
    namespace = "com.teobaranga.monica"

    defaultConfig {
        applicationId = "com.teobaranga.monica"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val localProperties = gradleLocalProperties(rootDir, providers)
            storeFile = file("release.jks")
            storePassword = localProperties["RELEASE_STORE_PASSWORD"] as String?
            keyAlias = localProperties["RELEASE_KEY_ALIAS"] as String?
            keyPassword = localProperties["RELEASE_KEY_PASSWORD"] as String?
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    testOptions.unitTests {
        isIncludeAndroidResources = true
        all { test ->
            test.systemProperties["robolectric.logging.enabled"] = "true"
        }
    }
}

dependencies {
    implementation(project(":app"))

    implementation(project(":core:account"))
    implementation(project(":core:data"))
    implementation(project(":core:datetime"))
    implementation(project(":core:dispatcher"))
    implementation(project(":core:inject"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))

    implementation(project(":component:tips"))
    implementation(project(":component:user_avatar"))

    implementation(project(":feature:account"))
    implementation(project(":feature:certificate"))
    implementation(project(":feature:configuration"))
    implementation(project(":feature:contact-api"))
    implementation(project(":feature:genders"))
    implementation(project(":feature:journal"))
    implementation(project(":feature:user-api"))

    implementation(libs.datastore.preferences)
    implementation(libs.browser)
    implementation(libs.coil)
    implementation(libs.kmlogging)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.work)
    implementation(libs.room.runtime)
    implementation(libs.kotlinx.datetime)
    implementation(libs.jetbrains.navigation)
    implementation(libs.paging.common)

    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.anvil.runtime)
    implementation(libs.kotlin.inject.anvil.runtime.optional)
    implementation(libs.kotlin.inject.viewmodel.runtime)
    implementation(libs.kotlin.inject.viewmodel.runtime.compose)
    ksp(libs.kotlin.inject.compiler)
    ksp(libs.kotlin.inject.anvil.compiler)
    ksp(libs.kotlin.inject.viewmodel.compiler)
}

sentry {
    autoInstallation {
        // Installed by the KMP plugin instead
        enabled = false
    }

    // this will upload your source code to Sentry to show it as part of the stack traces
    // disable if you don't want to expose your sources
    includeSourceContext = true

    ignoredBuildTypes = listOf("debug")
}
