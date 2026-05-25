
import com.teobaranga.monica.libs
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

@Suppress("unused")
class MetroConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.metro.get().pluginId)
            extensions.configure<KotlinMultiplatformExtension> {
                with(sourceSets) {
                    commonMain {
                        dependencies {
                            implementation("dev.zacsweers.metro:metrox-viewmodel:1.1.1")
                            implementation("dev.zacsweers.metro:metrox-viewmodel-compose:1.1.1")
                        }
                    }
                }
            }
            extensions.configure<MetroPluginExtension> {
                generateAssistedFactories.set(true)
                debug.set(true)
                reportsDestination.set(layout.buildDirectory.dir("metro/reports"))
            }
        }
    }
}
