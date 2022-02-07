import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.support.delegates.ProjectDelegate
import java.io.File

private fun RepositoryHandler.repository() {
    google()
    mavenCentral()
    listOf("https://jitpack.io", "https://maven.google.com").forEach { maven(it) }
}

fun builder(value: Any, file: File? = null) {
    when (value) {
        is ScriptHandler -> {
            value.repositories.repository()
            value.dependencies.apply {
                listOf(
                    "com.android.tools.build:gradle:7.1.0",
                    "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10"
                ).forEach { add(ScriptHandler.CLASSPATH_CONFIGURATION, it) }
            }
        }

        is Project -> value.repositories.repository()
        is ProjectDelegate -> value.tasks.register("type", Delete::class) { delete(file) }
    }
}










