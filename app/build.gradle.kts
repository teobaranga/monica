import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.monica.android.application)
    alias(libs.plugins.monica.android.compose)
    alias(libs.plugins.monica.kotlin.inject)
    alias(libs.plugins.monica.network)
    alias(libs.plugins.ksp)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
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

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.generateKotlin", "true")
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
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:dispatcher"))
    implementation(project(":feature:configuration"))
    implementation(project(":feature:genders"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.activity.compose)
    implementation(libs.androidx.compose.material.icons)

    implementation(libs.compose.placeholder)

    implementation(libs.browser)

    implementation(libs.coil)

    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    implementation(libs.datastore.preferences)

    implementation(libs.material.kolor)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    debugImplementation(libs.okhttp.logging.interceptor)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.timber)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)

    implementation(libs.work)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.turbine)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    ktlintRuleset(libs.compose.rules.ktlint)
}

ktlint {
    android = true
    verbose = true
    version = "1.3.1"
    reporters {
        reporter(ReporterType.HTML)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
