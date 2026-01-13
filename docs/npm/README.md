# @devmugi/scryfall-api

Scryfall API client for JavaScript and TypeScript. Access the [Scryfall](https://scryfall.com/) Magic: The Gathering card database with full TypeScript support.

## Installation

```bash
npm install @devmugi/scryfall-api
```

## Quick Start

```javascript
import { CardsApiJs } from '@devmugi/scryfall-api';

const api = new CardsApiJs();

// Get a random card
const card = await api.random(null);
console.log(`${card.name} - ${card.typeLine}`);

// Search for cards
const results = await api.search({ q: 'lightning bolt' });
console.log(`Found ${results.totalCards} cards`);

// Get card by exact name
const bolt = await api.namedExact({ name: 'Lightning Bolt' });
console.log(bolt.oracleText);
```

## Available APIs

| API | Class | Description |
|-----|-------|-------------|
| **Cards** | `CardsApiJs` | Search, random, named lookup, collection |
| **Sets** | `SetsApiJs` | Magic set information |
| **Rulings** | `RulingsApiJs` | Official card rulings |
| **Catalogs** | `CatalogsApiJs` | Reference data (card names, types, keywords) |
| **Symbology** | `CardSymbolApiJs` | Mana symbols and cost parsing |
| **Bulk Data** | `BulkDataApiJs` | Bulk data export metadata |

## Cards API Examples

```javascript
import { CardsApiJs } from '@devmugi/scryfall-api';

const cards = new CardsApiJs();

// Search with Scryfall syntax
const creatures = await cards.search({ q: 't:creature c:red pow>=4' });

// Get card by exact name
const lotus = await cards.namedExact({ name: 'Black Lotus' });

// Get card by fuzzy name
const card = await cards.namedFuzzy({ name: 'jace mind sculptor' });

// Get random card (pass null for no filter)
const random = await cards.random(null);

// Get random card with filter
const randomCreature = await cards.random({ q: 't:creature' });

// Autocomplete card names
const suggestions = await cards.autocomplete({ q: 'lig' });
```

## Sets API Example

```javascript
import { SetsApiJs } from '@devmugi/scryfall-api';

const sets = new SetsApiJs();

// Get all sets
const allSets = await sets.all();

// Get set by code
const khm = await sets.byCode({ code: 'khm' });
console.log(`${khm.name} - ${khm.cardCount} cards`);
```

## TypeScript Support

This package includes TypeScript declarations (`.d.mts`). All card properties and API responses are fully typed.

```typescript
import { CardsApiJs } from '@devmugi/scryfall-api';

const api = new CardsApiJs();

async function main(): Promise<void> {
    const card = await api.random(null);
    console.log(`${card.name}: ${card.oracleText ?? 'No text'}`);
}
```

## Requirements

- Node.js 18+ (ESM support)
- Modern browsers with ES module support

## Links

- [GitHub Repository](https://github.com/devmugi/scryfall-api)
- [Full Documentation](https://github.com/devmugi/scryfall-api#readme)
- [Scryfall API Docs](https://scryfall.com/docs/api)

## License

MIT
