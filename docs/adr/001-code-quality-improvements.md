# ADR-001: Code Quality Improvements for scryfall-api Module

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Detekt baseline cleanup and code quality improvements

---

## Summary

This ADR documents the decisions made to address 6 detekt baseline issues in the scryfall-api module, improving code quality while maintaining API stability and usability.

---

## Decision 1: Exception Handling in ScryfallBaseApi

### Problem
The `ScryfallBaseApi.kt` catches `Throwable` when parsing error responses:

```kotlin
val error = try {
    if (isJson) response.call.body<ScryfallError>() else null
} catch (e: Throwable) {
    null
}
```

**Detekt Issues**:
- `SwallowedException`: Exception is silently discarded
- `TooGenericExceptionCaught`: Catching `Throwable` includes system errors (OutOfMemoryError, StackOverflowError)

### Proposed Solutions

1. **Catch SerializationException only**
   - Pros: Precise, only catches expected deserialization failures
   - Cons: Other IO exceptions might slip through (unlikely in this context)

2. **Use runCatching**
   - Pros: Idiomatic Kotlin, concise
   - Cons: Still catches all exceptions, just hides the issue

3. **Log the exception before discarding**
   - Pros: Preserves debugging ability
   - Cons: Adds logging dependency, still catches Throwable

### Chosen Solution: Catch SerializationException

```kotlin
val error = try {
    if (isJson) response.call.body<ScryfallError>() else null
} catch (@Suppress("SwallowedException") e: SerializationException) {
    null  // Expected when response body isn't a valid ScryfallError JSON
}
```

**Rationale**:
- SerializationException is the only expected failure case when the response isn't valid JSON
- System errors (OOM, Stack Overflow) should propagate up, not be silently swallowed
- The `@Suppress("SwallowedException")` annotation documents the intentional suppression with a comment explaining why

---

## Decision 2: Long Parameter List in CardsApi.search()

### Problem
The `search()` method has 10 parameters:

```kotlin
suspend fun search(
    q: String,
    unique: UniqueModes? = null,
    order: SortingCards? = null,
    dir: SortDirection? = null,
    includeExtras: Boolean? = false,
    includeMultilingual: Boolean? = false,
    includeVariations: Boolean? = false,
    page: Int? = 1,
    format: String? = "json",
    pretty: Boolean? = false
): ScryfallList<Card>
```

**Detekt Issue**: `LongParameterList` - exceeds threshold (default: 6)

### Proposed Solutions

1. **Create SearchOptions data class**
   - Pros: Groups related parameters, reusable, cleaner signatures
   - Cons: Breaking API change, adds indirection for simple calls

2. **Use builder pattern**
   - Pros: Fluent API, extensible
   - Cons: More boilerplate, breaking API change

3. **Suppress with annotation**
   - Pros: No API change, documents intentional decision
   - Cons: Doesn't improve the code structure

### Chosen Solution: @Suppress annotation

```kotlin
@Suppress("LongParameterList")
suspend fun search(
    q: String,
    unique: UniqueModes? = null,
    // ... parameters unchanged
): ScryfallList<Card>
```

**Rationale**:
- This is an API wrapper method that directly maps to Scryfall's API query parameters
- All parameters after `q` have sensible defaults - callers typically only specify 1-2
- Changing the signature would break existing consumers
- The Scryfall API defines these parameters, not our design choice
- API wrapper methods commonly need many parameters to maintain fidelity with the wrapped API

---

## Decision 3: Long Parameter List in SearchQueryBuilder.searchWithBuilder()

### Problem
The DSL extension function has 8 parameters:

```kotlin
suspend fun CardsApi.searchWithBuilder(
    unique: UniqueModes? = null,
    order: SortingCards? = null,
    dir: SortDirection? = null,
    includeExtras: Boolean = false,
    includeMultilingual: Boolean = false,
    includeVariations: Boolean = false,
    page: Int = 1,
    builder: SearchQueryBuilder.() -> Unit
): ScryfallList<Card>
```

**Detekt Issue**: `LongParameterList` - exceeds threshold

### Proposed Solutions

Same as Decision 2.

### Chosen Solution: @Suppress annotation

