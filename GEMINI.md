# Gemini Project Helper

This document provides guidance for the Gemini agent to effectively assist with development tasks in the Monica-KMP project.

## Project Overview

This is a Kotlin Multiplatform project targeting Android and iOS. The project is built with Gradle and follows a modular architecture to promote code sharing and maintainability.

### Key Technologies

- **Kotlin Multiplatform:** For sharing code between Android and iOS.
- **Gradle:** Build system with Kotlin DSL and Version Catalogs.
- **Compose Multiplatform:** Compose Multiplatform for a shared UI between Android and iOS.
- **Ktor** for networking.
- **Detekt:** Static analysis for Kotlin code.
- **Kover:** Code coverage.
- **GitHub Actions:** For CI/CD workflows.

## Project Structure

The project is organized into the following main directories:

- `app/`: The Android application module.
- `iosApp/`: The iOS application module.
- `core/`: Shared core modules, such as `data`, `ui`, and `datetime`.
- `feature/`: Shared feature modules, such as `journal` and `contacts`.
- `component/`: Shared UI components, like `user_avatar`.
- `build-logic/`: Custom Gradle convention plugins for build configuration.
- `gradle/libs.versions.toml`: Defines all project dependencies.
- `config/detekt/detekt.yml`: Configuration for the Detekt static analyzer.

## Code style

- Modifiers should always be the first arguments specified when calling a Composable
- Composable parameters should always be on a new line
- All methods should be no longer than 80 lines
- Use a trailing comma where possible

### Navigation
- The project uses the [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
library for navigation. A `LocalNavigator` exists and is accessible from any Composable. It can be used to navigate
to any destination.

### Dependency Injection
- The project uses kotlin-inject for dependency injection. Additionally, this project uses a custom library called
kotlin-inject-viewmodel for assisted ViewModel injection. For example, this allows:
```kotlin
@Inject
@ContributesViewModel(
    scope = AppScope::class,
    assistedFactory = MyViewModel.Factory::class,
)
class MyViewModel(@Assisted val foo: Foo) : ViewModel() {
    
    @AssistedFactory
    interface Factory {
        operator fun invoke(foo: Foo): MyViewModel
    }
}

// Activity:
class MyActivity : ComponentActivity() {

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory
        get() = (application as App).appComponent.vmFactory
  
    // Assisted ViewModel
    val myAssistedViewModel by viewModels<MyViewModel>(
        extras = defaultViewModelCreationExtras.withCreationCallback<MyViewModel.Factory> { factory ->
            factory(Foo())
        },
    )
}

// Compose
val myAssistedViewModel = injectedViewModel<MyViewModel, MyViewModel.Factory>(
    creationCallback = { factory ->
        factory(Foo())
    },
)
```

## API usage

- Avoid using `androidx.compose.ui.platform.ClipboardManager`, use `androidx.compose.ui.platform.Clipboard` instead

## Common Commands

Here are some common Gradle commands to build, test, and analyze the project.

- **Clean the project:**
  ```bash
  ./gradlew clean
  ```

- **Run all checks (tests, linting, etc.):**
  ```bash
  ./gradlew check
  ```

- **Run unit tests for all modules:**
  ```bash
  ./gradlew testDebugUnitTest
  ```

- **Run Detekt static analysis:**
  ```bash
  ./gradlew detekt
  ```

- **Assemble the Android debug APK:**
  ```bash
  ./gradlew :app:assembleDebug
  ```
  It's preferred to use this command instead of `check` to verify that the build is successful.

- **Build the iOS framework for Xcode:**
  ```bash
  ./gradlew :shared:embedAndSignAppleFrameworkForXcode
  ```
