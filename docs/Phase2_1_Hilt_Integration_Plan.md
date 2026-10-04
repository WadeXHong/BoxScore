# Phase 2.1: Hilt Dependency Injection Integration Plan

## Objective
Introduce **Dagger Hilt** as the standard Dependency Injection (DI) framework for the `BoxScore` application. Hilt will replace manual object creation and provide a clean, scalable way to inject ViewModels, Use Cases, Room DAOs, and Repositories across the application.

## Step-by-Step Implementation Strategy

### 1. Dependency Configuration
*   **Version Catalogs (`libs.versions.toml`):** 
    *   Add Hilt version (e.g., `2.49`).
    *   Add the Hilt Android Gradle Plugin.
    *   Add `hilt-android` and `hilt-compiler` libraries.
*   **Build Scripts (`build.gradle.kts`):**
    *   Apply the `com.google.dagger.hilt.android` plugin at the project and app levels.
    *   Add KSP (Kotlin Symbol Processing) plugin to handle Hilt's annotation processing efficiently (replacing KAPT).

### 2. Application Class Setup
*   **Create `BoxScoreApplication.kt`:** Create a new custom Kotlin `Application` class.
*   **Annotate:** Add the `@HiltAndroidApp` annotation to trigger Hilt's code generation for the base application component.
*   **Manifest Registration:** Update `AndroidManifest.xml` to point the `<application android:name=".BoxScoreApplication">` attribute to the new class.

### 3. Core Module Definition
*   **Create `di/AppModule.kt`:** Establish the foundational Hilt Module annotated with `@Module` and `@InstallIn(SingletonComponent::class)`.
*   **Provide Global Dependencies:** Use `@Provides` to expose application-level singletons (e.g., providing the `Application` context globally if needed, though Hilt provides `@ApplicationContext` by default).

### 4. Legacy Interoperability (Preparation)
*   As we migrate vertical slices, we will use `@AndroidEntryPoint` on our Activities and Fragments to allow Hilt to inject modern `ViewModel`s alongside the existing legacy MVP structure.

## Verification
*   Compile the project successfully with the KSP and Hilt plugins.
*   Ensure the application launches without crashing, proving that the generated Hilt application component is functioning correctly.
