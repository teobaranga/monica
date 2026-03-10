import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class DataModuleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.monica.kmp.get().pluginId)
                // Used to contribute API classes to the DI graph
                apply(libs.plugins.monica.kotlin.inject.get().pluginId)
                apply(libs.plugins.monica.network.get().pluginId)
            }

            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    commonMain {
                        dependencies {
                            // Room because data modules have both remote and local data
                            implementation(libs.room.runtime)

                            implementation(project(":core:data"))
                            implementation(project(":core:dispatcher"))
                            implementation(project(":core:datetime"))
                            implementation(project(":core:network"))
                        }
                    }
                }
            }

            dependencies {
                add("kspCommonMainMetadata", libs.room.compiler)
            }
        }
    }
}
