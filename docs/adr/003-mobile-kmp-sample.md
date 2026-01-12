# ADR-003: Mobile KMP Sample Application

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Creating a self-contained sample app demonstrating scryfall-api usage

---

## Summary

This ADR documents decisions made for creating a minimal, self-contained mobile sample application that demonstrates how to use the `scryfall-api` library in a Kotlin Multiplatform project targeting Android and iOS.

---

## Decision 1: Self-Contained Sample Structure

### Problem
Sample code needs to demonstrate library usage. There are multiple approaches to structuring samples.

### Proposed Solutions

1. **Submodule of parent project**
   - Pros: Can use `project(":scryfall-api")` dependency, shared gradle config
   - Cons: Not copy-pasteable, tied to parent build

2. **Self-contained with published artifact**
   - Pros: Truly standalone, copy-pasteable, demonstrates real-world usage
   - Cons: Requires library to be published first

3. **Include library source code**
   - Pros: No publishing needed, fully standalone
   - Cons: Code duplication, maintenance burden

### Chosen Solution: Self-Contained with Maven Local

The sample lives in `scryfall-api/samples/mobile-kmp/` with its own:
- `settings.gradle.kts` (independent from parent)
- `gradle/libs.versions.toml` (minimal version catalog)
- Gradle wrapper files
- Source code

Consumes `io.github.devmugi:scryfall-api:1.0.0` from `mavenLocal()`.

**Rationale**:
- Users can copy the entire `mobile-kmp` directory and it works standalone
- Demonstrates realistic dependency management (Maven coordinates, not project references)
- Simple setup: `./gradlew :scryfall-api:publishToMavenLocal` prepares the dependency

---

## Decision 2: UI Component Location

### Problem
Should UI components live in `sharedUi` module or in the sample itself?

### Proposed Solutions

1. **Components in sharedUi module**
   - Pros: Reusable across apps, follows existing pattern
   - Cons: Pollutes shared library with sample-specific code, not self-contained

2. **Components in sample only**
   - Pros: Self-contained, no pollution of shared modules
   - Cons: Not reusable (acceptable for samples)

### Chosen Solution: Components in Sample

All UI components (SearchBar, CardListItem, CardGridItem, CardResults, etc.) live within the sample's `src/commonMain/kotlin/` directory.

**Rationale**:
- Sample should be self-contained and copy-pasteable
- Keeps `sharedUi` focused on production components
- Sample demonstrates how to build Scryfall UIs, not provide production-ready components

---

## Decision 3: State Management

### Problem
How to manage UI state in a minimal sample?

### Proposed Solutions

1. **Use Koin/Hilt DI**
   - Pros: Follows best practices, testable
   - Cons: Adds complexity, more dependencies

2. **Manual ViewModel creation**
   - Pros: Simple, no DI framework needed
   - Cons: Not scalable for large apps

3. **Compose-only state (remember)**
   - Pros: Simplest, no extra dependencies
   - Cons: Lost on configuration changes

### Chosen Solution: Manual ViewModel with StateFlow

```kotlin
@Composable
fun CardSearchScreen(
    viewModel: CardSearchViewModel = remember { CardSearchViewModel() }
) {
    val state by viewModel.uiState.collectAsState()
    // ...
}
```

**Rationale**:
- Sample demonstrates scryfall-api usage, not DI patterns
- `remember { }` keeps it simple for a minimal sample
- StateFlow provides reactive updates without extra dependencies
- ViewModel is created once per composition, survives recomposition

---

## Decision 4: Image Loading Library

### Problem
Which library to use for loading card images?

### Options Considered

1. **Coil 3** (coil-compose, coil-network-ktor3)
   - Pros: KMP support, integrates with Ktor (which scryfall-api uses)
   - Cons: Relatively new KMP support

2. **Kamel**
   - Pros: Designed for KMP from the start
   - Cons: Less community adoption

3. **Custom with Ktor**
   - Pros: No extra dependencies
   - Cons: Significant code for caching, memory management

### Chosen Solution: Coil 3

