# Repository Overview

This repo contains **ADR-Pods**, a Kotlin-based Android application for controlling AirPods Pro 2.

## Key Points
- Build system: **Gradle** using Kotlin DSL (`build.gradle.kts`).
- Minimum Android API level: **23**.
- Main module: `app` with source under `app/src/main/java/com/kyoya/adrpods/`.
- Packages: `aap`, `ble`, `background`, `ui` (Compose UI with MVVM).
- Additional module `wear` (placeholder) and `buildSrc` for build logic.

## Development Workflow
1. Implement code in the correct package.
2. For UI changes, edit `MainScreen.kt` and related files.
3. Manage dependencies via `gradle/libs.versions.toml` then reference them in module `build.gradle.kts`.
4. Build with `gradle assembleDebug` (or `gradle build`).
5. Run unit tests with `gradle test` (instrumentation tests use `gradle connectedAndroidTest`).

## References
- See `README.md` for the project goals and feature scope.
- `GEMINI.md` provides an architectural summary and package descriptions.
