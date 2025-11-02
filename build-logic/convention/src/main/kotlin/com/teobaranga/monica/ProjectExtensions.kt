/*
 * Copyright 2023 The Android Open Source Project
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

package com.teobaranga.monica

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.withType

val Project.libs
    get(): LibrariesForLibs = extensions.getByName("libs") as LibrariesForLibs

fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    add("implementation", dependencyNotation)
}

fun DependencyHandlerScope.debugImplementation(dependencyNotation: Any) {
    add("debugImplementation", dependencyNotation)
}

fun DependencyHandlerScope.testImplementation(dependencyNotation: Any) {
    add("testImplementation", dependencyNotation)
}

fun DependencyHandlerScope.coreLibraryDesugaring(dependencyNotation: Any) {
    add("coreLibraryDesugaring", dependencyNotation)
}

fun Project.configureUnitTests() {
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        reports {
            html.required.set(false)
            junitXml.required.set(false)
        }
        systemProperty("gradle.build.dir", project.layout.buildDirectory.asFile.get())

        // JUnit
        // Robolectric will need to keep using JUnit 4 for a while...
        // https://github.com/robolectric/robolectric/issues/3477
        systemProperty("junit.vintage.discovery.issue.reporting.enabled", false)

        // Kotest
        systemProperty("kotest.framework.classpath.scanning.config.disable", true)
        systemProperty("kotest.framework.classpath.scanning.autoscan.disable", true)
        systemProperty("kotest.framework.config.fqn", "com.teobaranga.monica.KotestConfig")
    }

    // https://issuetracker.google.com/issues/411739086?pli=1
    tasks.withType<AbstractTestTask>().configureEach {
        failOnNoDiscoveredTests.set(false)
    }
}
