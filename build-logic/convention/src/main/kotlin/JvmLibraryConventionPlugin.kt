import com.teobaranga.monica.configureKotlinJvm
import org.gradle.api.Project

@Suppress("unused") // Registered as a plugin in build.gradle.kts
class JvmLibraryConventionPlugin : MonicaPlugin() {

    override fun apply(target: Project) {
        super.apply(target)

        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
            }
            configureKotlinJvm()
        }
    }
}
