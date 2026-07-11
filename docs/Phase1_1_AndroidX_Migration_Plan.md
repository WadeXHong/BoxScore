# Phase 1.1: AndroidX Source Code Migration Plan

## Objective
The build system has been successfully upgraded to require AndroidX (`android.useAndroidX=true`), but the raw source code (`.java` and `.xml` files) still references the deprecated `android.support.*` packages. This plan outlines the strategy to bulk-migrate the legacy source code to the modern `androidx.*` namespace using a terminal-based text replacement approach.

## Target Scope
1.  **Java Source Files:** `/app/src/main/java/**/*.java`
2.  **XML Layout/Resource Files:** `/app/src/main/res/**/*.xml`

## Core Migration Mappings
We will execute a script to perform the following exact string replacements. This covers all the standard support library usages in the BoxScore app.

### 1. Core Framework & UI
*   `android.support.v7.app.AppCompatActivity` ➔ `androidx.appcompat.app.AppCompatActivity`
*   `android.support.v4.app.Fragment` ➔ `androidx.fragment.app.Fragment`
*   `android.support.v4.app.FragmentManager` ➔ `androidx.fragment.app.FragmentManager`
*   `android.support.v4.app.FragmentTransaction` ➔ `androidx.fragment.app.FragmentTransaction`
*   `android.support.v4.app.FragmentStatePagerAdapter` ➔ `androidx.fragment.app.FragmentStatePagerAdapter`
*   `android.support.v4.content.ContextCompat` ➔ `androidx.core.content.ContextCompat`
*   `android.support.v4.app.ActivityCompat` ➔ `androidx.core.app.ActivityCompat`

### 2. Layouts & Widgets
*   `android.support.v7.widget.RecyclerView` ➔ `androidx.recyclerview.widget.RecyclerView`
*   `android.support.v7.widget.LinearLayoutManager` ➔ `androidx.recyclerview.widget.LinearLayoutManager`
*   `android.support.constraint.ConstraintLayout` ➔ `androidx.constraintlayout.widget.ConstraintLayout`
*   `android.support.v4.view.ViewPager` ➔ `androidx.viewpager.widget.ViewPager`
*   `android.support.v4.widget.DrawerLayout` ➔ `androidx.drawerlayout.widget.DrawerLayout`
*   `android.support.v4.widget.SwipeRefreshLayout` ➔ `androidx.swiperefreshlayout.widget.SwipeRefreshLayout`
*   `android.support.design.widget.TabLayout` ➔ `com.google.android.material.tabs.TabLayout`
*   `android.support.design.widget.FloatingActionButton` ➔ `com.google.android.material.floatingactionbutton.FloatingActionButton`

### 3. Annotations
*   `android.support.annotation.NonNull` ➔ `androidx.annotation.NonNull`
*   `android.support.annotation.Nullable` ➔ `androidx.annotation.Nullable`
*   `android.support.annotation.StringRes` ➔ `androidx.annotation.StringRes`
*   `android.support.annotation.ColorRes` ➔ `androidx.annotation.ColorRes`
*   `android.support.annotation.DimenRes` ➔ `androidx.annotation.DimenRes`
*   `android.support.annotation.StringDef` ➔ `androidx.annotation.StringDef`

## Execution Strategy
1.  **Script Creation:** I will write a bash script utilizing `find` and `sed` to recursively search through the project directories.
2.  **Dry Run / Regex Matching:** The script will precisely match the package names in `import` statements (Java) and XML tags (`<android.support...>`).
3.  **Execution:** Run the script to apply the replacements in-place.
4.  **Verification:** Execute `./gradlew compileDebugJavaWithJavac` to verify that all 375+ missing package errors have been resolved and the Java code cleanly compiles against the modern SDK.
