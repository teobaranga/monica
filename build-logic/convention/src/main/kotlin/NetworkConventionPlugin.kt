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
                // Ktor for networking
                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.ktor.serialization.kotlinx.json)

                // Kotlinx Serialization for JSON serialization
                implementation(libs.kotlinx.serialization)

                // Sandwich for a nicer way to handle network responses
                implementation(libs.sandwich)
                implementation(libs.sandwich.ktor)
            }
        }
    }
}
