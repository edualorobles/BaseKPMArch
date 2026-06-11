# BaseKPMArch 🚀

**Modular Clean Architecture Template for Kotlin Multiplatform (KMP).**

This repository serves as a starting point (*Boilerplate*) to create scalable native mobile applications for Android and iOS, sharing **100% of the business logic and UI**.

Designed to be **Future-Proof**, ready for **Gradle 9.0**, and aligned with the latest recommendations from **Google** and **JetBrains**.

---

## 🛠 Tech Stack

* **Language:** Kotlin 2.3+ (Multiplatform)
* **UI:** Compose Multiplatform 1.7+ (Android & iOS)
* **Dependency Injection:** Koin 4.x (Annotations & Compose support)
* **Async:** Coroutines & Flow
* **Architecture:** Strict Modular Clean Architecture
* **Navigation:** Jetpack Navigation Compose (Type-Safe)
* **Networking:** Ktor 3.x (Content Negotiation, Logging, Serialization)
* **Build System:** Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`)
* **Plugins:** `com.android.kotlin.multiplatform.library` and experimental shared resources support

---

## 📂 Modular Structure

The project follows a **strict separation of responsibilities** to ensure scalability and testability:

* **`:androidApp`**  
  Native Android launcher. Minimal configuration. Manages the native *Splash Screen* and initial lifecycle.

* **`:shared`**  
  Main shared library (KMP, `com.android.kotlin.multiplatform.library`). Contains:
  - `App.kt` (shared Compose UI entry point)
  - `AppNavigation`
  - `MainViewController` (iOS entry point, in `iosMain`)
  - Theme and shared resources

* **`:di` (Composition Root)**  
  The **only module that knows all others**.  
  Koin is initialized here, binding implementations (`data`) to interfaces (`domain`).

* **`:feature:*`** (e.g. `dashboard`)  
  Vertical screen/flow modules.
  - ViewModels
  - Composables
  - Presentation logic  
    They depend on `:core:domain` and `:core:ui`, but **never** on `:core:data`.

* **`:core:domain`**  
  Business rules, Use Cases and pure Models.  
  No framework or dependency injection dependencies.

* **`:core:data`**  
  Repository implementations, HTTP client (Ktor) and data sources.  
  Knows nothing about Koin or UI.

* **`:core:ui`**  
  Design System, Colors, Typography and shared resources.

---

## 🚀 Quick Start (Generate a New Project)

This template includes an automation script (`setup_project.sh`) that sets up your new project in seconds, automatically renaming packages, directories and configuration files.

### 1. Clone the Repository

```bash
git clone <url-of-this-repo> NewProject
cd NewProject
```

### 2. Run the Setup Script

Run the script from the project root.  
You will be prompted for:
- Project name (e.g. `GymTracker`)
- Base package (e.g. `com.gymtracker.app`)

```bash
./setup_project.sh
```

### 3. Cleanup and First Build

Once the script finishes successfully:

```bash
rm setup_project.sh
```

(Optional) Reset Git history:

```bash
rm -rf .git && git init
```

Then:

* Open Android Studio
* Run **Sync Project with Gradle Files**
* Run the app (**Run `androidApp`**) to verify everything compiles correctly

---

## 🎨 How to Customize App Icon & Splash Screen

This project is configured with native Splash Screens for both platforms. Follow these steps to update the branding for a new project.

### 🍎 iOS

1.  **App Icon**:
  * Generate your iOS app icons (sizes 20pt to 1024pt).
  * Replace the contents of `iosApp/iosApp/Assets.xcassets/AppIcon.appiconset` with your new images.
  * *Tip:* Ensure the 1024x1024 file is opaque (no transparency) for the App Store.

2.  **Splash Screen (Launch Screen)**:
  * **Logo**: Replace the images in `iosApp/iosApp/Assets.xcassets/SplashLogo.imageset` (1x, 2x, 3x) with your transparent logo.
  * **Background Color**: Open `iosApp/iosApp/LaunchScreen.storyboard` in Xcode. Select the main View and change the `Background` property.
  * **Important**: If you rename the storyboard file, ensure the filename has **NO spaces** (e.g., use `LaunchScreen.storyboard`, not `Launch Screen.storyboard`).
  * Update `iosApp/iosApp/Info.plist`: Verify that the key `UILaunchStoryboardName` matches your storyboard filename exactly (without extension).

### 🤖 Android

1. **App Icon**:
  * Place your standard icons in `androidApp/src/main/res/mipmap-*` folders.
  * **Adaptive Icons**: Update `ic_launcher_foreground.xml` (your logo) and `ic_launcher_background.xml` (your background color) in `androidApp/src/main/res/drawable` or `mipmap-anydpi-v26`.
  * Or run `./scripts/update_icons.sh --icon path/to/icon.png` to auto-generate all sizes.
2. **Splash Screen**:
  * This project uses `androidx.core:core-splashscreen`.
  * Update the `windowSplashScreenBackground` and `windowSplashScreenAnimatedIcon` colors/drawables in `androidApp/src/main/res/values/themes.xml` (Theme `Theme.App.Starting`).

---

## 📦 Version Management

All library and plugin versions are centralized in:

```
gradle/libs.versions.toml
```

Do not hardcode versions in `build.gradle.kts` files.

---

## 🤝 Author

**Edu Alonso**
