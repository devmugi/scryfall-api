# 📱 Scryfall API Samples

Sample applications demonstrating the [scryfall-api](../README.md) library across multiple platforms.

## Gallery

| Android | Browser (Vite) |
|---------|----------------|
| <img src="images/android-kmp-search-grid.png" width="280" alt="Android Grid"/> | <img src="images/browser-vite-result.png" width="400" alt="Browser Vite"/> |

| Node.js (Kotlin/JS) | Node.js (npm + JS) | Node.js (npm + TS) |
|---------------------|--------------------|--------------------|
| <img src="images/node-kmp-result.png" width="280" alt="Node KMP"/> | <img src="images/node-npm-js-result.png" width="280" alt="Node JS"/> | <img src="images/node-npm-ts-result.png" width="280" alt="Node TS"/> |

## Quick Reference

| Sample | Platform | Build Tool | Description |
|--------|----------|------------|-------------|
| [android-kmp](#android-kmp) | Android | Gradle | Compose Multiplatform app |
| [ios-kmp](#ios-kmp) | iOS | Gradle + Xcode | iOS app with SwiftUI bridge |
| [browser-vite](#browser-vite) | Web | npm + Vite | Browser app with search UI |
| [node-kmp](#node-kmp) | Node.js | Gradle | Kotlin/JS console app |
| [node-npm-js](#node-npm-js) | Node.js | npm | Pure JavaScript console app |
| [node-npm-ts](#node-npm-ts) | Node.js | npm | TypeScript console app |

---

## Sample Structure

```
samples/
├── sample-shared/    # Shared UI components (models + UI)
├── android-kmp/       # Android + iOS sample
├── ios-kmp/          # iOS-only sample with Xcode project
├── node-kmp/         # Node.js console sample (Kotlin/JS)
├── node-npm-js/      # Node.js JavaScript sample (pure npm)
├── node-npm-ts/      # Node.js TypeScript sample (pure npm)
└── browser-vite/     # Browser sample with Vite (pure npm)
```

`android-kmp` and `ios-kmp` depend on `sample-shared` via Gradle composite build.
`node-kmp` uses Kotlin/JS with Gradle.
`node-npm-js`, `node-npm-ts`, and `browser-vite` are pure npm projects (no Gradle).

---

## sample-shared

A Kotlin Multiplatform library containing the complete sample app shared between platform entry points.

**Contains:**
- **App Entry Point**: `App()` composable with MaterialTheme
- **Screen**: `CardSearchScreen` with search UI
- **ViewModel**: `CardSearchViewModel` with CardsApi integration
- **Models**: `CardUi`, `SearchState`, `ViewMode`
- **UI Components**: `SearchBar`, `ViewModeToggle`, `CardListItem`, `CardGridItem`, `CardResults`

This module is consumed by other samples via composite build - no separate publishing required.

---

## android-kmp

A Compose Multiplatform application demonstrating the scryfall-api library for **Android and iOS**.

**Features Demonstrated:**
- **Card Search** - Full-text search using `CardsApi.search(q: String)`
- **Random Cards** - Fetch random cards using `CardsApi.random()`
- **Dual View Modes** - Toggle between list view and grid view
- **Image Loading** - Display card images using Coil 3
- **State Management** - Simple ViewModel pattern with StateFlow

### Building android-kmp

```bash
# Publish scryfall-api first
./gradlew :scryfall-api:publishToMavenLocal

# Build Android
cd scryfall-api/samples/android-kmp
./gradlew assembleDebug

# Build iOS
./gradlew iosSimulatorArm64MainBinaries
```

---

## ios-kmp

An **iOS-only** sample with a complete Xcode project, demonstrating scryfall-api usage specifically for iOS developers.

**Features Demonstrated:**
- Same functionality as android-kmp (search, random, dual view modes)
- Complete Xcode project for running on iOS Simulator or device
- SwiftUI wrapper bridging to Kotlin Compose Multiplatform

### Building ios-kmp

```bash
# Publish scryfall-api first
./gradlew :scryfall-api:publishToMavenLocal

# Build Kotlin framework
cd scryfall-api/samples/ios-kmp
./gradlew iosSimulatorArm64MainBinaries

# Run in Xcode
# Open ios-kmp/iosApp/iosApp.xcodeproj
# Select iOS Simulator target and click Run
```

**Note:** When running from Xcode, it automatically compiles the Kotlin framework via a build phase script.

---

## node-kmp

A minimal **Node.js console** sample demonstrating headless scryfall-api usage.

**Features Demonstrated:**
- Fetch a random card using `CardsApi.random()`
- Print card details to console
- No UI dependencies - pure console output

### Running node-kmp

```bash
# Publish scryfall-api first
./gradlew :scryfall-api:publishToMavenLocal

# Run the sample
cd scryfall-api/samples/node-kmp
./gradlew jsNodeDevelopmentRun
```

**Output:**
```
Random Card: Lightning Bolt
Type: Instant
Text: Lightning Bolt deals 3 damage to any target.
```

---

## node-npm-js

A minimal **pure JavaScript** sample demonstrating npm package consumption. No Kotlin or Gradle required.

> **Note:** Requires `@devmugi/scryfall-api` to be published to npm.

**Features Demonstrated:**
- Fetch a random card using `CardsApiJs.random()`
- Print card details to console
- ESM module format

### Running node-npm-js

```bash
cd scryfall-api/samples/node-npm-js
npm install
npm start
```

**Output:**
```
Random Card: Lightning Bolt
Type: Instant
Text: Lightning Bolt deals 3 damage to any target.
```

---

## node-npm-ts

A minimal **pure TypeScript** sample with full type safety. No Kotlin or Gradle required.

> **Note:** Requires `@devmugi/scryfall-api` to be published to npm.

**Features Demonstrated:**
- Fetch a random card using `CardsApiJs.random()`
- Full TypeScript type checking with generated `.d.ts` files
- IDE autocompletion for all API methods and card properties

### Running node-npm-ts

```bash
cd scryfall-api/samples/node-npm-ts
npm install
npm run build
npm start
```

**Output:**
```
Random Card: Lightning Bolt
Type: Instant
Text: Lightning Bolt deals 3 damage to any target.
```

---

## browser-vite

A **browser-based** sample using Vite, demonstrating the scryfall-api in a web application with search UI.

> **Note:** Requires Node.js 18+ and `@devmugi/scryfall-api` to be published to npm (or use local build).

**Features Demonstrated:**
- Card search using Scryfall query syntax
- Random card fetching
- Responsive grid displaying card images and details
- Modern Vite dev server with hot reload
- Dark theme MTG aesthetic

### Running browser-vite (with local build)

```bash
# Build JS library first
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Run the sample (requires Node.js 18+)
cd scryfall-api/samples/browser-vite
npm install
npm run dev
```

Open http://localhost:5173 in your browser.

**Usage:**
- Type a card name or Scryfall query (e.g., `lightning bolt`, `c:red cmc:1`)
- Click "Search" to find matching cards
- Click "Random Card" to fetch a random Magic card

---

## Prerequisites

- **JDK 17** or higher
- **Android Studio** (for Android development)
- **Xcode** (for iOS development, macOS only)
- **Node.js** (for node-kmp sample)

## Project Structure

```
samples/
├── sample-shared/                   # Shared app (contains everything)
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/commonMain/kotlin/devmugi/sample/shared/
│       ├── App.kt                   # App entry point
│       ├── screen/
│       │   ├── CardSearchScreen.kt
│       │   └── CardSearchViewModel.kt
│       ├── model/
│       │   ├── CardUi.kt
│       │   ├── SearchState.kt
│       │   └── ViewMode.kt
│       └── ui/
│           ├── SearchBar.kt
│           ├── ViewModeToggle.kt
│           ├── CardListItem.kt
│           ├── CardGridItem.kt
│           └── CardResults.kt
├── android-kmp/                      # Android + iOS entry points only
│   ├── build.gradle.kts
│   ├── settings.gradle.kts          # includeBuild("../sample-shared")
│   └── src/
│       ├── androidMain/.../MainActivity.kt
│       └── iosMain/.../MainViewController.kt
├── ios-kmp/                         # iOS-only entry point
│   ├── build.gradle.kts
│   ├── settings.gradle.kts          # includeBuild("../sample-shared")
│   ├── src/iosMain/.../MainViewController.kt
│   └── iosApp/                      # Xcode project
│       ├── Configuration/Config.xcconfig
│       └── iosApp/
│           ├── iOSApp.swift
│           └── ContentView.swift
├── node-kmp/                        # Node.js console sample (Kotlin/JS)
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/jsMain/kotlin/Main.kt
├── node-npm-js/                     # Pure JavaScript sample
│   ├── package.json
│   ├── index.js
│   └── README.md
├── node-npm-ts/                     # Pure TypeScript sample
│   ├── package.json
│   ├── tsconfig.json
│   ├── src/index.ts
│   └── README.md
└── browser-vite/                    # Browser sample with Vite
    ├── package.json
    ├── tsconfig.json
    ├── vite.config.ts
    ├── index.html
    ├── src/
    │   ├── main.ts
    │   ├── style.css
    │   └── vite-env.d.ts
    └── README.md
```

## Technologies Used

| Technology | Purpose |
|------------|---------|
| Kotlin Multiplatform | Shared code across platforms |
| Compose Multiplatform | Cross-platform UI framework |
| Coil 3 | Image loading with KMP support |
| Material Design 3 | UI theming and components |
| kotlinx-coroutines | Async operations |
| StateFlow | Reactive state management |

## Copying Samples

To use a sample independently, copy both:
1. The sample directory (e.g., `ios-kmp/`)
2. The `sample-shared/` directory

Update the `includeBuild` path in `settings.gradle.kts` if the relative path changes.

## Adding More API Features

The scryfall-api library provides many more features not shown in these samples:

- **Sets API** - Query set information
- **Rulings API** - Get card rulings
- **Catalogs API** - Access catalog data (card types, keywords, etc.)
- **Symbology API** - Parse mana costs and symbols
- **Bulk Data API** - Download bulk data exports
- **Search Builder DSL** - Programmatic query construction
- **Extension Functions** - Filter/sort utilities for card lists

See the [main README](../README.md) for complete API documentation.

## Related Documentation

- [scryfall-api README](../README.md) - Complete library documentation
- [ADR-003](../docs/adr/003-android-kmp-sample.md) - Architecture decisions for android-kmp
- [ADR-004](../docs/adr/004-ios-kmp-sample.md) - Architecture decisions for ios-kmp and shared module
- [ADR-005](../docs/adr/005-node-kmp-sample.md) - Architecture decisions for node-kmp
- [ADR-006](../docs/adr/006-npm-support.md) - Architecture decisions for npm support
- [ADR-007](../docs/adr/007-npm-samples.md) - Architecture decisions for npm samples
- [ADR-008](../docs/adr/008-browser-vite-sample.md) - Architecture decisions for browser-vite
- [Scryfall API Reference](https://scryfall.com/docs/api) - Official Scryfall API documentation
