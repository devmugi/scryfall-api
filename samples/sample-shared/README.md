# Sample Shared

Shared Compose Multiplatform UI components used by the Android and iOS samples.

## Overview

This module contains the complete sample application logic and UI, shared between platform entry points. It demonstrates best practices for structuring a KMP app with shared UI.

## Contents

### Entry Point
- **`App.kt`** - Main composable with MaterialTheme setup

### Screen
- **`CardSearchScreen.kt`** - Search UI with results display
- **`CardSearchViewModel.kt`** - State management with StateFlow

### Models
- **`CardUi.kt`** - UI representation of a card
- **`SearchState.kt`** - Search state (loading, success, error)
- **`ViewMode.kt`** - List vs Grid view mode

### UI Components
- **`SearchBar.kt`** - Search input field
- **`ViewModeToggle.kt`** - List/Grid toggle button
- **`CardListItem.kt`** - Card row for list view
- **`CardGridItem.kt`** - Card tile for grid view
- **`CardResults.kt`** - Results container

## Usage

This module is included via Gradle composite build:

```kotlin
// In android-kmp/settings.gradle.kts or ios-kmp/settings.gradle.kts
includeBuild("../sample-shared")

// In build.gradle.kts
commonMain.dependencies {
    implementation("devmugi.sample:sample-shared")
}
```

## Project Structure

```
sample-shared/
├── build.gradle.kts
├── settings.gradle.kts
└── src/
    └── commonMain/
        └── kotlin/devmugi/sample/shared/
            ├── App.kt
            ├── screen/
            │   ├── CardSearchScreen.kt
            │   └── CardSearchViewModel.kt
            ├── model/
            │   ├── CardUi.kt
            │   ├── SearchState.kt
            │   └── ViewMode.kt
            └── ui/
                ├── SearchBar.kt
                ├── ViewModeToggle.kt
                ├── CardListItem.kt
                ├── CardGridItem.kt
                └── CardResults.kt
```

## Related

- [Android Sample](../android-kmp/) - Android entry point
- [iOS Sample](../ios-kmp/) - iOS entry point
- [Main README](../../README.md) - Full library documentation
