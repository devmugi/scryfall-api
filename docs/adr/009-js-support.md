# ADR-009: JavaScript Support Strategy

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Addressing Kotlin/JS property name mangling and providing stable JavaScript API

---

## Summary

This ADR documents the strategy for supporting JavaScript consumers of the scryfall-api library, specifically addressing the Kotlin/JS IR compiler's property name mangling and the decision to use accessor functions rather than `@JsExport` on commonMain data classes.

---

## Problem Statement

### Property Name Mangling

The Kotlin/JS IR compiler mangles property names for optimization:

```javascript
// What JavaScript sees in browser console:
Card { k5l_1: null, l5l_1: 'uuid', m5l_1: 'en', ... }

// What developers expect:
Card { arenaId: null, id: 'uuid', language: 'en', ... }
```

### Impact

- Direct property access (e.g., `card.name`) returns `undefined`
- Mangled names change between Kotlin versions
- Code using mangled names breaks on any Kotlin/compiler update
- No IDE autocompletion or type safety

---

## Alternatives Evaluated

### Option A: Accessor Functions in jsMain (Chosen)

Create `@JsExport` utility objects with accessor functions:

```kotlin
@JsExport
object CardUtils {
    @JsName("getName")
    fun getName(card: Card): String = card.name

    @JsName("getTypeLine")
    fun getTypeLine(card: Card): String? = card.typeLine
}

@JsExport
object ListUtils {
    @JsName("getData")
    fun <T> getData(list: ScryfallList<T>): Array<T> = list.data.toTypedArray()

    @JsName("getTotalCards")
    fun <T> getTotalCards(list: ScryfallList<T>): Int = list.totalCards ?: list.data.size
}
```

**Pros:**
- Clean separation of concerns
- No changes to commonMain
- Works with all Kotlin features (default parameters, extensions, sealed classes)
- Stable API - accessor functions don't change with compiler versions
- Full type safety in Kotlin code

**Cons:**
- Requires utility functions for property access
- More boilerplate in jsMain
- JS consumers use `any` type for raw objects

### Option B: @JsExport on commonMain Data Classes (Rejected)

Add `@JsExport` annotation to all data classes:

```kotlin
@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class Card(
    val id: String,
    val name: String,
    // ... 60+ properties
)
```

**Rejected because:**

| Issue | Severity | Description |
|-------|----------|-------------|
| Default parameters | HIGH | `@JsExport` doesn't support default parameters. All 60+ Card properties have defaults - would require overloaded constructors |
| Long type | HIGH | `Long` not supported in JS (no 64-bit int). BulkData.size would need changing to Double or String |
| Nullable collections | MEDIUM | `List<String>?` types may have issues |
| Generic types | MEDIUM | `ScryfallList<T>` generic causes TypeScript generation issues |
| Code bloat | MEDIUM | Every data class exported = larger JS bundle |
| Experimental API | LOW | `@JsExport` is still `@ExperimentalJsExport` |
| Multiplatform pollution | MEDIUM | Annotations affect all platforms even though only JS needs them |
| Extension functions | HIGH | Would STILL not work - extensions cannot be `@JsExport` |

### Option C: Hybrid - Export Simple Models Only (Rejected)

Export simple data classes, wrap complex ones:

```kotlin
// Simple - export directly
@JsExport
data class ImageUris(val small: String?, val normal: String?, val large: String?)

// Complex - wrap with accessors
object CardUtils { ... }
```

**Rejected because:**
- Inconsistent API confuses users
- Still requires wrappers for Card (the main model)
- More complex to maintain and document

### Option D: JSON Serialization Export (Rejected)

Convert Kotlin objects to plain JS objects via JSON:

```kotlin
@JsExport
object CardUtils {
    @JsName("toJsObject")
    fun toJsObject(card: Card): dynamic = JSON.parse(Json.encodeToString(card))
}
```

**Rejected because:**
- Double conversion overhead (Kotlin -> JSON -> JS)
- Memory and CPU cost on every access
- Loses object identity

---

## Chosen Solution: Option A - Accessor Functions

### Architecture

```
commonMain/
├── api/               -->  Pure Kotlin API classes (CardsApi, SetsApi, etc.)
└── models/            -->  Pure Kotlin data classes (Card, Set, etc.)

jsMain/js/
├── CardsApiJs.kt      -->  @JsExport wrapper for CardsApi
├── SetsApiJs.kt       -->  @JsExport wrapper for SetsApi
├── RulingsApiJs.kt    -->  @JsExport wrapper for RulingsApi
├── CatalogsApiJs.kt   -->  @JsExport wrapper for CatalogsApi
├── CardSymbolApiJs.kt -->  @JsExport wrapper for CardSymbolApi
├── BulkDataApiJs.kt   -->  @JsExport wrapper for BulkDataApi
├── Types.kt           -->  External interfaces for options objects
└── Utils.kt           -->  Accessor utilities for all models
```

