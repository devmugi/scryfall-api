# iOS KMP Sample

An iOS app using Compose Multiplatform with Xcode integration.

## Features

- **Card Search** - Full-text search using Scryfall syntax
- **Random Cards** - Fetch random Magic cards
- **Dual View Modes** - Toggle between list and grid views
- **SwiftUI Bridge** - Kotlin Compose UI wrapped in SwiftUI
- **Complete Xcode Project** - Ready to run on simulator or device

## Running

### Option 1: Command Line

```bash
cd samples/ios-kmp
./gradlew iosSimulatorArm64MainBinaries
```

Then open `iosApp/iosApp.xcodeproj` in Xcode and run.

### Option 2: Xcode Only

1. Open `iosApp/iosApp.xcodeproj` in Xcode
2. Select an iOS Simulator target
3. Click Run (⌘R)

The Xcode build phase automatically compiles the Kotlin framework.

## Project Structure

```
ios-kmp/
├── build.gradle.kts
├── src/
│   └── iosMain/
│       └── kotlin/.../MainViewController.kt
├── iosApp/
│   ├── iosApp.xcodeproj
│   └── iosApp/
│       ├── iOSApp.swift
│       └── ContentView.swift
└── settings.gradle.kts
```

## Dependencies

This sample depends on:
- [sample-shared](../sample-shared/) - Shared UI components via composite build
- scryfall-api - The main library

## Related

- [Android Sample](../android-kmp/) - Same app for Android
- [Main README](../../README.md) - Full library documentation
