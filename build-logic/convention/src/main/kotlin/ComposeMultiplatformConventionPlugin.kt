
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Compose Multiplatform library.
 */
@Suppress("unused") // Registered as a plugin in build.gradle.kts
class ComposeMultiplatformConventionPlugin : Plugin<Project> {

    @OptIn(ExperimentalComposeLibrary::class)
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

                            implementation(libs.jetbrains.compose.material3)
                            implementation(libs.jetbrains.compose.ui.backhandler)
                            implementation(libs.jetbrains.lifecycle.runtime.compose)
                            implementation(compose.ui)
                            implementation(compose.components.resources)
                            implementation(compose.components.uiToolingPreview)
                            implementation(compose.materialIconsExtended)
                            implementation(compose.runtime)
                            implementation(compose.foundation)

                            implementation(libs.jetbrains.navigation)
                        }
                    }
                    commonTest {
                        dependencies {
                            // Robolectric UI tests
                            implementation(libs.robolectric)
                            implementation(compose.uiTest)
                            // Robolectric only works with JUnit 4 but the regular unit tests run with JUnit 5
                            implementation(libs.junit.vintage)
                        }
                    }
                }
            }
        }
    }
}
