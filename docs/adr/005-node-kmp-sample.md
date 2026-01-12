# ADR-005: Node.js KMP Console Sample

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Creating a minimal Node.js console sample demonstrating headless scryfall-api usage

---

## Summary

This ADR documents decisions made for creating a minimal Node.js console sample that demonstrates how to use the `scryfall-api` library in a Kotlin/JS environment targeting Node.js.

---

## Decision 1: Standalone Sample Structure

### Problem
How should the Node.js sample be structured relative to other samples?

### Proposed Solutions

1. **Include sample-shared module**
   - Pros: Consistent with mobile samples
   - Cons: sample-shared contains UI code, unnecessary for console app

2. **Standalone with minimal dependencies**
   - Pros: Simplest possible setup, no UI dependencies
   - Cons: Cannot share code with other samples

### Chosen Solution: Standalone Sample

The sample lives in `scryfall-api/samples/node-kmp/` as a completely standalone project with no dependency on `sample-shared`.

**Rationale**:
- Console sample has no UI, so sample-shared (which contains Compose UI) is irrelevant
- Demonstrates the simplest possible scryfall-api integration
- Users can copy the directory and have a working Node.js project immediately

---

## Decision 2: Kotlin/JS Target Configuration

### Problem
Which Kotlin/JS configuration to use for Node.js?

### Options Considered

1. **Legacy compiler**
   - Pros: Older, more documented
   - Cons: Deprecated, slower

2. **IR compiler with nodejs()**
   - Pros: Modern, better optimization, recommended
   - Cons: Slightly different semantics

### Chosen Solution: IR Compiler with nodejs()

```kotlin
kotlin {
    js(IR) {
        nodejs()
        binaries.executable()
    }
}
```

**Rationale**:
- IR compiler is the current standard for Kotlin/JS
- `nodejs()` configures proper Node.js environment
- `binaries.executable()` generates runnable JavaScript

---

## Decision 3: Coroutine Scope Management

### Problem
How to handle coroutines in a Node.js environment?

### Proposed Solutions

1. **runBlocking**
   - Pros: Simple, blocks until complete
   - Cons: Not available in Kotlin/JS

2. **MainScope with launch**
   - Pros: Works in Kotlin/JS, standard coroutine pattern
   - Cons: Process exits when main() ends (before coroutine completes)

3. **GlobalScope**
   - Pros: Simple
   - Cons: Not recommended, harder to manage lifecycle

### Chosen Solution: MainScope with launch

```kotlin
fun main() {
    val api = CardsApi()
    MainScope().launch {
        val card = api.random()
        println("Random Card: ${card.name}")
        println("Type: ${card.typeLine}")
        println("Text: ${card.oracleText ?: "No text"}")
    }
}
```

**Rationale**:
- MainScope() is the standard approach for UI-less Kotlin/JS applications
- The coroutine keeps the Node.js event loop alive until completion
- Simple and straightforward for a sample

---

## Decision 4: API Method Demonstrated

### Problem
Which CardsApi method to demonstrate in a minimal console sample?

### Options Considered

1. **search()** - Requires query input, returns list
2. **random()** - No input needed, returns single card
3. **namedExact()** - Requires exact card name

### Chosen Solution: random()

```kotlin
val card = api.random()
```

**Rationale**:
- No user input required - sample works out of the box
- Returns a complete Card object to demonstrate
- Fun and engaging - shows different card each run
- Single card is easier to display than a list

---

## Decision 5: Output Format

### Problem
What card information to display?

### Chosen Fields

| Field | Source | Purpose |
|-------|--------|---------|
| Name | `card.name` | Primary identifier |
| Type | `card.typeLine` | Card type (Creature, Instant, etc.) |
| Text | `card.oracleText` | Rules text |

**Rationale**:
- Three fields demonstrate accessing different Card properties
- Nullable handling shown with `oracleText ?: "No text"`
- Human-readable output suitable for console

---

## Decision 6: Dependency Management

### Problem
How to manage dependencies for the sample?

### Chosen Solution: Version Catalog with mavenLocal

```toml
# gradle/libs.versions.toml
[versions]
kotlin = "2.3.0"
coroutines = "1.10.2"

[libraries]
scryfall-api = { module = "io.github.devmugi:scryfall-api", version = "1.0.0" }
kotlinx-coroutines-core = { module = "org.jetbrains.kotlinx:kotlinx-coroutines-core", version.ref = "coroutines" }

[plugins]
kotlin-multiplatform = { id = "org.jetbrains.kotlin.multiplatform", version.ref = "kotlin" }
```

**Rationale**:
- Version catalog provides clear dependency overview
- Consistent with other samples
- scryfall-api consumed from mavenLocal (same as mobile samples)

---

## Consequences

### Positive
- Simplest possible scryfall-api sample
- No UI framework dependencies
- Runs on any system with Node.js
- Quick to build and execute
- Demonstrates Kotlin/JS usage of the library

### Negative
- No user interaction (just prints one random card)
- No error handling UI (errors print to console)
- Single-shot execution (doesn't loop or accept input)

### Neutral
- Requires Node.js installed
- Requires `publishToMavenLocal` before building

---

## File Structure

```
scryfall-api/samples/node-kmp/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml
├── gradlew, gradlew.bat
└── src/jsMain/kotlin/
    └── Main.kt
```

---

## Usage

```bash
# From project root, publish library
./gradlew :scryfall-api:publishToMavenLocal

# From sample directory, run
cd scryfall-api/samples/node-kmp
./gradlew jsNodeDevelopmentRun
```

**Expected Output:**
```
Random Card: Lightning Bolt
Type: Instant
Text: Lightning Bolt deals 3 damage to any target.
```

---

## Related Documentation

- [samples/README.md](../../samples/README.md) - Sample overview
- [ADR-003](003-mobile-kmp-sample.md) - Mobile sample decisions
- [ADR-004](004-ios-kmp-sample.md) - iOS sample and shared module decisions
