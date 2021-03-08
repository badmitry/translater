import org.gradle.api.JavaVersion

object Config {
    const val application_id = "com.badmitry.translater"
    const val compile_sdk = 30
    const val min_sdk = 21
    const val target_sdk = 30
    val java_version = JavaVersion.VERSION_1_8
}

object Releases {
    const val version_code = 1
    const val version_name = "1.0"
}

object Modules {
    const val app = ":app"
    const val data = ":data"
    const val repository = ":repository"
}
