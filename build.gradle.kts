// Top-level build file

// --- Groovy vs Kotlin DSL Migration Notes ---
// In Groovy (.gradle), we used a `buildscript` block with `dependencies { classpath ... }`
// In Kotlin (.kts) with Version Catalogs, we use the `plugins` block to declare them cleanly, 
// pulling the version directly from `gradle/libs.versions.toml`.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
}

// In Groovy, defining a task was:
// task clean(type: Delete) { delete rootProject.buildDir }
// In Kotlin, tasks are strongly typed and registered using standard function calls:
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