```kotlin
AsyncImage(
    model = card.normalImageUrl,
    contentDescription = card.name,
    contentScale = ContentScale.Fit
)
```

**Rationale**:
- Already used in the main `composeApp` and `sharedUi` modules
- Ktor network backend integrates seamlessly (scryfall-api uses Ktor)
- Simple API with `AsyncImage` composable
- Handles caching, memory management automatically

---

## Decision 5: View Modes (List vs Grid)

### Problem
How to display search results?

### Chosen Solution: Dual View Mode

The sample supports both List and Grid views:

| View | Layout | Image Size | Details |
|------|--------|------------|---------|
| List | `LazyColumn` | `ImageUris.small` (146x204) | Name, type line, oracle text |
| Grid | `LazyVerticalGrid(2 columns)` | `ImageUris.normal` (488x680) | Image only |

**Rationale**:
- Demonstrates both Scryfall image sizes
- Shows different Compose Lazy layouts
- Common UX pattern for card browsers

---

## Decision 6: API Methods Demonstrated

### Problem
Which CardsApi methods to demonstrate?

### Chosen Methods

1. **`search(q: String)`** - Full-text search
   - Returns `ScryfallList<Card>` with pagination info
   - Most common use case

2. **`random()`** - Random card
   - Returns single `Card`
   - Fun feature, simple API

### Not Included (to keep sample minimal)

- `namedExact()` / `namedFuzzy()` - exact name lookups
- `bySetAndCollectorNumber()` - set-specific queries
- `collection()` - batch lookups
- Pagination (`ScryfallList.loadNextPage()`)

**Rationale**:
- Search and random cover 80% of use cases
- Other methods follow same pattern (suspend fun returning Card or ScryfallList)
- Documentation and tests cover advanced usage

---

## Decision 7: Error Handling

### Problem
How detailed should error handling be?

### Chosen Solution: Minimal Error State

```kotlin
sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    data class Success(val cards: List<CardUi>) : SearchState
    data class Error(val message: String) : SearchState
}
```

Error handling in ViewModel:
```kotlin
try {
    val result = api.search(query)
    _uiState.value = uiState.value.copy(searchState = SearchState.Success(result.data.map { it.toUi() }))
} catch (e: Exception) {
    _uiState.value = uiState.value.copy(searchState = SearchState.Error(e.message ?: "Unknown error"))
}
```

**Rationale**:
- Demonstrates basic try-catch pattern
- Shows error state in UI
- Production apps would add retry logic, specific error types

---

## Consequences

### Positive
- Complete, working sample users can copy and modify
- Demonstrates core scryfall-api usage patterns
- Minimal dependencies, easy to understand
- Works on both Android and iOS

### Negative
- No pagination (would add complexity)
- No card detail view (keeps sample focused)
- No offline support / caching strategy

### Neutral
- Requires `publishToMavenLocal` before building sample
- Uses Material3 defaults (no custom theming)

---

## File Structure

```
scryfall-api/samples/mobile-kmp/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml
├── gradlew, gradlew.bat
└── src/
    ├── commonMain/kotlin/devmugi/sample/mobilekmp/
    │   ├── App.kt
    │   ├── CardSearchScreen.kt
    │   ├── CardSearchViewModel.kt
    │   ├── model/
    │   │   ├── CardUi.kt
    │   │   ├── SearchState.kt
    │   │   └── ViewMode.kt
    │   └── ui/
    │       ├── SearchBar.kt
    │       ├── ViewModeToggle.kt
    │       ├── CardListItem.kt
    │       ├── CardGridItem.kt
    │       └── CardResults.kt
    ├── androidMain/
    │   ├── AndroidManifest.xml
    │   └── kotlin/.../MainActivity.kt
    └── iosMain/kotlin/.../MainViewController.kt
```

---

## Usage

```bash
# From project root, publish library
./gradlew :scryfall-api:publishToMavenLocal

# From sample directory, build Android
cd scryfall-api/samples/mobile-kmp
./gradlew assembleDebug
```
