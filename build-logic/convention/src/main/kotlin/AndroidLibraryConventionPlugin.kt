/*
 * Copyright 2022 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
/*
 * Modifications copyright (C) 2024 Teo Baranga
 */

import com.android.build.gradle.LibraryExtension
import com.teobaranga.monica.configureKotlinAndroid
import com.teobaranga.monica.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused") // Registered as a plugin in build.gradle.kts
class AndroidLibraryConventionPlugin : MonicaPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {
            with(pluginManager) {
                apply(libs.plugins.androidLibrary.get().pluginId)
                apply(libs.plugins.kotlinAndroid.get().pluginId)
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = libs.versions.targetSdk.get().toInt()
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                // The resource prefix is derived from the module name,
                // so resources inside ":core:module1" must be prefixed with "core_module1_"
                resourcePrefix = path.split("""\W""".toRegex())
                    .drop(1)
                    .distinct()
                    .joinToString(separator = "_")
                    .lowercase() + "_"
            }
            dependencies {
                // Add common library dependencies here
            }
        }
    }
}
