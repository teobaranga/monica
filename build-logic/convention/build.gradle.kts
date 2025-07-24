/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Modifications copyright (C) 2024 Teo Baranga
 */

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.teobaranga.monica.buildlogic"

// Configure the build-logic plugins to target JDK 21
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    // Type-safe libs accessors are not available in the Kotlin source files unless explicitly imported as such.
    // https://stackoverflow.com/a/70878181/8643328
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.jetbrains.compose.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "monica.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "monica.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("kmp") {
            id = "monica.kmp"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("cmp") {
            id = "monica.cmp"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("kotlinInject") {
            id = "monica.kotlin.inject"
            implementationClass = "KotlinInjectConventionPlugin"
        }
        register("network") {
            id = "monica.network"
            implementationClass = "NetworkConventionPlugin"
        }
    }
}
