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

import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.teobaranga.monica.buildlogic"

// Configure the build-logic plugins to target JDK 25
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
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
    compileOnly(libs.metro.gradle.plugin)
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
        register("kmp") {
            id = "monica.kmp"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("cmp") {
            id = "monica.cmp"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("metro") {
            id = "monica.metro"
            implementationClass = "MetroConventionPlugin"
        }
        register("network") {
            id = "monica.network"
            implementationClass = "NetworkConventionPlugin"
        }
        register("data-module") {
            id = "monica.module.data"
            implementationClass = "DataModuleConventionPlugin"
        }
        register("test-fixture-module") {
            id = "monica.module.test.fixture"
            implementationClass = "TestFixtureModuleConventionPlugin"
        }
    }
}
