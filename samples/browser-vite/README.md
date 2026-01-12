# Browser Vite Sample

A browser-based sample demonstrating `@devmugi/scryfall-api` with Vite, TypeScript, and a search form UI.

## Screenshot

<img src="../images/browser-vite-result.png" width="700" alt="Browser Vite Sample"/>

> **Note:** This sample can use either a local build or published npm package.
> For Kotlin/JS usage without npm, see the [node-kmp](../node-kmp/) sample.

## Features

- **Search Form**: Full-text search using Scryfall syntax
- **Random Card**: Fetch a random Magic card
- **Card Display**: Responsive grid showing card images and details
- **Dark Theme**: MTG-themed dark UI
- **TypeScript**: Full type safety with generated declarations
- **Hot Reload**: Vite dev server with instant updates

## Prerequisites

- Node.js 18+

## Setup (Local Build)

Build the JS library first, then install and run:

```bash
# From project root
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Install and run
cd scryfall-api/samples/browser-vite
npm install
npm run dev
```

## Setup (npm Package)

If `@devmugi/scryfall-api` is published to npm, update `package.json`:

```json
"@devmugi/scryfall-api": "^1.0.0"
```

Then:

```bash
cd scryfall-api/samples/browser-vite
npm install
```

## Development

Start the development server:

```bash
npm run dev
```

Open http://localhost:5173 in your browser.

## Building

Build for production:

```bash
npm run build
```

Preview the production build:

```bash
npm run preview
```

## Usage

1. **Search**: Type a card name or Scryfall query in the search box
   - Examples: `lightning bolt`, `c:red cmc:1`, `t:creature pow>=5`
2. **Random**: Click "Random Card" to fetch a random Magic card
3. **View**: Cards display in a responsive grid with images and details

## Code Overview

### main.ts

```typescript
import * as scryfallApi from '@devmugi/scryfall-api';

// CardsApiJs is a class, CardUtils/ListUtils use getInstance() for singletons
const { CardsApiJs, CardUtils: CardUtilsFactory, ListUtils: ListUtilsFactory } = scryfallApi as any;
const api = new CardsApiJs();
const cardUtils = CardUtilsFactory.getInstance();
const listUtils = ListUtilsFactory.getInstance();

// Search for cards
const results = await api.search('lightning bolt', null);
const totalCards = listUtils.getTotalCards(results);
const cards = listUtils.getData(results);

// Get a random card
const card = await api.random(null);

// Use CardUtils accessor functions (Kotlin/JS mangles property names)
const name = cardUtils.getName(card);
const typeLine = cardUtils.getTypeLine(card);
const imageUrl = cardUtils.getNormalImageUrl(card);
```

### Available APIs

The library exports the following API classes:

| API | Description | Example |
|-----|-------------|---------|
| `CardsApiJs` | Card search and retrieval | `new CardsApiJs()` |
| `SetsApiJs` | MTG set information | `new SetsApiJs()` |
| `RulingsApiJs` | Card rulings | `new RulingsApiJs()` |
| `CatalogsApiJs` | Catalog data (types, keywords) | `new CatalogsApiJs()` |
| `CardSymbolApiJs` | Mana symbols and parsing | `new CardSymbolApiJs()` |
| `BulkDataApiJs` | Bulk data downloads | `new BulkDataApiJs()` |

### Kotlin/JS Considerations

Kotlin/JS mangles property names for optimization. Use accessor functions:

**CardUtils** - Card property accessors:
- `cardUtils.getName(card)` - Card name
- `cardUtils.getTypeLine(card)` - Type line
- `cardUtils.getOracleText(card)` - Oracle text
- `cardUtils.getNormalImageUrl(card)` - Image URL
- `cardUtils.getManaCost(card)` - Mana cost
- And more...

**ListUtils** - ScryfallList accessors:
- `listUtils.getData(results)` - Array of items
- `listUtils.getTotalCards(results)` - Total card count
- `listUtils.hasMore(results)` - Has more pages
- `listUtils.getNextPage(results)` - Next page URL

**Other Utils** - Property accessors for other models:
- `SetUtils` - Set properties (name, code, releaseDate, etc.)
- `RulingUtils` - Ruling properties (comment, source, etc.)
- `CatalogUtils` - Catalog data
- `CardSymbolUtils` - Symbol properties
- `BulkDataUtils` - Bulk data properties

## Project Structure

```
browser-vite/
├── package.json       # npm config with Vite + commonjs plugin
├── tsconfig.json      # TypeScript config
├── vite.config.ts     # Vite config with CommonJS/UMD handling
├── index.html         # HTML entry point
├── src/
│   ├── main.ts        # Application code
│   ├── style.css      # CSS styling
│   └── vite-env.d.ts  # Vite types
└── README.md
```

## Related

- [Node.js JavaScript Sample](../node-npm-js/) - Pure JavaScript without browser
- [Node.js TypeScript Sample](../node-npm-ts/) - Pure TypeScript without browser
- [Kotlin/JS Sample](../node-kmp/) - Uses Kotlin/JS with Gradle
- [ADR-008](../../docs/adr/008-browser-vite-sample.md) - Browser sample decisions
- [ADR-009](../../docs/adr/009-js-support.md) - JavaScript support strategy
