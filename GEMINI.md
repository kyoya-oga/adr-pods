## Gemini Project Helper

This document provides a quick reference for the Gemini agent to understand the ADR-Pods project.

### Project Overview

- **Name**: ADR-Pods (Android AirPods Pro 2 Controller)
- **Goal**: A native, open-source Android application to control and manage Apple AirPods Pro 2.
- **License**: MIT
- **Target API**: Android 6.0 (API 23) and later.

### Tech Stack & Architecture

- **Language**: Kotlin, with extensive use of Coroutines and Flow for asynchronous operations.
- **UI**: Jetpack Compose with a Material 3 design.
- **Architecture**: Model-View-ViewModel (MVVM).
- **Build System**: Gradle with Kotlin DSL (`.kts`). Dependencies are managed centrally in `gradle/libs.versions.toml`.

### Module & Package Structure

The application is structured into the following core packages within `app/src/main/java/com/kyoya/adrpods/`:

- **`aap`**: Handles the Apple Accessory Protocol (AAP). Contains classes for parsing (`AapParser`), building (`AapBuilder`), and routing (`PacketRouter`) AAP packets.
- **`ble`**: Manages Bluetooth Low Energy (BLE) connectivity.
  - `BleScanner`: Scans for nearby Apple devices.
  - `L2capManager`: Handles the primary, high-speed L2CAP connection (API 29+).
  - `GattManager`: Provides a fallback GATT connection for older devices or when L2CAP fails.
  - `ConnectionManager`: A facade that selects the appropriate connection method (L2CAP/GATT) and manages the overall connection state.
- **`background`**: Contains components for background operations.
  - `ConnectedDeviceService`: A foreground service to maintain the BLE connection when the app is not in the foreground.
  - `AutoReconnectWorker`: A `WorkManager` task to automatically reconnect to known devices.
- **`ui`**: The user interface layer.
  - `MainActivity`: The main entry point of the application, responsible for requesting permissions.
  - `MainScreen`: The primary Composable UI screen.
  - `AirPodsViewModel`: The ViewModel that connects the UI to the backend (`ConnectionManager`, `PacketRouter`).
  - `theme`: Contains the Jetpack Compose theme files.

### Key Technical Points

- **Primary Connection**: BLE L2CAP CoC using PSM `0x1001` for AAP transport.
- **Fallback Connection**: Standard BLE GATT Battery Service (`0x180F`).
- **Permissions**: The app requires `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, and `FOREGROUND_SERVICE` permissions, which are handled in `MainActivity`.

### Development Workflow

1.  **Code Implementation**: Follow the established package structure. Place new classes in their respective `aap`, `ble`, `background`, or `ui` packages.
2.  **UI Development**: All UI is built with Jetpack Compose. Modify `MainScreen.kt` for UI changes and `AirPodsViewModel.kt` for UI logic.
3.  **Dependency Management**: Add new libraries to the `gradle/libs.versions.toml` file and then reference them in the appropriate `build.gradle.kts` file.
4.  **Building**: The project can be built using standard Gradle commands (e.g., `./gradlew assembleDebug`).