```kotlin
@Suppress("LongParameterList")
suspend fun CardsApi.searchWithBuilder(
    // ... parameters unchanged
): ScryfallList<Card>
```

**Rationale**:
- Same rationale as Decision 2
- This function intentionally mirrors `search()` parameters for consistency
- The DSL builder is the last parameter (trailing lambda), making it ergonomic despite the count

---

## Decision 4: Cyclomatic Complexity in Card.isLegalIn()

### Problem
The function uses a 23-branch `when` expression:

```kotlin
fun Card.isLegalIn(format: String): Boolean {
    val legality = when (format.lowercase()) {
        "standard" -> legalities.standard
        "future" -> legalities.future
        "historic" -> legalities.historic
        // ... 20 more branches
        else -> null
    }
    return legality?.lowercase() == "legal"
}
```

**Detekt Issue**: `CyclomaticComplexMethod` - too many execution paths

### Proposed Solutions

1. **Use property map lookup**
   - Pros: O(1) lookup, cyclomatic complexity = 1, easier to maintain
   - Cons: Slightly more memory for the map, reflection-like access

2. **Use Kotlin reflection**
   - Pros: Dynamic, automatically handles new formats
   - Cons: Reflection not available in all KMP targets, runtime overhead

3. **Keep when expression with @Suppress**
   - Pros: No change, explicit mapping visible
   - Cons: Doesn't improve maintainability, hard to read

### Chosen Solution: Property map lookup

```kotlin
private val FORMAT_PROPERTY_MAP: Map<String, (Legalities) -> String?> = mapOf(
    "standard" to { it.standard },
    "future" to { it.future },
    "historic" to { it.historic },
    // ... all formats
)

fun Card.isLegalIn(format: String): Boolean {
    val getter = FORMAT_PROPERTY_MAP[format.lowercase()] ?: return false
    return getter(legalities)?.lowercase() == "legal"
}
```

**Rationale**:
- Reduces cyclomatic complexity from 24 to 2
- More maintainable: adding a new format is one map entry, not modifying a when expression
- Map lookup is O(1), same as the original when expression
- The format list changes infrequently (when Wizards adds new formats)

---

## Decision 5: Too Many Functions in CardExtensions.kt

### Problem
`CardExtensions.kt` contains 32+ extension functions, exceeding the threshold of 25.

**Detekt Issue**: `TooManyFunctions` - file has too many functions

### Proposed Solutions

1. **Split into multiple files by category**
   - Pros: Clear organization, each file has focused responsibility
   - Cons: More files to navigate, functions spread across files

2. **Use @file:Suppress annotation**
   - Pros: No change, keeps related functions together
   - Cons: Doesn't improve organization

3. **Extract to helper objects**
   - Pros: Namespaced functions
   - Cons: Changes API (`CardFilters.byType()` vs `filterByType()`)

### Chosen Solution: Split into 4 files

| File | Responsibility | Functions |
|------|----------------|-----------|
| `CardTypeExtensions.kt` | Type checking | isCreature, isInstant, isSorcery, isArtifact, isEnchantment, isPlaneswalker, isLand, isBattle |
| `CardPropertyExtensions.kt` | Property queries | isLegalIn, hasColor, isColorless, isMulticolored, hasKeyword |
| `CardFilterExtensions.kt` | List filtering | filterByType, filterByColor, filterLegalIn, etc. (16 functions) |
| `CardSortExtensions.kt` | List sorting | sortByName, sortByCmc, sortByPrice, etc. (6 functions) |

**Rationale**:
- Follows Single Responsibility Principle per file
- Makes navigation easier: need a filter? Look in `CardFilterExtensions.kt`
- Each file stays well under the 25-function threshold
- No API changes - imports work the same way with wildcard imports
- Improves discoverability of available extensions

---

---

## Test Function Naming Convention

**Pattern**: `methodName_scenario_expectedResult`

**Example**: `namedExact_realCall_returnsExpectedCard`

**Decision: Keep current convention**

**Rationale**:
- Widely accepted test naming pattern in JVM ecosystem
- Excluded from detekt checks via `ignoreAnnotated: ['Test']` in `FunctionNaming` rule
- Backtick syntax (`` `named exact real call returns expected card` ``) would be more readable but doesn't work on Android/iOS targets
- Consistent across all test files in the module

