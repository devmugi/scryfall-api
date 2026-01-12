# ADR-006: npm Support for scryfall-api

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Enabling JavaScript/TypeScript developers to use scryfall-api via npm

---

## Summary

This ADR documents decisions made for adding npm publishing support to the scryfall-api library, enabling JavaScript and TypeScript developers to consume the library via `npm install @devmugi/scryfall-api`.

---

## Decision 1: JS Wrapper Layer Architecture

### Problem

Kotlin Multiplatform's `@JsExport` has several limitations that make direct export of commonMain code problematic:
- Default parameters are not supported
- Extension functions cannot be exported
- Sealed classes need explicit subclass exports
- Long type loses precision in JavaScript

### Proposed Solutions

1. **Add @JsExport to all commonMain classes**
   - Pros: Single source of truth
   - Cons: Requires @JsName overloads for every default parameter, pollutes Kotlin API

2. **Create thin JS wrapper layer in jsMain**
   - Pros: Clean separation, JS-idiomatic API, doesn't affect Kotlin consumers
   - Cons: Additional code to maintain

3. **Generate JS API automatically**
   - Pros: No manual work
   - Cons: No existing tooling, would need custom solution

### Chosen Solution: JS Wrapper Layer

Created dedicated wrapper classes in `src/jsMain/kotlin/devmugi/mtgcards/scryfall/js/`:

```
scryfall-api/src/jsMain/kotlin/devmugi/mtgcards/scryfall/js/
├── CardsApiJs.kt   # Promise-returning wrapper for CardsApi
├── Types.kt        # External interfaces for options objects
└── Utils.kt        # CardUtils object with utility functions
```

**Rationale**:
- Keeps commonMain API clean for Kotlin consumers
- Provides idiomatic JavaScript API with options objects
- Extension functions are exposed as static utility methods
- Minimal maintenance overhead - wrapper is thin

---

## Decision 2: Options Object Pattern

### Problem

JavaScript doesn't support default parameters in the same way as Kotlin. Functions with many optional parameters need a JS-friendly pattern.

### Example Issue

```kotlin
// Kotlin - 10 parameters with defaults
suspend fun search(
    q: String,
    unique: UniqueModes? = null,
    order: SortingCards? = null,
    // ... 7 more optional parameters
): ScryfallList<Card>
```

### Chosen Solution: External Interface Options

```kotlin
@JsExport
external interface SearchOptions {
    val unique: String?
    val order: String?
    val dir: String?
    val page: Int?
    // ...
}

@JsExport
class CardsApiJs {
    fun search(q: String, options: SearchOptions?): Promise<ScryfallList<Card>>
}
```

**JavaScript Usage**:
```javascript
// Clean, idiomatic API
const results = await api.search('lightning bolt', {
    unique: 'prints',
    order: 'released',
    page: 2
});
```

**Rationale**:
- Follows JavaScript conventions (options objects are idiomatic)
- Works well with TypeScript (external interfaces generate clean .d.ts)
- Optional parameters are naturally optional in JS objects

---

## Decision 3: Extension Functions as Utility Objects

### Problem

Kotlin extension functions cannot be exported with @JsExport.

### Affected Code

- `Card.isCreature()`, `Card.isInstant()`, etc.
- `Card.hasColor()`, `Card.isLegalIn()`, etc.
- `List<Card>.filterByType()`, `List<Card>.sortByName()`, etc.

### Chosen Solution: Static Utility Object

```kotlin
@JsExport
object CardUtils {
    fun isCreature(card: Card): Boolean
    fun hasColor(card: Card, color: String): Boolean
    fun isLegalIn(card: Card, format: String): Boolean
    // ...
}
```

**JavaScript Usage**:
```javascript
import { CardUtils } from '@devmugi/scryfall-api';

if (CardUtils.isCreature(card)) {
    console.log('Power:', card.power);
}

const creatures = cards.filter(c => CardUtils.isCreature(c));
```

**Rationale**:
- Explicit namespace avoids global pollution
- Works well with tree-shaking
- Familiar pattern for JS developers (similar to lodash)

---

