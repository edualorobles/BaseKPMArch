plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibraryKmp)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "es.edualorobles.basekpmarch.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources { enable = true }
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling)
        }
        commonMain.dependencies {
            implementation(project(":di"))
            implementation(project(":feature:dashboard"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.preview)
            implementation(libs.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

// iOS tests require a connected simulator/device — run them from Xcode.
// The linkDebugTest tasks are disabled here to keep ./gradlew build green on any machine.
tasks.matching { it.name.startsWith("linkDebugTest") && it.name.contains("Ios") }
    .configureEach { enabled = false }

compose.resources {
    publicResClass = true
    packageOfResClass = "es.edualorobles.basekpmarch.resources"
    generateResClass = always
}
