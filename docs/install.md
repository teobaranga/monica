
To try out the app, you can either build the app locally or get the latest build from the CI.

### Build

1. Follow the official Jetbrains docs to [setup your environment](https://www.jetbrains.com/help/kotlin-multiplatform-dev/quickstart.html#set-up-the-environment)
for Kotlin Multiplatform development. This also includes Xcode installation for iOS development.
2. Open the project in Android Studio
3. Run the app:
    - Android: run the `app` configuration
    - iOS: run the `MonicaApp` configuration

### Download the latest CI build

1. Navigate to the [Push Action](https://github.com/teobaranga/monica/actions/workflows/push_self_runner.yaml?query=branch%3Amain)
results
2. Click on the latest build
3. Scroll down to the `Artifacts` section

... the next steps will depend on the platform you want to try out the app on.

=== "Android"

      4. Download the `app-release` artifact
      5. Unzip and install the apk file on your device

=== "iOS"

    TODO