## Decision 4: TypeScript Declarations

### Problem

JavaScript consumers benefit from TypeScript type definitions for IDE autocompletion and type checking.

### Chosen Solution: Kotlin IR Compiler Type Generation

```kotlin
kotlin {
    js(IR) {
        generateTypeScriptDefinitions()
    }
}
```

**Rationale**:
- Built-in Kotlin feature, no extra tooling needed
- Generates accurate .d.ts files from Kotlin types
- @JsExport classes automatically get type definitions

---

## Decision 5: Promise-Based API

### Problem

Kotlin suspend functions need to be exposed to JavaScript.

### Chosen Solution: GlobalScope.promise

```kotlin
@JsExport
class CardsApiJs {
    fun search(q: String, options: SearchOptions?): Promise<ScryfallList<Card>> =
        GlobalScope.promise {
            api.search(q = q, unique = options?.unique?.let { parseUnique(it) })
        }
}
```

**Rationale**:
- Kotlin/JS `GlobalScope.promise` converts suspend functions to Promises
- Familiar async/await pattern for JavaScript developers
- Automatic error propagation

---

## Decision 6: Package Configuration

### Chosen Configuration

```kotlin
js(IR) {
    compilations["main"].packageJson {
        customField("name", "@devmugi/scryfall-api")
        customField("description", "Kotlin Multiplatform library for Scryfall MTG API")
        customField("keywords", listOf("magic", "scryfall", "mtg", "cards", "api"))
        customField("license", "MIT")
    }
}
```

**Rationale**:
- Scoped package (`@devmugi/`) avoids naming conflicts
- Version synced with Maven version for consistency
- Keywords help discoverability on npm

---

## Decision 7: Versioning Strategy

### Problem

Should npm and Maven versions be synchronized?

### Chosen Solution: Synchronized Versions

Both npm and Maven packages use the same version number (e.g., 1.0.0).

**Rationale**:
- Single source of truth for version
- Easier to track feature parity
- Simpler release process

---

## Consequences

### Positive

- JavaScript/TypeScript developers can use the library via npm
- Clean, idiomatic JavaScript API
- Full TypeScript support with generated declarations
- No changes to existing Kotlin API

### Negative

- Additional code to maintain (JS wrappers)
- Not all extension functions exposed (filter/sort utilities omitted initially)
- Enum values passed as strings in JS (slight type safety loss)

### Neutral

- Requires local npm publish for testing
- npm registry publishing to be added later

---

## File Structure

```
scryfall-api/src/jsMain/kotlin/devmugi/mtgcards/scryfall/js/
├── CardsApiJs.kt       # JS-friendly CardsApi wrapper
├── Types.kt            # External interfaces for options
└── Utils.kt            # CardUtils utility object
```

---

## Usage Examples

### JavaScript

```javascript
import { CardsApiJs, CardUtils } from '@devmugi/scryfall-api';

const api = new CardsApiJs();

// Search with options
const results = await api.search('lightning bolt', { unique: 'prints' });

// Get random card
const card = await api.random({ q: 'c:red' });

// Use utility functions
if (CardUtils.isCreature(card)) {
    console.log(`${card.name}: ${card.power}/${card.toughness}`);
}
```

### TypeScript

```typescript
import { CardsApiJs, Card, SearchOptions, CardUtils } from '@devmugi/scryfall-api';

const api = new CardsApiJs();

const options: SearchOptions = { unique: 'art', page: 1 };
const results = await api.search('forest', options);

const creatures: Card[] = results.data.filter(c => CardUtils.isCreature(c));
```

---

## Verification

```bash
# Build JS distribution
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Test in node-kmp sample
cd scryfall-api/samples/node-kmp
./gradlew jsNodeDevelopmentRun

# Local npm test
cd test-project
npm install ../scryfall-api/build/dist/js/productionLibrary
```

---

## Related Documentation

- [samples/README.md](../../samples/README.md) - Sample overview
- [ADR-005](005-node-kmp-sample.md) - Node.js sample decisions
- [Kotlin/JS Documentation](https://kotlinlang.org/docs/js-overview.html)
