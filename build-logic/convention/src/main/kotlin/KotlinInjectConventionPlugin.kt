import com.teobaranga.monica.implementation
import com.teobaranga.monica.ksp
import com.teobaranga.monica.kspTest
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class KotlinInjectConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(libs.plugins.ksp.get().pluginId)
            dependencies {
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kimchi.annotations)
                implementation(libs.kotlin.inject.anvil.runtime.optional)
                ksp(libs.kotlin.inject.compiler)
                ksp(libs.kimchi.compiler)
                kspTest(libs.kotlin.inject.compiler)
                kspTest(libs.kotlin.inject.anvil.compiler)
            }
        }
    }
}
