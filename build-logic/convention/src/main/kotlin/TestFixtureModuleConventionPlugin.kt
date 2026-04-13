import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * These modules are not meant to be used in production code.
 * They exist only as a workaround until there is proper test fixture support in Kotlin Multiplatform.
 *
 * **Issue Tracker**: [Add support for Gradle Test Fixtures in non-JVM platforms](https://youtrack.jetbrains.com/issue/KT-63142)
 */
@Suppress("unused")
class TestFixtureModuleConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.monica.kmp.get().pluginId)
                apply(libs.plugins.monica.kotlin.inject.get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    androidMain {
                        dependencies {
                            implementation(libs.mockk)
                        }
                    }
                    commonMain {
                        dependencies {
                            // Test fixture modules will most likely require coroutines and serialization
                            implementation(libs.kotlinx.coroutines.core)
                            implementation(libs.kotlinx.coroutines.test)
                            implementation(libs.kotlinx.serialization)
                        }
                    }
                }
            }
        }
    }
}
