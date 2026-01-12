# ADR-004: iOS KMP Sample with Shared UI Module

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Extracting shared UI components and creating an iOS-only sample

---

## Summary

This ADR documents decisions made for extracting reusable UI components into a shared module (`sample-shared`) and creating an iOS-only sample (`ios-kmp`) that demonstrates scryfall-api usage on iOS.

---

## Decision 1: Shared UI Module Approach

### Problem
The `mobile-kmp` sample contains UI components (SearchBar, CardListItem, etc.) that could be reused. Creating an iOS-only sample would require duplicating this code.

### Proposed Solutions

1. **Duplicate code in each sample**
   - Pros: Samples remain fully independent
   - Cons: Code duplication, maintenance burden

2. **Extract to shared module with composite build**
   - Pros: Code reuse, single source of truth
   - Cons: Samples depend on another module

3. **Publish shared module to Maven**
   - Pros: Clean separation, no composite build complexity
   - Cons: Requires additional publishing step

### Chosen Solution: Shared Module with Composite Build

Created `scryfall-api/samples/sample-shared/` containing:
- Model classes: `CardUi`, `SearchState`, `ViewMode`
- UI components: `SearchBar`, `ViewModeToggle`, `CardListItem`, `CardGridItem`, `CardResults`

Samples use Gradle composite build to include it:
```kotlin
// settings.gradle.kts
includeBuild("../sample-shared") {
    dependencySubstitution {
        substitute(module("devmugi.sample:sample-shared")).using(project(":"))
    }
}

// build.gradle.kts
implementation("devmugi.sample:sample-shared")
```

**Rationale**:
- No additional publishing step required (unlike Maven approach)
- Changes to shared module immediately reflected during development
- Samples can still be copied together (`sample/` + `sample-shared/`) and work standalone
- Clean separation: UI components in shared, API logic in each sample

---

## Decision 2: Code Split Strategy

### Problem
What code should be shared vs. kept in individual samples?

### Chosen Split

**Shared (`sample-shared`) - Contains everything:**
- `App.kt` - Entry point with MaterialTheme
- `screen/CardSearchScreen.kt` - Screen composition
- `screen/CardSearchViewModel.kt` - Contains CardsApi calls
- `model/CardUi.kt` - UI data class
- `model/SearchState.kt` - State sealed interface
- `model/ViewMode.kt` - View mode enum
- `ui/SearchBar.kt` - Search input component
- `ui/ViewModeToggle.kt` - View mode toggle chips
- `ui/CardListItem.kt` - List view card item
- `ui/CardGridItem.kt` - Grid view card item
- `ui/CardResults.kt` - Results container

**Sample-specific (in `mobile-kmp` and `ios-kmp`):**
- Platform entry points only (MainActivity, MainViewController)

**Rationale**:
- Maximum code reuse - samples only provide platform bootstrapping
- The shared module contains the complete app logic
- Simplifies maintenance - only one place to update business logic
- Samples demonstrate platform-specific setup (Xcode project, Android manifest)

---

## Decision 3: iOS-Only Sample Structure

### Problem
How to structure an iOS-only sample?

### Chosen Structure

The `ios-kmp` sample has:

**Gradle/Kotlin:**
```
ios-kmp/
├── build.gradle.kts        # iOS-only targets (no Android)
├── settings.gradle.kts     # With includeBuild for sample-shared
├── gradle/libs.versions.toml
└── src/
    ├── commonMain/         # App, Screen, ViewModel
    └── iosMain/            # MainViewController
```

**Xcode Project:**
```
ios-kmp/iosApp/
├── Configuration/Config.xcconfig    # Bundle ID, version
├── iosApp/
│   ├── iOSApp.swift                # SwiftUI @main entry
│   ├── ContentView.swift           # Kotlin framework bridge
│   ├── Info.plist
│   └── Assets.xcassets/
└── iosApp.xcodeproj/               # Xcode project with build phase
```

**Key differences from `mobile-kmp`:**
- No Android plugin or `androidMain` source set
- Only iOS framework binary output (`IosKmpSample`)
- Xcode project included for running on iOS simulator/device

---

## Decision 4: Xcode Integration

### Problem
How should the iOS sample integrate with Xcode?

### Chosen Solution: Xcode Project with Gradle Build Phase

The Xcode project includes a "Compile Kotlin Framework" build phase that runs:
```bash
./gradlew embedAndSignAppleFrameworkForXcode
```

SwiftUI acts as a thin wrapper:
```swift
import IosKmpSample

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }
}
```

**Rationale**:
- Follows the same pattern as the main project's `iosApp`
- Xcode handles iOS-specific lifecycle and signing
- Gradle handles all Kotlin compilation
- Simple bridge layer (SwiftUI wraps ComposeUIViewController)

---

## Decision 5: Maintaining Self-Contained Nature

### Problem
How do samples remain self-contained when they share code?

### Chosen Solution: Copy-Together Pattern

To use a sample independently:
1. Copy the sample directory (e.g., `ios-kmp/`)
2. Copy the `sample-shared/` directory
3. Update `includeBuild` path in `settings.gradle.kts` if needed

Both directories together form a complete, working sample.

**Rationale**:
- Samples aren't truly standalone, but the pair is self-contained
- Better than code duplication for maintenance
- Clear dependency: one shared module, multiple consuming samples

---

## Consequences

### Positive
- Code reuse between samples reduces duplication
- Single source of truth for UI components
- iOS-only sample demonstrates iOS-specific setup
- Samples work with composite build (no extra publishing)

### Negative
- Samples depend on `sample-shared` (not fully standalone)
- Three version catalogs to keep in sync
- More complex structure than single-sample approach

### Neutral
- Xcode project copied from main project's `iosApp`
- Same Kotlin/Compose patterns as `mobile-kmp`

---

## File Structure

```
scryfall-api/samples/
├── README.md
├── sample-shared/                    # Complete app (everything shared)
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── gradle/libs.versions.toml
│   └── src/commonMain/kotlin/devmugi/sample/shared/
│       ├── App.kt
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
├── mobile-kmp/                       # Android + iOS entry points
│   ├── build.gradle.kts
│   ├── settings.gradle.kts           # includeBuild sample-shared
│   └── src/
│       ├── androidMain/.../MainActivity.kt
│       └── iosMain/.../MainViewController.kt
└── ios-kmp/                          # iOS-only entry point
    ├── build.gradle.kts
    ├── settings.gradle.kts           # includeBuild sample-shared
    ├── src/iosMain/.../MainViewController.kt
    └── iosApp/                       # Xcode project
        ├── Configuration/Config.xcconfig
        └── iosApp/
            ├── iOSApp.swift
            └── ContentView.swift
```

---

## Usage

```bash
# From project root, publish library
./gradlew :scryfall-api:publishToMavenLocal

# Build ios-kmp Kotlin framework
cd scryfall-api/samples/ios-kmp
./gradlew iosSimulatorArm64MainBinaries

# Run in Xcode
# Open ios-kmp/iosApp/iosApp.xcodeproj
# Select iOS Simulator target and click Run
```
