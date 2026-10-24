# AGENTS.md

Guidance for AI coding agents working in this repo. This project is an offline-first
Kotlin Multiplatform client (Android + iOS) for the [Monica](https://github.com/monicahq/monica)
personal relationship management system. See `README.md` for product context.

## Module layout

- `app/` — shared KMP module: root navigation (`MonicaApp`, `RootNavGraphBuilder`,
  `HomeNavGraphBuilder`), app-level screens (home/tab shell, dashboard, contacts list, setup),
  the `MonicaDatabase` Room 3 database (aggregates all feature tables), DataStore, sync scheduling,
  app links and Coil setup. Also produces the `MonicaApp` iOS framework
  (`iosArm64` + `iosSimulatorArm64`, static, links `-lsqlite3`).
- `androidApp/` — thin Android shell: `MonicaApp` Application (owns the Metro graph),
  `AppComponent`, manifest, signing config, Sentry and debug/release logger wiring. Screens do not
  live here (`MainActivity` is in `app/src/androidMain`). Assemble/install with
  `:androidApp:assembleDebug`.
- `iosApp/` — thin Xcode shell consuming the `MonicaApp` framework. Build via `xcodebuild`, not Gradle.
- `core/` — shared infrastructure: `account` (`AccountListener`), `data` (paging/sync primitives),
  `datetime`, `dispatcher`, `inject` (Metro/ViewModel glue), `network` (Ktor + Sandwich setup),
  `test` (test fixtures: fakes, clock/dispatcher bindings, Kotest config), `ui` (theme,
  `LocalNavigator`, `LocalWebBrowser`, clipboard helpers, shared components).
- `feature/` — feature modules:
  - split `:data` / `:domain` / `:ui`: `feature/activity` (plus `:nav` and `:test`),
    `feature/contact` (plus `:nav` and `:test`); `feature/photo` has `:data` only.
  - single module: `account`, `certificate`, `configuration`, `genders`, `journal`.
  - `feature/contact-api` and `feature/user-api` expose cross-feature public APIs.
  - `feature/*/test` modules are test fixtures (fake DAOs/APIs, MockK bindings), not production code.
  - `:nav` modules only contain `@Serializable` route types; the `NavGraphBuilder` extensions live in
    the matching `:ui` module. Other features keep routes and nav extensions in the same module.
- `component/` — shared components: `tips` (Room-backed tips + table owner), `user_avatar`.
- `build-logic/` — convention plugins applied via version catalog aliases:
  `monica.kmp` (KMP with Android KMP library + iOS targets), `monica.cmp` (Compose Multiplatform,
  applies `monica.kmp`), `monica.metro` (Metro + Metrox ViewModels), `monica.network`
  (Ktor/Sandwich/serialization), `monica.module.data` (Room + Paging + core data deps),
  `monica.module.test.fixture`, `monica.android.application`. Check these before adding
  per-module build configuration.
- `gradle/libs.versions.toml` — all dependencies and plugin versions.
- `config/detekt/detekt.yml` — detekt 2 overrides on top of the default rule set.
- `docs/` + `mkdocs.yml` — user-facing docs site; `http/` — scratch `.http` requests for the Monica API.

## Key technologies

- Kotlin Multiplatform: every `monica.kmp`/`monica.cmp` module builds an Android KMP library
  (`com.android.kotlin.multiplatform.library`) plus `iosArm64` and `iosSimulatorArm64`.
- Compose Multiplatform (JetBrains artifacts) + Material3, Navigation Compose
  (`org.jetbrains.androidx.navigation`, type-safe routes).
- Metro for DI (`dev.zacsweers.metro` binding containers, `metrox-viewmodel` +
  `metrox-viewmodel-compose`).
- Ktor (OkHttp on Android, Darwin on iOS) + Sandwich for networking; kotlinx.serialization.
- Room 3 (`androidx.room3`) + bundled SQLite, DataStore Preferences, Paging 3, Coil 3.
- WorkManager on Android behind `WorkScheduler` (iOS runs sync in a coroutine); AndroidX Browser
  for OAuth.
- Kotest (assertions/engine, JUnit5 runner) + Turbine + MockK; Robolectric + Compose UI test for
  host tests.
- Kover for coverage, detekt 2 + compose-rules for lint, Sentry KMP, GitHub Actions for CI.

## Dependency injection (Metro)

- Never construct ViewModels manually. There are two patterns:
  - Plain ViewModel: `@Inject` + `@ContributesIntoMap(AppScope::class)` + `@ViewModelKey`,
    retrieved in Compose with `metroViewModel()`.
  - ViewModel with navigation args or assisted parameters: `@AssistedInject` with a nested
    `@AssistedFactory` annotated with `@ViewModelAssistedFactoryKey(...)` (SavedStateHandle-based)
    or `@ManualViewModelAssistedFactoryKey` (extra runtime args), also
    `@ContributesIntoMap(AppScope::class)`; retrieve with `assistedMetroViewModel<...>()`.
  ```kotlin
  @Inject
  @ContributesIntoMap(AppScope::class)
  @ViewModelKey
  class AccountViewModel(
      private val dispatcher: Dispatcher,
      /* ... */
  ) : ViewModel()

  @Composable
  fun Account(viewModel: AccountViewModel = metroViewModel(), /* ... */) { /* ... */ }
  ```
- `MonicaViewModelFactory` (`app`) bridges Metro's ViewModel maps into `MetroViewModelFactory`.
  `LocalMetroViewModelFactory` is provided at the Compose root: `MainActivity` on Android,
  `MainViewController` on iOS.
- App graph: `@DependencyGraph(AppScope::class) ... : ViewModelGraph` — an interface on Android
  (`androidApp/.../di/AppComponent.android.kt`), an abstract class on iOS
  (`app/src/iosMain/.../di/AppComponent.ios.kt`). Reusable bindings live in
  `@ContributesTo(AppScope::class) @BindingContainer object`s (e.g. `DatabaseComponent`,
  `NetworkBindings`, `ContactDaoComponent`); implementations use `@ContributesBinding` and are
  scoped with `@SingleIn(AppScope::class)`. Provide platform types with `@Provides`.
- Dispatchers: inject `Dispatcher` (`core:dispatcher`) and use `dispatcher.io`; never hardcode
  dispatchers (detekt `InjectDispatcher`; existing violations are baselined).

## Navigation

- Type-safe routes: `@Serializable object` / `data class ...Route`, registered with
  `fun NavGraphBuilder.<feature>() { composable<...Route> { ... } }`.
- Root graph: `RootNavGraphBuilder` (used by `MonicaApp` with `startDestination = HomeRoute`)
  registers only `home()`, `setup()` and `certificateListAndDetails()`.
- `HomeRoute` renders the tab shell. `HomeScreen` owns a nested `NavHost` built from
  `HomeNavGraphBuilder`; the tabs are declared in `topLevelRoutes` (Dashboard, Contacts, Journal).
- Adding a destination: define the route + nav extension, then register it in `HomeNavGraphBuilder`
  for the logged-in shell or in `RootNavGraphBuilder` for root-level screens (setup, certificates).
- Navigate with `LocalNavigator` (`core:ui`), a `NavHostController` provided at the Compose root
  (`MainActivity`/`MainViewController`) and overridden in `HomeScreen` for the nested graph:
  `LocalNavigator.current.navigate(...)`, `popBackStack()`.
- Cross-feature routes belong in `feature/contact/nav` or `feature/activity/nav`, with their nav
  extensions in the matching `:ui` module.

## Code style

- Detekt 2 runs on `:app` only (`app/build.gradle.kts`) with `buildUponDefaultConfig = true` and
  overrides in `config/detekt/detekt.yml`; compose-rules is registered for that module. Existing
  findings are recorded in `app/detekt-baseline-main.xml`. There is no ktlint/detekt-formatting
  setup on the Gradle side, and detekt is not part of CI — run it locally.
- Default detekt rules are still enforced in `:app` and are worth following everywhere:
  `MaxLineLength` 120, `LongMethod` 60 lines, `ReturnCount` max 2, `MagicNumber` (use named
  constants; `-1/0/1/2` exempt), `ForbiddenComment` (no `TODO:`, `FIXME:`, `STOPSHIP:`).
- Compose rules: `ModifierMissing` / `ModifierWithoutDefault` (modifier first, with default),
  `ModifierNotUsedAtRoot`, `ComposableParamOrder`, `ViewModelForwarding` (screens take the ViewModel;
  inner composables take state/callbacks). Follow them in feature modules too.
- A new `CompositionLocal` must be added to `Compose.CompositionLocalAllowlist` in
  `config/detekt/detekt.yml` (currently only `LocalWebBrowser`).

## Clipboard

Copy via Compose Multiplatform's `androidx.compose.ui.platform.Clipboard` from `LocalClipboard`
and the `suspend` helpers in `core/ui/.../clipboard/Clipboard.kt`:

```kotlin
LocalClipboard.current.setPlainText(text)
```

Do not use the old Android `ClipboardManager`.

## Testing

- `commonTest` in every KMP module: Kotest assertions/engine + coroutines-test + Turbine. The
  `monica.kmp` plugin already adds `:core:test` (fakes, clock/dispatcher/networking bindings) to
  `commonTest` — don't add it manually.
- `androidHostTest` (what CI runs): Kotest JUnit5 runner + MockK + Robolectric; `monica.cmp` adds
  Compose UI test, Robolectric, Espresso and the JUnit 4 vintage engine (Robolectric needs JUnit 4).
  Host tests currently live in `app`, `feature/contact/ui` and `feature/journal`.
- Test fixtures: `core:test`, `feature/activity:test` and `feature/contact:test` provide fake DAOs,
  fake APIs and MockK bindings; `feature:*:test` modules need explicit dependencies.
- `core/test/.../KotestConfig.kt` installs `UnconfinedTestDispatcher` as `Dispatchers.Main` and mocks
  `SavedStateHandle` navigation statics — keep that working when changing navigation args in tests.
- Room DAOs/databases are faked in tests, not exercised against a real database; don't chase coverage
  on them. Kover tries to exclude `@Dao`/`@Database` classes, but `settings.gradle.kts` still filters
  `androidx.room.*` while the code uses `androidx.room3.*`, so those exclusions don't currently match.
- CI (`.github/workflows/push.yaml`) runs `./gradlew testAndroidHostTest` and, on `main`, reports
  Kover line coverage. Detekt is not run in CI.

## Common commands

Prefer assembling/installing over full `check` for quick verification:

```bash
./gradlew :androidApp:assembleDebug          # build Android debug APK
./gradlew :androidApp:assembleRelease -Pandroid.experimental.settings.executionProfile=ci  # CI release path
./gradlew testAndroidHostTest                # host unit tests (what CI runs)
./gradlew detektMain                         # lint :app only
./gradlew check                              # all checks (slow)
```

iOS (requires macOS + Xcode):

```bash
xcodebuild build -project iosApp/iosApp.xcodeproj -scheme iosApp \
  -destination 'platform=iOS Simulator,name=iPhone 17 Pro Max,OS=latest'
```

The iOS framework is produced by `:app` (`MonicaApp`); there is no `:shared` module.

## Gotchas

- KSP output is wired into `commonMain`/iOS source dirs by `monica.kmp`; Metro/Room generated code
  resolves without per-module setup. Only main compilations get KSP by default
  (`ksp.allow.all.target.configuration=false`).
- Kotlin warnings become errors when `warningsAsErrors=true` is set in `~/.gradle/gradle.properties`
  (or `-PwarningsAsErrors=true`); keep new code warning-free.
- Coroutine opt-in helpers live in build-logic: `optInExperimentalCoroutinesApi()` and
  `optInFlowPreview()`.
- Room schemas are versioned under `app/schemas`
  (`app/schemas/com.teobaranga.monica.data.MonicaDatabase/1.json`); any schema change must update and
  commit that file. All entities are registered in `MonicaDatabase`.
- Most feature data modules own their tables via an `XxxTableOwner` (contact, activity, genders,
  journal, tips): `MonicaDatabase` implements the owner, `DatabaseComponent` returns it, and the
  feature's `XxxDaoComponent` exposes the DAO. Photo and user DAOs are provided directly in
  `DaosComponent`. Register new entities/DAOs in `MonicaDatabase` (and app-provided DAOs in
  `DaosComponent`).
- Name collision: `com.teobaranga.monica.MonicaApp` is the Composable in `app/`, while `androidApp`'s
  `com.teobaranga.monica.MonicaApp` is the Android `Application`.
- Gradle isolated projects and the configuration cache are enabled; avoid cross-project configuration
  and other constructs that break isolated projects.
