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

import com.android.build.api.dsl.ApplicationExtension
import com.teobaranga.monica.configureUnitTests
import com.teobaranga.monica.coreLibraryDesugaring
import com.teobaranga.monica.libs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

@Suppress("unused") // Registered as a plugin in build.gradle.kts
class AndroidApplicationConventionPlugin : MonicaPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {
            with(pluginManager) {
                apply(libs.plugins.androidApplication.get().pluginId)
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.versions.compileSdk.get().toInt()

                defaultConfig {
                    minSdk = libs.versions.minSdk.get().toInt()
                    targetSdk = libs.versions.targetSdk.get().toInt()
                }

                compileOptions {
                    // Java 11+ APIs available through desugaring
                    // https://developer.android.com/studio/write/java11-default-support-table
                    sourceCompatibility = JavaVersion.VERSION_21
                    targetCompatibility = JavaVersion.VERSION_21
                    isCoreLibraryDesugaringEnabled = true
                }


                dependencies {
                    coreLibraryDesugaring(libs.desugar.jdk.libs)
                }

                testOptions.animationsDisabled = true
            }

            configureUnitTests()
        }
    }
}
