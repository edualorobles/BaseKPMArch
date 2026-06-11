# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Project Is

**BaseKPMArch** is a Kotlin Multiplatform (KMP) template for production-ready mobile apps targeting Android and iOS. It uses Compose Multiplatform for shared UI, Clean Architecture, and Koin for DI. When a developer starts a new project, they run `./setup_project.sh` to rename package names and module references.

## Build Commands

```bash
# Full build
./gradlew build

# Android
./gradlew assembleAndroidDebug
./gradlew bundleAndroidRelease

# iOS frameworks
./gradlew iosSimulatorArm64Binaries  # iOS Simulator (Apple Silicon)
./gradlew iosArm64Binaries        # iOS device

# Unit tests
./gradlew testDebugUnitTest                          # All Android unit tests
./gradlew :core:domain:testDebugUnitTest             # Single module
./gradlew :feature:dashboard:testDebugUnitTest

# Kotlin compilation check
./gradlew :androidApp:compileDebugKotlin     # Android
./gradlew compileKotlinIosArm64              # iOS device

# Clean
./gradlew clean
```

## Module Architecture

There are 7 modules with strict dependency rules:

```
androidApp ─── shared (App.kt, navigation)
                    │
                   di  ← Composition Root (ONLY module that imports all others)
                  / \
        core:data   feature:dashboard
             │              │
        core:domain ←───────┘
        
core:ui  ← Design system (imported by shared and feature modules, not data/domain)
```

**Strict rules enforced by module boundaries:**
- `core:domain` — zero framework dependencies. Only Coroutines, DateTime, Serialization.
- `core:data` — imports `core:domain`. Never imported by feature modules directly.
- `feature:*` — imports `core:domain` and `core:ui` only. Never imports `core:data`.
- `:di` — the single Composition Root. It imports everything to wire DI modules together.
- `:shared` — imports `:di` only (transitively gets everything). KMP library (`com.android.kotlin.multiplatform.library`). Entry points: `MainActivity` lives in `:androidApp`; `MainViewController` lives in `shared/iosMain`.

## Adding a New Feature Module

1. Create `feature/<name>/build.gradle.kts` — copy `feature/dashboard/build.gradle.kts`, update namespace.
2. Add to `settings.gradle.kts` include list.
3. Create `DashboardModule`-style Koin module in `feature/<name>/src/commonMain/kotlin/.../di/`.
4. Register the Koin module in `di/src/commonMain/kotlin/.../KoinInitializer.kt`.
5. Add navigation route in `shared` (type-safe route object with `@Serializable`).

## Dependency Injection (Koin 4.x)

- **Initialization on Android:** `BaseApplication.onCreate()` calls `initKoin { androidContext(this) }`
- **Initialization on iOS:** `MainViewController()` calls `initKoin()`
- **Entry point:** `di/src/commonMain/.../KoinInitializer.kt` — `initKoin()` function lists all modules
- **ViewModel pattern:** Use `koinViewModel<MyViewModel>()` in Composables; register with `viewModel { MyViewModel(get()) }` in Koin modules

## Platform-Specific Code

Use `androidMain` / `iosMain` source sets for:
- Ktor engine selection (`OkHttp` for Android, `Darwin` for iOS)
- Platform API implementations via `expect`/`actual`
- iOS entry point: `MainViewController` in `shared/src/iosMain/`
- Android entry point: `MainActivity` in `androidApp/src/main/kotlin/` (app module, not the shared library)

## Key Technology Versions

See `gradle/libs.versions.toml` for all versions. Core ones:
- Kotlin: 2.4.0 | Compose Multiplatform: 1.11.1 | Koin: 4.2.1 | Ktor: 3.5.0
- Android compileSdk/targetSdk: 36 | minSdk: 24

Always update versions in `libs.versions.toml`, never hardcode them in `build.gradle.kts` files.

## Compose Resources

Shared resources (images, strings, fonts) live in `shared/src/commonMain/composeResources/`. They are accessed via the generated public class `es.edualorobles.basekpmarch.resources`. Android KMP resources are enabled via `androidResources { enable = true }` in `shared/build.gradle.kts`.

## Setting Up a New Project From This Template

```bash
git clone <repo> MyNewProject
cd MyNewProject
./setup_project.sh   # Prompts for new name and package (e.g. com.company.appname)
rm setup_project.sh
# Open in Android Studio, sync Gradle
```

The script replaces `es.edualorobles.basekpmarch` → new package and `BaseKPMArch` → new name across all source files and directory structures, then cleans build artifacts.
