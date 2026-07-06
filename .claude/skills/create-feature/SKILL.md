---
name: "create-feature"
description: "Scaffolds a new feature module in BaseKPMArch following the canonical vertical-slice pattern (domain + data + presentation + di), wires it into the app, and creates its living spec.md. Use when adding a new feature/screen to this KMP template."
---

# create-feature

Scaffolds a new `feature:<name>` module for this repo, reproducing the exact layered structure
used by `feature:dashboard` — the canonical reference feature. Read `feature/dashboard/` first;
every file this skill creates should look like the equivalent file there, with names changed.

The goal is **anti-drift**: every feature in this codebase — human-written or AI-generated —
follows the same shape, so nobody has to guess where a repository interface, a use case, or a
ViewModel belongs.

## Before you start

1. Ask the user for: the feature name (kebab/PascalCase, e.g. `profile`), and a one-line
   description of what it does (goes into `spec.md`).
2. Read `CLAUDE.md` — the "Module Architecture" and "Adding a New Feature Module" sections — for
   the dependency rules this scaffold must respect:
   - `core:domain` — zero framework dependencies (no Koin, no Compose).
   - `core:data` — implements `core:domain` interfaces. Never imported by features directly.
   - `feature:*` — imports `core:domain` and `core:ui` only. Never imports `core:data`.
   - Use cases are registered in the **feature's** Koin module (not domain's), keeping
     `core:domain` framework-free.
3. Read `feature/dashboard/` end to end (all 4 layers below) as the concrete template to imitate.

## Steps

### 1. Domain layer — `core/domain/src/commonMain/kotlin/.../domain/`

- `model/<Name>Data.kt` — plain data class, no annotations.
- `repository/<Name>Repository.kt` — interface with `suspend fun` members.
- `usecase/Get<Name>DataUseCase.kt` — class taking the repository, exposing `suspend operator fun invoke()`.

Mirror `core/domain/.../domain/{model,repository,usecase}/Dashboard*.kt`.

### 2. Data layer — `core/data/src/commonMain/kotlin/.../data/`

- `dto/<Name>Dto.kt` — `@Serializable` DTO mirroring the wire format.
- `mapper/<Name>Mapper.kt` — `<Name>Dto.toDomain(): <Name>Data` extension function.
- `repository/<Name>RepositoryImpl.kt` — implements the domain interface. If there's no backend
  yet, return static/stub data with a `// TODO:` comment showing where the real Ktor call
  (`createHttpClient()`, already in `core:data`) would go — do not block the scaffold on a
  backend existing.
- Register it in `core/data/.../di/DataModule.kt`:
  ```kotlin
  singleOf(::<Name>RepositoryImpl) { bind<<Name>Repository>() }
  ```

Mirror `core/data/.../data/{dto,mapper,repository}/Dashboard*.kt` and the `dataModule` entry.

### 3. Feature module — `feature/<name>/`

- `build.gradle.kts` — copy `feature/dashboard/build.gradle.kts` verbatim, then:
  - change `namespace` to `es.edualorobles.basekpmarch.feature.<name>`
  - change `packageOfResClass` to `es.edualorobles.basekpmarch.feature.<name>.resources`
  - keep `implementation(project(":core:domain"))`, `implementation(project(":core:ui"))`,
    `androidResources { enable = true }`, and `withHostTest {}` — all four are required (see
    CLAUDE.md's "Compose Resources" section for why `androidResources` matters, and the host-test
    note below for why `withHostTest {}` matters).
  - keep the `commonTest.dependencies` block (`kotlin.test` + `kotlinx.coroutines.test`).
- `src/commonMain/kotlin/.../presentation/<Name>UiState.kt` — sealed interface: at minimum
  `Loading`, `Content(data: <Name>Data)`, `Error(message: String)`.
- `src/commonMain/kotlin/.../presentation/<Name>ViewModel.kt` — `ViewModel()` holding a
  `MutableStateFlow<<Name>UiState>`, loads via the use case in `init {}`, exposed as a public
  `StateFlow`.
- `src/commonMain/kotlin/.../ui/<Name>Screen.kt` — `@Composable fun <Name>Screen(viewModel: <Name>ViewModel = koinViewModel())`, collects `uiState`, renders a `when` over the sealed states.
  Wrap any custom look-and-feel in tokens from `core:ui` (`MaterialTheme.spacing`, `MaterialTheme.typography`, `MaterialTheme.colorScheme`) — never a hardcoded `Color(...)` or raw `.dp` literal.
- `src/commonMain/kotlin/.../di/<Name>Module.kt`:
  ```kotlin
  val <name>Module = module {
      factoryOf(::Get<Name>DataUseCase)
      viewModelOf(::<Name>ViewModel)
  }
  ```
- `src/commonMain/composeResources/values/strings.xml` (+ `values-es/` if the app is localized) —
  only for static UI chrome (titles, labels). Anything that comes from the domain model must
  never live in compose resources.
- `src/commonTest/kotlin/.../presentation/<Name>ViewModelTest.kt` — mirror
  `feature/dashboard/src/commonTest/.../DashboardViewModelTest.kt`: a fake repository, one test for
  the success path (`Content`), one for the failure path (`Error`). Remember the `Dispatchers.setMain`/`resetMain` `@BeforeTest`/`@AfterTest` pair — omitting it makes the test crash with
  "Module with the Main dispatcher had failed to initialize".

### 4. Wire it in

- Add `include(":feature:<name>")` to `settings.gradle.kts`.
- Add `implementation(project(":feature:<name>"))` to `di/build.gradle.kts`.
- Register `<name>Module` in `di/src/commonMain/kotlin/.../di/KoinInitializer.kt`'s `modules(...)` list.
- Add a route: a `@Serializable data object <Name>` under `sealed interface Route` in
  `shared/.../navigation/Route.kt`, and a `composable<Route.<Name>> { <Name>Screen() }` entry in
  `shared/.../navigation/AppNavigation.kt`. If `shared` doesn't yet depend on
  `project(":feature:<name>")`, add it.

### 5. Write the spec

Copy `docs/specs/_TEMPLATE.md` to `docs/specs/<name>.md` and fill it in using the one-line
description from step 0 — goals, non-goals, the design decisions made while scaffolding (e.g.
"data comes from a stub repository, not a real endpoint, because no backend exists yet"), and the
requirements as GIVEN/WHEN/THEN scenarios. This file is the persistent memory for the feature:
update its changelog whenever the feature's behavior or shape changes materially.

## Verify

1. `./gradlew :core:domain:compileAndroidMain :core:data:compileAndroidMain :feature:<name>:compileAndroidMain :di:compileAndroidMain :androidApp:compileDebugKotlin`
2. `./gradlew compileKotlinIosArm64`
3. `./gradlew :feature:<name>:testAndroidHostTest`
4. Run the app (see the `run` skill) and navigate to the new screen.
