
import com.teobaranga.monica.implementation
import com.teobaranga.monica.ksp
import com.teobaranga.monica.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class NetworkConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                // Moshi for JSON serialization
                implementation(libs.moshi)
                implementation(libs.moshi.adapters)
                implementation(libs.moshi.converter)
                ksp(libs.moshi.kotlin.codegen)

                // Retrofit for REST API
                implementation(libs.retrofit)

                // Sandwich for a nicer way to handle network responses
                implementation(libs.sandwich)
                implementation(libs.sandwich.retrofit)
            }
        }
    }
}
