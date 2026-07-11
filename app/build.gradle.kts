// --- Groovy vs Kotlin DSL Migration Notes ---

// In Groovy: apply plugin: 'com.android.application'
// In Kotlin: we use the strongly-typed `plugins` DSL and Version Catalogs aliases.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android) // Applied Kotlin for Android
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    // In Groovy: compileSdkVersion 27
    // In Kotlin: Property assignment requires `=`
    compileSdk = 34
    
    // In modern AGP (8.x+), the namespace is required here instead of AndroidManifest.xml
    namespace = "com.wadexhong.boxscore"

    defaultConfig {
        // In Groovy: applicationId "com.wadexhong.boxscore"
        // In Kotlin: Property assignment requires `=`
        applicationId = "com.wadexhong.boxscore"
        
        // Bumped minSdk from 22 to 24 (minimum recommended for Jetpack Compose)
        minSdk = 24 
        targetSdk = 34
        versionCode = 17
        versionName = "1.0.10"
        
        // In Groovy: testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
        // In Kotlin: Upgraded to AndroidX test runner
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        // In Groovy: release { minifyEnabled false; proguardFiles getDefaultProguardFile('...'), '...' }
        // In Kotlin: We use `getByName("release")` to configure existing build types
        getByName("release") {
            // Groovy used `minifyEnabled`, Kotlin uses `isMinifyEnabled` (boolean property)
            isMinifyEnabled = false 
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), 
                "proguard-rules.pro"
            )
        }
    }
    
    // In Groovy: compileOptions { sourceCompatibility JavaVersion.VERSION_1_8 ... }
    // In Kotlin: AGP 8 requires minimum Java 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // In Groovy: implementation fileTree(dir: 'libs', include: ['*.jar'])
    // In Kotlin: `fileTree` takes a map, so we use `mapOf()` and `listOf()`
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    
    // In Groovy: implementation 'com.android.support:appcompat-v7:27.1.1'
    // In Kotlin: We use the Version Catalog alias `libs.androidx.appcompat` inside parentheses ()
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Firebase (Migrated from individual old versions to the modern Firebase BOM)
    // In Groovy: implementation 'com.google.firebase:firebase-auth:16.0.1'
    // In Kotlin:
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.crashlytics)

    // Third party libraries
    implementation(libs.mpandroidchart)
    implementation(libs.permissionsdispatcher)
    // In Groovy: annotationProcessor "..."
    // In Kotlin: annotationProcessor() requires parentheses
    annotationProcessor(libs.permissionsdispatcher.processor)
    implementation(libs.expandablerecyclerview)
    
    // Using string dependency directly in Kotlin (also valid in KTS)
    implementation("com.cleveroad:adaptivetablelayout:1.1.1")
    
    // Testing libraries migrated to AndroidX
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    
    // In Groovy: compile files('libs/jxl.jar')
    // In Kotlin: `compile` is deprecated, use `implementation`. `files()` requires parentheses.
    implementation(files("libs/jxl.jar"))
}
