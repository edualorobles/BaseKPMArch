# BaseKPMArch 🚀

**Plantilla de Arquitectura Limpia Modular para Kotlin Multiplatform (KMP).**

Este repositorio sirve como punto de partida (Boilerplate) para crear aplicaciones móviles nativas escalables para Android e iOS, compartiendo el 100% de la lógica de negocio y la UI.

Diseñado para ser **Future-Proof**, preparado para Gradle 9.0 y las últimas recomendaciones de Google y JetBrains.

## 🛠 Stack Tecnológico

* **Lenguaje:** Kotlin 2.3+ (Multiplatform)
* **UI:** Compose Multiplatform 1.9+ (Android & iOS)
* **Inyección de Dependencias:** Koin 4.x (Annotations & Compose support)
* **Async:** Coroutines & Flow
* **Arquitectura:** Clean Architecture Modular Estricta
* **Navegación:** Navigation Compose
* **Build System:** Gradle Kotlin DSL + Version Catalog (`libs.versions.toml`)
* **Plugins Android:** Nuevo plugin `com.android.kotlin.multiplatform.library` para librerías compartidas.

## 📂 Estructura Modular

El proyecto sigue una separación estricta de responsabilidades para garantizar la escalabilidad y testabilidad:

* **`:androidApp`**: Lanzador nativo Android. Configuración mínima, solo lanza la UI compartida.
* **`:composeApp`**: Librería compartida principal. Contiene el `CompositionLocalProvider`, el tema y el punto de entrada de la UI.
* **`:di` (Composition Root)**: El único módulo que conoce a todos. Aquí se inicializa Koin y se enlazan las implementaciones (`data`) con las interfaces (`domain`).
* **`:feature:*`** (ej. `dashboard`): Módulos de pantallas/flujos. Contienen ViewModels y Composables. Ven `:core:domain` y `:core:ui`, pero **nunca** `:core:data`.
* **`:core:domain`**: Reglas de negocio, Casos de Uso y Modelos puros. No tiene dependencias de framework ni de inyección.
* **`:core:data`**: Implementación de repositorios y fuentes de datos. No conoce a Koin ni la UI.
* **`:core:ui`**: Design System, Theme, Tipografía y recursos comunes.

---

## 🏗 Cómo usar esta Plantilla (Crear nuevo proyecto)

Sigue estos pasos rigurosamente para instanciar un nuevo proyecto (ej. *GymTracker*) basado en esta arquitectura.

### 1. Crear el Repositorio
Usa el botón **"Use this template"** en GitHub para crear tu nuevo repositorio.

### 2. Clonar
Clona tu nuevo repositorio `GymTracker` en tu máquina.

### 3. Renombrado Global (Search & Replace)
Debes reemplazar las referencias del template por las de tu nuevo proyecto. Usa `Cmd+Shift+R` (Mac) o `Ctrl+Shift+R` (Win/Linux) en tu IDE.

**A. Renombrar el Proyecto:**
* Buscar: `BaseKPMArch`
* Reemplazar por: `GymTracker` (o el nombre de tu app)
* *Archivos clave afectados:* `settings.gradle.kts`.

**B. Renombrar el Paquete Base:**
* Buscar: `es.edualorobles.basekpmarch`
* Reemplazar por: `com.tundominio.gymtracker`
* *Archivos clave afectados:* Todos los `build.gradle.kts` (propiedad `namespace`), `AndroidManifest.xml` y cabeceras de archivos Kotlin.

### 4. Reestructuración de Carpetas (Refactor)
Al cambiar el paquete, debes mover las carpetas físicas para que coincidan con la nueva estructura.

1.  En Android Studio, desmarca "Compact Middle Packages" en el árbol del proyecto.
2.  Renombra/Mueve la ruta de carpetas:
  * De: `src/.../kotlin/es/edualorobles/basekpmarch`
  * A: `src/.../kotlin/com/tundominio/gymtracker`
3.  **Repite esto en todos los módulos:** `:composeApp`, `:androidApp`, `:di`, `:core:*`, `:feature:*`.

### 5. Configuración de Identificadores Nativos
* **Android (`androidApp/build.gradle.kts`):**
  * Verifica `applicationId` (ej. `com.gymtracker.android`).
  * Actualiza `versionCode` y `versionName`.
* **iOS (`iosApp.xcodeproj`):**
  * Abre el proyecto en Xcode.
  * Cambia el **Bundle Identifier** y el **Display Name**.

### 6. Sincronización Final
1.  Ejecuta `./gradlew clean` en la terminal.
2.  Dale a **Sync Project with Gradle Files**.
3.  Compila (`Build > Make Project`) para asegurar que todo está correcto.

---

## 📦 Gestión de Versiones

Todas las versiones se centralizan en `gradle/libs.versions.toml`.
Para actualizar una librería (ej. Compose, Kotlin), edita ese archivo y sincroniza Gradle.

## 🤝 Autor
**Edu Alo Robles**
*Arquitectura diseñada para la eficiencia y la escalabilidad.*