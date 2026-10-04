// --- Groovy vs Kotlin DSL Migration Notes ---
// In Groovy (.gradle), plugins and repositories were often defined in the root `build.gradle` inside `allprojects {}`.
// In Kotlin (.kts), we define `pluginManagement` and `dependencyResolutionManagement` in `settings.gradle.kts` to centralize this.

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // This forces all modules to use these repositories, overriding any repositories declared in module build files.
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter() // Added back temporarily for legacy dependencies like com.thoughtbot
        // In Groovy: maven { url "https://jitpack.io" }
        // In Kotlin: We must use string quotes and wrap the url in uri()
        maven { url = uri("https://jitpack.io") }
    }
}

// In Groovy: rootProject.name = 'BoxScore'
// In Kotlin: String literals use double quotes ""
rootProject.name = "BoxScore"

// In Groovy: include ':app'
// In Kotlin: Functions require parentheses ()
include(":app")
