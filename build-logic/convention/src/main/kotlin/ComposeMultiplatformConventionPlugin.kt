import com.teobaranga.monica.debugImplementation
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Compose Multiplatform library.
 */
@Suppress("unused") // Registered as a plugin in build.gradle.kts
class ComposeMultiplatformConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.monica.kmp.get().pluginId)
                apply(libs.plugins.compose.compiler.get().pluginId)
                apply(libs.plugins.jetbrains.compose.get().pluginId)
            }

            configure<KotlinMultiplatformExtension> {
                val compose = extensions.getByType<ComposePlugin.Dependencies>()

                with(sourceSets) {
                    commonMain {
                        dependencies {
                            implementation(project(":core:datetime"))

                            implementation(libs.jetbrains.compose.components.resources)
                            implementation(libs.jetbrains.compose.foundation)
                            implementation(libs.jetbrains.compose.material3)
                            implementation(libs.jetbrains.compose.material.icons.extended)
                            implementation(libs.jetbrains.compose.runtime)
                            implementation(libs.jetbrains.compose.ui)
                            implementation(libs.jetbrains.compose.ui.backhandler)
                            implementation(libs.jetbrains.compose.ui.tooling.preview)
                            implementation(libs.jetbrains.navigation)
                        }
                    }
                    afterEvaluate {
                        findByName("androidUnitTest")?.run {
                            dependencies {
                                // Robolectric UI tests
                                implementation(libs.robolectric)
                                implementation(libs.jetbrains.compose.ui.test)
                                // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
                                implementation(libs.junit.vintage)
                            }
                            project.dependencies {
                                debugImplementation(libs.compose.ui.test.manifest)
                            }
                        }
                    }
                }

                afterEvaluate {
                    if (sourceSets.findByName("androidMain") != null) {
                        target.dependencies {
                            debugImplementation(libs.jetbrains.compose.ui.tooling)
                        }
                    }
                }
            }
        }
    }
}
