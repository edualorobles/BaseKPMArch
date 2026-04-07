plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibraryKmp)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    android {
        namespace = "es.edualorobles.basekpmarch.core.domain"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        withJava()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
