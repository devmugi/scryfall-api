# ADR-002: Integration Testing Strategy for scryfall-api Module

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Migration from Android host tests to JVM-based integration tests

---

## Summary

This ADR documents the decisions made to establish a robust integration testing strategy for the scryfall-api module, replacing Android host tests with JVM tests.

---

## Background

The scryfall-api module originally had integration tests in `androidHostTest` source set. These tests make real HTTP calls to the Scryfall API to verify the client implementation works correctly against the live service.

**Problems with Android host tests:**
- Requires Android SDK toolchain even for pure JVM testing
- Slower build times due to Android compilation overhead
- Less portable across development environments
- The scryfall-api module is a pure KMP library with no Android-specific code

**Goal:** Run integration tests on JVM for faster, simpler test execution.

---

## Options Analyzed

### Option A: Custom Compilation within JVM Target

**Approach:** Create a custom `integrationTest` compilation using KMP's `compilations.create()` API.

```kotlin
kotlin {
    jvm {
        compilations {
            val main by getting
            val integrationTest by creating {
                associateWith(main)
                defaultSourceSet {
                    dependencies {
                        implementation(libs.kotlin.test)
                        implementation(libs.ktor.client.okhttp)
                    }
                }
            }
        }
    }
}
```

**Source:** [Kotlin Multiplatform Configure Compilations](https://kotlinlang.org/docs/multiplatform/multiplatform-configure-compilations.html)

**Result: FAILED**

**Technical Findings:**
- Dependencies declared in `defaultSourceSet.dependencies { }` are not resolved to the compilation's classpath
- The `jvmIntegrationTestImplementation` configuration exists but is marked as "cannot be resolved"
- Explicit configuration names (`jvmIntegrationTestCompilationImplementation`) also fail
- This appears to be a limitation/bug in the KMP Gradle plugin's dependency pipeline for custom compilations

**Error encountered:**
```
e: Unresolved reference 'Test'
e: Unresolved reference 'runBlocking'
```

---

### Option B: Multiple JVM Targets

**Approach:** Declare two JVM targets to get separate source sets.

```kotlin
kotlin {
    jvm()                    // Creates jvmMain, jvmTest
    jvm("integration")       // Creates integrationMain, integrationTest
}
```

**Source:** [Kotlin Discussions on Multiple JVM Targets](https://discuss.kotlinlang.org/t/multiplatform-and-mulitple-jvm-targets/11866)

**Result: NOT RECOMMENDED**

**Reasons:**
- Kotlin produces **deprecation warnings** for multiple targets of the same type
- Creates completely separate targets (not just test separation)
- Requires attribute disambiguation for dependency resolution
- Overkill for test separation use case
- Future Kotlin versions may remove support for this pattern

---

### Option C: Use Standard jvmTest Source Set

**Approach:** Place all JVM tests (unit and integration) in the standard `jvmTest` source set. Use naming conventions or Gradle filtering to distinguish test types.

```kotlin
kotlin {
    jvm()

    sourceSets {
        jvmTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.okhttp)
        }
    }
}
```

**Result: CHOSEN SOLUTION**

**Benefits:**
- Works reliably with standard KMP dependency resolution
- No custom configuration required
- Integration tests named with `*IntegrationTest.kt` suffix are easily identifiable
- Can use Gradle `--tests` filter to run specific test categories

---

## Chosen Solution: jvmTest with Naming Convention

### Implementation

1. **Source Location:** `src/jvmTest/kotlin/devmugi/mtgcards/scryfall/api/`

2. **Naming Convention:**
   - Unit tests: `*Test.kt` (e.g., `CardsApiTest.kt`)
   - Integration tests: `*IntegrationTest.kt` (e.g., `CardsApiSearchIntegrationTest.kt`)

3. **Dependencies in build.gradle.kts:**
```kotlin
sourceSets {
    jvmTest.dependencies {
        implementation(libs.kotlin.test)
        implementation(libs.kotlinx.coroutines.test)
        implementation(libs.ktor.client.okhttp)
    }
}
```

### Test Execution Commands

```bash
# Run all JVM tests (unit + integration)
./gradlew :scryfall-api:jvmTest

# Run only integration tests
./gradlew :scryfall-api:jvmTest --tests "*IntegrationTest"

# Run only unit tests (exclude integration)
./gradlew :scryfall-api:jvmTest --tests "*Test" --tests "!*IntegrationTest"

# Run specific integration test class
./gradlew :scryfall-api:jvmTest --tests "CardsApiSearchIntegrationTest"
```

### Test Files

| Test File | Type | Description |
|-----------|------|-------------|
| `BulkDataApiIntegrationTest.kt` | Integration | Tests bulk data endpoints |
| `CardSymbolApiIntegrationTest.kt` | Integration | Tests card symbol endpoints |
| `CardsApiAutocompleteIntegrationTest.kt` | Integration | Tests autocomplete endpoint |
| `CardsApiByIdIntegrationTest.kt` | Integration | Tests card retrieval by ID |
| `CardsApiCollectionIntegrationTest.kt` | Integration | Tests collection endpoint |
| `CardsApiNamedIntegrationTest.kt` | Integration | Tests named card lookup |
| `CardsApiRandomIntegrationTest.kt` | Integration | Tests random card endpoint |
| `CardsApiSearchIntegrationTest.kt` | Integration | Tests search endpoint |
| `CatalogsApiIntegrationTest.kt` | Integration | Tests catalog endpoints |
| `RulingsApiIntegrationTest.kt` | Integration | Tests rulings endpoints |
| `SearchQueryBuilderIntegrationTest.kt` | Integration | Tests DSL builder with real API |
| `SetsApiIntegrationTest.kt` | Integration | Tests sets endpoints |
| `*ApiTest.kt` (14 files) | Unit | Mock-based unit tests |

---

## Removed Infrastructure

As part of this migration, the following Android-specific test infrastructure was removed:

| Removed | Reason |
|---------|--------|
| `src/androidHostTest/` | Integration tests moved to jvmTest |
| `src/androidDeviceTest/` | Not needed for this library module |
| `withHostTestBuilder { }` | Android host test configuration |
| `withDeviceTestBuilder { }` | Android device test configuration |

---

## Consequences

### Positive
- Faster test execution without Android toolchain overhead
- Simpler CI/CD configuration (no Android SDK required for tests)
- Standard KMP patterns that work reliably
- Clear naming convention distinguishes test types

### Negative
- Unit and integration tests share the same source set
- Must use `--tests` filter for selective execution
- No compile-time separation (all tests compile together)

### Neutral
- Integration tests still require network access to Scryfall API
- Test count: 207 total (97 API tests, 47 core tests, 63 model tests)

---

## Future Considerations

If Kotlin Multiplatform improves support for custom compilations with proper dependency resolution, revisiting Option A would provide cleaner separation. Monitor these resources:

- [KT-61573](https://youtrack.jetbrains.com/issue/KT-61573) - KMP custom compilation dependencies
- [Kotlin Multiplatform Roadmap](https://kotlinlang.org/docs/roadmap.html)

---

## References

- [Kotlin Multiplatform Configure Compilations](https://kotlinlang.org/docs/multiplatform/multiplatform-configure-compilations.html)
- [Kotlin Multiplatform Advanced Project Structure](https://kotlinlang.org/docs/multiplatform/multiplatform-advanced-project-structure.html)
- [Kotlin Discussions: Multiple JVM Targets](https://discuss.kotlinlang.org/t/multiplatform-and-mulitple-jvm-targets/11866)
