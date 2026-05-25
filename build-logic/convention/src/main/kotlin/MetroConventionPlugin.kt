import com.teobaranga.monica.libs
import dev.zacsweers.metro.gradle.MetroPluginExtension
import dev.zacsweers.metro.gradle.RequiresIdeSupport
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
                            implementation(libs.metrox.viewmodel)
                            implementation(libs.metrox.viewmodel.compose)
                        }
                    }
                }
            }
            extensions.configure<MetroPluginExtension> {
                @OptIn(RequiresIdeSupport::class)
                generateAssistedFactories.set(true)
            }
        }
    }
}
