plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibraryKmp)
}

kotlin {
    android {
        namespace = "es.edualorobles.basekpmarch.di"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(project(":core:domain"))
            implementation(project(":core:data"))
            implementation(project(":core:ui"))
            implementation(project(":feature:dashboard"))
        }
    }
}
