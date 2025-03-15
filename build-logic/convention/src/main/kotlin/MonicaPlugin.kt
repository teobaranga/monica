import com.teobaranga.monica.MonicaExtension.Companion.monica
import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class MonicaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            extensions.monica()
        }
    }
}