### Exported API Classes

| API Class | Description | Methods |
|-----------|-------------|---------|
| `CardsApiJs` | Card search and retrieval | `search`, `autocomplete`, `namedExact`, `namedFuzzy`, `random`, `bySetAndCollectorNumber`, `byScryfallId`, `byMultiverseId`, `byMtgoId`, `byArenaId`, `byTcgplayerId` |
| `SetsApiJs` | Set information | `all`, `byCode`, `byTcgPlayerId`, `byId` |
| `RulingsApiJs` | Card rulings | `byCardId`, `byMultiverseId`, `byMtgoId`, `byArenaId`, `byCollectorId` |
| `CatalogsApiJs` | Catalog data | `cardNames`, `artistNames`, `wordBank`, `supertypes`, `cardTypes`, `creatureTypes`, `planeswalkerTypes`, `landTypes`, `artifactTypes`, `battleTypes`, `enchantmentTypes`, `spellTypes`, `powers`, `toughnesses`, `loyalties`, `watermarks`, `keywordAbilities`, `keywordActions`, `abilityWords`, `flavorWords` |
| `CardSymbolApiJs` | Symbology | `all`, `parseMana` |
| `BulkDataApiJs` | Bulk data | `all`, `byId`, `byType` |

### Exported Utility Objects

| Utility | Model | Key Methods |
|---------|-------|-------------|
| `CardUtils` | `Card` | `getName`, `getTypeLine`, `getOracleText`, `getManaCost`, `getCmc`, `getPower`, `getToughness`, `getNormalImageUrl`, `isCreature`, `isLegalIn`, etc. |
| `ListUtils` | `ScryfallList<T>` | `getData`, `getTotalCards`, `hasMore`, `getNextPage` |
| `SetUtils` | `Set` | `getId`, `getCode`, `getName`, `getSetType`, `getReleasedAt`, `getCardCount`, `getIconSvgUri`, etc. |
| `RulingUtils` | `Ruling` | `getOracleId`, `getSource`, `getPublishedAt`, `getComment` |
| `CatalogUtils` | `Catalog` | `getUri`, `getTotalValues`, `getData` |
| `CardSymbolUtils` | `CardSymbol` | `getSymbol`, `getEnglish`, `getManaValue`, `getSvgUri`, `getColors`, etc. |
| `ParsedManaCostUtils` | `ParsedManaCost` | `getCost`, `getCmc`, `getColors`, `isColorless`, `isMonocolored`, `isMulticolored` |
| `BulkDataUtils` | `BulkData` | `getId`, `getName`, `getDownloadUri`, `getSize`, `getUpdatedAt`, etc. |

### Implementation

#### CardUtils (Card Property Accessors)

```kotlin
@JsExport
object CardUtils {
    // Basic properties
    @JsName("getName") fun getName(card: Card): String = card.name
    @JsName("getTypeLine") fun getTypeLine(card: Card): String? = card.typeLine
    @JsName("getOracleText") fun getOracleText(card: Card): String? = card.oracleText
    @JsName("getManaCost") fun getManaCost(card: Card): String? = card.manaCost
    @JsName("getCmc") fun getCmc(card: Card): Double = card.manaValue
    @JsName("getPower") fun getPower(card: Card): String? = card.power
    @JsName("getToughness") fun getToughness(card: Card): String? = card.toughness
    @JsName("getSetCode") fun getSetCode(card: Card): String = card.setCode
    @JsName("getSetName") fun getSetName(card: Card): String = card.setName
    @JsName("getRarity") fun getRarity(card: Card): String = card.rarity
    @JsName("getId") fun getId(card: Card): String = card.id

    // Image URLs
    @JsName("getNormalImageUrl") fun getNormalImageUrl(card: Card): String? = card.imageUris?.normal
    @JsName("getSmallImageUrl") fun getSmallImageUrl(card: Card): String? = card.imageUris?.small
    @JsName("getLargeImageUrl") fun getLargeImageUrl(card: Card): String? = card.imageUris?.large
    @JsName("getArtCropUrl") fun getArtCropUrl(card: Card): String? = card.imageUris?.artCrop

    // Type checks (extension functions exposed as static methods)
    @JsName("isCreature") fun isCreature(card: Card): Boolean = ...
    @JsName("isInstant") fun isInstant(card: Card): Boolean = ...
    @JsName("isLand") fun isLand(card: Card): Boolean = ...

    // Format legality
    @JsName("isLegalIn") fun isLegalIn(card: Card, format: String): Boolean = ...
}
```