---

## Existing @Suppress Annotations Review

### 1. `@Suppress("MagicNumber")` in ScryfallBaseApi.kt:120

**Code:**
```kotlin
@Suppress("MagicNumber")
if (status.value in 500..599) {
```

**Decision: Keep**

**Rationale:**
- HTTP status code ranges are well-defined standards (RFC 7231)
- `500..599` is universally understood as server error range
- Extracting to a constant doesn't improve readability
- Alternative: Define `HTTP_SERVER_ERROR_RANGE = 500..599` constant

### 2. `@Suppress("MagicNumber")` in CardExtensions.kt:335

**Code:**
```kotlin
@Suppress("MagicNumber")
fun List<Card>.sortByRarity(): List<Card> {
    val rarityOrder = mapOf(
        "common" to 1,
        "uncommon" to 2,
        "rare" to 3,
        "mythic" to 4,
        "special" to 5,
        "bonus" to 6
    )
    ...
}
```

**Decision: Keep**

**Rationale:**
- The numbers are ordering weights, not magic numbers with hidden meaning
- The map structure makes the ordering explicit and self-documenting
- Values 1-6 are arbitrary weights; the relative order matters, not the values themselves

---

## Decision 6: EditorConfig and Ktlint Configuration Cleanup

### Problem
The `.editorconfig` and `build.gradle.kts` ktlint configuration had several issues:

| Issue | Location | Problem |
|-------|----------|---------|
| **Experimental rules conflict** | gradle vs editorconfig | `enableExperimentalRules.set(false)` vs `ktlint_experimental = enabled` |
| **Code style mismatch** | gradle comment vs editorconfig | Comment said "official" but editorconfig used `intellij_idea` |
| **Redundant settings** | editorconfig | Both `ij_kotlin_*` IDE settings and `ktlint_standard_*` for same thing |
| **Deprecated setting** | editorconfig | `ktlint_experimental` should be controlled by gradle |

### Proposed Solutions

1. **Remove conflicts, gradle as source of truth**
   - Remove `ktlint_experimental` from editorconfig (gradle controls it)
   - Update gradle comment to match actual style (intellij_idea)
   - Remove redundant `ij_kotlin_*` settings

2. **Keep as-is, suppress warnings**
   - Document conflicts but don't change
   - Risk: confusing for future maintainers

### Chosen Solution: Simplify editorconfig, align with gradle

**Changes to `.editorconfig`:**
- Removed `ktlint_experimental = enabled` (gradle controls this)
- Removed redundant `ij_kotlin_allow_trailing_comma_*` settings
- Re-enabled `class-signature` and `package-name` rules
- Added clear comments explaining each disabled rule

**Changes to `build.gradle.kts`:**
- Updated comment: "Code style is set via .editorconfig (ktlint_code_style = intellij_idea)"
- Clarified that rule configuration is in .editorconfig

**Rationale:**
- Single source of truth: gradle controls experimental flag, editorconfig controls rules
- Clear documentation for each disabled rule with justification
- Removed redundant settings that could cause confusion
- Re-enabled class-signature and package-name (no violations found)

### Disabled Ktlint Rules Summary

| Rule | Justification |
|------|---------------|
| `no-wildcard-imports` | Common in KMP, matches detekt WildcardImport=disabled |
| `trailing-comma-on-call-site` | KMP expect/actual declarations don't need them |
| `trailing-comma-on-declaration-site` | KMP expect/actual declarations don't need them |
| `function-naming` | Tests use snake_case pattern (methodName_scenario_result) |
| `filename` | KMP has platform-specific files (Foo.android.kt, Foo.ios.kt) |

---

## Consequences

### Positive
- Detekt baseline can be deleted (all issues resolved)
- Improved exception handling catches only expected errors
- Better code organization with focused extension files
- Reduced cyclomatic complexity improves maintainability

### Negative
- Source file count increases from 1 to 4 for card extensions
- @Suppress annotations are code smell indicators, though justified here

### Neutral
- No breaking API changes
- No behavioral changes to existing functionality
