import org.gradle.kotlin.dsl.DependencyHandlerScope

const val composeCompilerVersion = "1.2.0-alpha02"

fun DependencyHandlerScope.impl() {

    //Compose
    with("1.2.0-alpha02") {
        listOf(
            "androidx.compose.compiler:compiler:$this",
            "androidx.compose.foundation:foundation:$this",
            "androidx.compose.material:material:$this",
            "androidx.compose.material:material-icons-core:$this",
            "androidx.compose.material:material-icons-extended:$this",
            "androidx.activity:activity-compose:1.4.0"
        ).forEach { addD(dep = it) }
    }

    //Base
    addD(dep = "androidx.core:core-ktx:1.7.0")
}

val listPlugins = listOf(
    "com.android.application",
    "org.jetbrains.kotlin.android",
    "kotlin-kapt"
)

private fun DependencyHandlerScope.addD(method: String = imp, dep: String) = add(method, dep)
private val imp get() = "implementation"
private val kapt get() = "kapt"



