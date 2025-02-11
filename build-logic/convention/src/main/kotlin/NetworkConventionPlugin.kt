import com.teobaranga.monica.implementation
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class NetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.plugins.kotlinx.serialization.get().pluginId)
            }

            dependencies {
                // Kotlinx Serialization for JSON serialization
                implementation(libs.kotlinx.serialization)
                implementation(libs.retrofit.converter.kotlinx.serialization)

                // Retrofit for REST API
                implementation(libs.retrofit)

                // Sandwich for a nicer way to handle network responses
                implementation(libs.sandwich)
                implementation(libs.sandwich.retrofit)
                implementation(libs.sandwich.retrofit.serialization)
            }
        }
    }
}
