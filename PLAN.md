# Android AirPods Pro 2 Controller — Implementation Plan

## Goals

* Provide a **native FOSS Android app** (MIT‑licensed, no ads or tracking) that controls AirPods Pro 2 on devices running **Android 6.0 (API 23) or later**.
* Deliver the **widest possible feature set without requiring root**; enable additional functions when root is available.
* Avoid Apple trademarks and proprietary images; UI follows Material 3 design with dark‑theme support.

---

## Feature Scope

### 🔹 P0 (MVP – non‑root, must‑have)

1. **Connection & Auto‑Reconnect**
   • Primary path – BLE **L2CAP CoC** (PSM `0x1001`) transporting **Apple Accessory Protocol (AAP)** packets.
   • Fallback – BLE **GATT** Battery Service (`0x180F`) when L2CAP is unavailable.
2. **Battery Status** (L / R / Case + charging state).
3. **Noise Control** – toggle **ANC / Transparency / Off** via AAP Opcode `0x0D`.
4. **Compose UI** – Material 3, single‑screen MVP with dark‑theme.

### 🔹 P1 (Extension – non‑root)

5. **Adaptive Audio** & **Conversational Awareness** (AAP Opcodes `0x2E` / `0x28`).
6. **In‑Ear Detection** → automatic play / pause through `MediaSession`.
7. **Case‑Open Pop‑Up** – iOS‑style banner when the lid opens.
8. **Wear OS Tile** & home‑screen **Widget** for quick controls.

### 🔹 P2 (Experimental – root recommended)

9. **Head Gestures** for call answer / media control.
10. **Rename AirPods & advanced settings** (write operations that need patched Bluetooth stack).
11. **Generic support** for other AirPods / Beats generations.

---

## Non‑Functional Requirements

| Category               | Requirement                                                                                        |
| ---------------------- | -------------------------------------------------------------------------------------------------- |
| **Language**           | Kotlin + Coroutines/Flow                                                                           |
| **UI**                 | Jetpack Compose + Compose for Wear OS                                                              |
| **Architecture**       | MVVM ‑‑ module split (see diagram)                                                                 |
| **Permissions**        | `BLUETOOTH_SCAN`, `BLUETOOTH_CONNECT`, `BLUETOOTH_ADVERTISE` (Android 12+ with `neverForLocation`) |
| **Foreground Service** | `foregroundServiceType="connectedDevice"` (Android 14+)                                            |
| **Privacy**            | No analytics, all data stored locally, offline operation                                           |
| **Distribution**       | GitHub releases & F‑Droid; Play Store optional                                                     |

### Architecture modules

```text
ui-compose ─▶ AirPodsViewModel (StateFlow)
               │
               └─▶ core-ble
                     ├─ L2capManager   (API 29+)
                     ├─ GattManager    (all APIs)
                     └─ AutoSwitch      (chooses best path)

core-aap  ──▶ AapParser • AapBuilder • PacketRouter
background ─▶ ConnectedDeviceService (Fg) + AutoReconnectWorker (WorkManager)
```

### Jetpack Compose UI Approach

The entire user interface is implemented with **Jetpack Compose**. Core screens
such as device discovery, connection status, and noise control are written as
Composable functions using Material 3 components. Each screen exposes a preview
to speed up iteration. Compose is also used for dark theme support and will
power future Wear OS tiles.

---

## Key Technical Points

* **BLE L2CAP**: `BluetoothDevice#createL2capChannel(0x1001)` for credit‑based CoC; socket wrapped in Kotlin I/O coroutines.
* **AAP Packet Format**: `Header 4 B` + `Length 2 B` (LE) + `Opcode 1 B` + `Payload`.
* **Fallback Strategy**: if L2CAP connect fails or phone < API 29, switch to `GattManager`‑driven path (Battery Service and any discovered private UUIDs).
* **In‑Ear Detection**: subscribe to AAP Sensor notifications → bridge to `MediaSession` for play/pause.
* **Energy Optimisation**: Scan filter on Apple VID `0x004C`, 10 min scan interval, rely on HCI events while connected.
* **Root Add‑Ons**: optional Magisk module that patches `libbluetooth_jni.so` to fix stack bugs and unlock write operations.

---

## Risks & Mitigations

| Risk                                     | Mitigation                                                         |
| ---------------------------------------- | ------------------------------------------------------------------ |
| **L2CAP unsupported on device**          | Auto‑switch to GATT; publish compatibility list.                   |
| **Apple firmware updates break opcodes** | CI smoke‑tests BLE packets; fail‑fast alerts.                      |
| **High power consumption**               | Intermittent scans; foreground service only while connected.       |
| **Google Play BG restrictions**          | Show mandatory UX prompt; declare correct `foregroundServiceType`. |

---

## OSS References

* **LibrePods** – complete AAP opcode catalogue.
* **CAPod** – Wear OS implementation ideas.
* **AirPodsLikeNormal** – root‑level gesture control.
* Android documentation – BLE L2CAP, GATT, and foreground services.

---

## Implementation Review

The current codebase includes skeleton implementations for BLE connectivity, the AAP packet utilities, and a basic Compose UI. Key components like `L2capManager`, `GattManager`, `PacketRouter`, and foreground/background services exist but many features remain incomplete.

### Observed Issues

- **Missing dependencies** for Kotlin Coroutines and WorkManager in the Gradle configuration.
- The UI lacks actions to start scanning or initiate connections.
- `PacketRouter` only handles a placeholder battery opcode.
- Dependency injection is not yet set up (temporary factory used in `MainActivity`).
- The Wear module is empty.

## Improvement Plan

1. **Add required libraries**
   - Include `kotlinx-coroutines-android` and `androidx.work:work-runtime-ktx` in `libs.versions.toml` and reference them from `app/build.gradle.kts`.
2. **Implement device discovery UI with Compose**
   - Build the scanning screen as a Composable with buttons to start/stop scanning and a list of nearby devices.
   - Allow the user to select a device and trigger `ConnectionManager.connect` from the Composable UI.
3. **Expand packet handling**
   - Implement parsing for noise control opcodes and other essential AAP commands in `PacketRouter`.
4. **Introduce dependency injection**
   - Replace the manual ViewModel factory with a lightweight DI framework (e.g., Hilt or Koin) for easier testing and modularity.
5. **Background and auto-reconnect**
   - Flesh out `ConnectedDeviceService` and `AutoReconnectWorker` to maintain and restore connections when the app is not in the foreground.
6. **Prepare Wear module**
   - Set up a minimal Wear OS module with Compose for Wear OS to host future Tile/Widget code.
7. **Add unit tests**
   - Cover `AapParser`, `AapBuilder`, and connection logic to ensure compatibility with different Android versions.