#### ListUtils (ScryfallList Accessors)

```kotlin
@JsExport
object ListUtils {
    @JsName("getData")
    fun <T> getData(list: ScryfallList<T>): Array<T> = list.data.toTypedArray()

    @JsName("getTotalCards")
    fun <T> getTotalCards(list: ScryfallList<T>): Int = list.totalCards ?: list.data.size

    @JsName("hasMore")
    fun <T> hasMore(list: ScryfallList<T>): Boolean = list.hasMore

    @JsName("getNextPage")
    fun <T> getNextPage(list: ScryfallList<T>): String? = list.nextPage

    @JsName("getObjectType")
    fun <T> getObjectType(list: ScryfallList<T>): String = list.objectType
}
```

### JavaScript Usage

```typescript
import * as scryfallApi from '@devmugi/scryfall-api';

// Extract API classes and utility factories
const {
    CardsApiJs,
    SetsApiJs,
    RulingsApiJs,
    CatalogsApiJs,
    CardSymbolApiJs,
    BulkDataApiJs,
    CardUtils: CardUtilsFactory,
    ListUtils: ListUtilsFactory,
    SetUtils: SetUtilsFactory,
    RulingUtils: RulingUtilsFactory,
    CatalogUtils: CatalogUtilsFactory,
} = scryfallApi as any;

// Create API instances (classes)
const cardsApi = new CardsApiJs();
const setsApi = new SetsApiJs();
const rulingsApi = new RulingsApiJs();
const catalogsApi = new CatalogsApiJs();

// Get utility singletons (Kotlin objects use getInstance())
const cardUtils = CardUtilsFactory.getInstance();
const listUtils = ListUtilsFactory.getInstance();
const setUtils = SetUtilsFactory.getInstance();
const catalogUtils = CatalogUtilsFactory.getInstance();

// Search for cards
const results = await cardsApi.search('lightning bolt', null);
const totalCards = listUtils.getTotalCards(results);
const cards = listUtils.getData(results);

// Access card properties via CardUtils
cards.forEach(card => {
    const name = cardUtils.getName(card);
    const typeLine = cardUtils.getTypeLine(card);
    const imageUrl = cardUtils.getNormalImageUrl(card);

    if (cardUtils.isCreature(card)) {
        console.log(`${name}: ${cardUtils.getPower(card)}/${cardUtils.getToughness(card)}`);
    }
});

// Get all sets
const setsResult = await setsApi.all();
const sets = listUtils.getData(setsResult);
sets.forEach(set => {
    console.log(`${setUtils.getName(set)} (${setUtils.getCode(set)})`);
});

// Get creature types catalog
const creatureTypes = await catalogsApi.creatureTypes();
const types = catalogUtils.getData(creatureTypes);
console.log('Creature types:', types.length);
```

---

## Consequences

### Positive

- Stable API that doesn't break with Kotlin version updates
- Clean separation between Kotlin and JavaScript concerns
- Full Kotlin feature support (default params, extensions, sealed classes)
- No impact on Kotlin consumers
- Accessor functions provide clear, documented API

### Negative

- Verbose JavaScript code compared to direct property access
- Requires learning accessor function pattern
- Multiple utility objects to manage (CardUtils, ListUtils, SetUtils, etc.)
- TypeScript types are `any` for raw Kotlin objects

### Neutral

- Singleton pattern uses `getInstance()` (Kotlin object convention)
- Additional files to maintain in jsMain

---

## Verification

```bash
# Rebuild JS library
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Test browser-vite sample
cd scryfall-api/samples/browser-vite
npm install
npm run dev

# Verify:
# 1. Search returns cards with proper count
# 2. Card images display correctly
# 3. No mangled property names in application code
```

---

## Related Documentation

- [ADR-006: npm Support](006-npm-support.md) - JS wrapper layer architecture
- [ADR-008: Browser Vite Sample](008-browser-vite-sample.md) - Browser integration
- [Utils.kt](../../src/jsMain/kotlin/devmugi/mtgcards/scryfall/js/Utils.kt) - Implementation
- [Kotlin/JS @JsExport](https://kotlinlang.org/docs/js-to-kotlin-interop.html#jsexport-annotation) - Official docs
