# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Project Is

**BaseKPMArch** is a Kotlin Multiplatform (KMP) template for production-ready mobile apps targeting Android and iOS. It uses Compose Multiplatform for shared UI, Clean Architecture, and Koin for DI. When a developer starts a new project, they run `./setup_project.sh` to rename package names and module references.

## Build Commands

```bash
# Full build
./gradlew build

# Android (task names live on :androidApp — the classic androidApplication plugin module —
# not at the root; there is no root-level assembleAndroidDebug/bundleAndroidRelease task)
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:bundleRelease

# iOS frameworks
./gradlew iosSimulatorArm64Binaries  # iOS Simulator (Apple Silicon)
./gradlew iosArm64Binaries        # iOS device

# Unit tests (KMP library modules run host tests via testAndroidHostTest, not testDebugUnitTest —
# that task name only exists on :androidApp, which uses the classic androidApplication plugin.
# Every module with a commonTest source set must set `withHostTest {}` inside `android { ... }`
# in its build.gradle.kts, or the testAndroidHostTest task won't exist at all.)
./gradlew testAndroidHostTest                        # All KMP-module host unit tests
./gradlew :feature:dashboard:testAndroidHostTest      # Single module
./gradlew :androidApp:testDebugUnitTest               # androidApp's own JVM unit tests

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

## Feature Module Structure (Vertical Slice)

Every feature is a **vertical slice** through all layers, split by module boundary. `feature:dashboard`
is the canonical reference — read it before building a new feature, and make the new one look like it:

- `core:domain` — `domain/model/<Name>Data.kt` (plain data class), `domain/repository/<Name>Repository.kt`
  (interface), `domain/usecase/Get<Name>DataUseCase.kt` (`suspend operator fun invoke()`).
- `core:data` — `data/dto/<Name>Dto.kt` (`@Serializable`), `data/mapper/<Name>Mapper.kt`
  (`<Name>Dto.toDomain()`), `data/repository/<Name>RepositoryImpl.kt` (implements the domain
  interface; if no backend exists yet, return static data with a `// TODO:` marking the real Ktor
  call). Register the impl in `DataModule.kt` via `singleOf(::<Name>RepositoryImpl) { bind<<Name>Repository>() }`.
- `feature:<name>` — `presentation/<Name>UiState.kt` (sealed `Loading`/`Content`/`Error`),
  `presentation/<Name>ViewModel.kt` (`StateFlow<UiState>`, calls the use case), `ui/<Name>Screen.kt`
  (collects state, uses `core:ui` tokens), `di/<Name>Module.kt` (registers the use case with
  `factoryOf` **and** the ViewModel with `viewModelOf` — use cases are wired here, not in
  `core:domain`, so domain stays framework-free), plus a `commonTest` with a ViewModel test.

**To scaffold a new one, use the `/create-feature` skill** (`.claude/skills/create-feature/SKILL.md`)
— it walks all four layers, the Gradle wiring, and the `spec.md` in one pass. Manually, the steps are:

1. Create `feature/<name>/build.gradle.kts` — copy `feature/dashboard/build.gradle.kts`, update
   namespace and `packageOfResClass`. Keep `implementation(project(":core:ui"))` and
   `withHostTest {}`.
2. Add to `settings.gradle.kts` include list, and `implementation(project(":feature:<name>"))` to `di/build.gradle.kts`.
3. Build the domain/data/presentation files above.
4. Register the feature's Koin module in `di/src/commonMain/kotlin/.../KoinInitializer.kt`.
5. Add navigation route in `shared` (type-safe route object with `@Serializable`).
6. Copy `docs/specs/_TEMPLATE.md` to `docs/specs/<name>.md` and fill it in.

## Feature Specs (`docs/specs/`)

Each feature has a living spec at `docs/specs/<name>.md` (template: `docs/specs/_TEMPLATE.md`,
reference instance: `docs/specs/dashboard.md`): goals, non-goals, dated design decisions with
rejected alternatives, requirements as GIVEN/WHEN/THEN scenarios, and a changelog. Read the spec
before modifying a feature; update it — don't just edit the code — when behavior, scope, or shape
changes. This is what keeps the codebase from drifting as features accumulate.

## Dependency Injection (Koin 4.x)

- **Initialization on Android:** `BaseApplication.onCreate()` calls `initKoin { androidContext(this) }`
- **Initialization on iOS:** `MainViewController()` calls `initKoin()`
- **Entry point:** `di/src/commonMain/.../KoinInitializer.kt` — `initKoin()` function lists all modules
- **ViewModel pattern:** Use `viewModel: MyViewModel = koinViewModel()` as a Composable default parameter; register with `viewModelOf(::MyViewModel)` (from `org.koin.core.module.dsl`) in the feature's Koin module. Use cases follow the same DSL: `factoryOf(::MyUseCase)`.

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

Shared resources (images, strings, fonts) live in `shared/src/commonMain/composeResources/`. They are accessed via the generated public class `es.edualorobles.basekpmarch.resources`.

**Any KMP module with its own `composeResources/` (e.g. `feature:dashboard`, or any new `feature:*` module) MUST set `androidResources { enable = true }` inside `kotlin { android { ... } }` in its `build.gradle.kts`.** Without it, AGP does not package that module's `.cvr` resource assets into the final app APK, and any `stringResource(...)`/`painterResource(...)` call from that module crashes at runtime with `org.jetbrains.compose.resources.MissingResourceException` — even for the default (non-localized) resources, not just locale variants. Verify by checking the APK contains `assets/composeResources/<packageOfResClass>/...`.

## Setting Up a New Project From This Template

```bash
git clone <repo> MyNewProject
cd MyNewProject
./setup_project.sh   # Prompts for new name and package (e.g. com.company.appname)
rm setup_project.sh
# Open in Android Studio, sync Gradle
```

The script replaces `es.edualorobles.basekpmarch` → new package and `BaseKPMArch` → new name across all source files and directory structures, then cleans build artifacts.
