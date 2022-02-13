import org.gradle.kotlin.dsl.DependencyHandlerScope

const val composeCompilerVersion = "1.2.0-alpha02"

fun DependencyHandlerScope.impl() {

    with(composeCompilerVersion) {
        listOf(

            //Compose
            "androidx.compose.compiler:compiler:$this",
            "androidx.compose.foundation:foundation:$this",
            "androidx.compose.material:material:$this",
            "androidx.compose.material:material-icons-core:$this",
            "androidx.compose.material:material-icons-extended:$this",
            "androidx.activity:activity-compose:1.4.0",

            //Base
            "androidx.core:core-ktx:1.7.0"
        ).forEach { addD(dep = it) }
    }
}

val listPlugins = listOf("com.android.application", "org.jetbrains.kotlin.android")

private fun DependencyHandlerScope.addD(method: String = imp, dep: String) = add(method, dep)

private val imp get() = "implementation"




