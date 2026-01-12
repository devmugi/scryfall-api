# ADR-008: Browser Vite Sample for JavaScript/TypeScript Developers

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Creating a browser-based sample demonstrating npm package consumption with modern tooling

---

## Summary

This ADR documents decisions made for creating a browser-based Vite sample that consumes the `@devmugi/scryfall-api` npm package, demonstrating the library in a web application context with a search form UI.

---

## Decision 1: Vite as Build Tool

### Problem

Which build tool should be used for the browser sample?

### Proposed Solutions

1. **Webpack**
   - Pros: Most established, extensive ecosystem
   - Cons: Complex configuration, slower builds, steeper learning curve

2. **Vite**
   - Pros: Fast builds, minimal config, modern defaults, great DX
   - Cons: Newer, smaller plugin ecosystem

3. **Parcel**
   - Pros: Zero config, easy to use
   - Cons: Less control, smaller community

4. **esbuild**
   - Pros: Extremely fast
   - Cons: Lower-level, requires more setup for full dev experience

### Chosen Solution: Vite

```typescript
import { defineConfig } from 'vite';

export default defineConfig({
  // No special config needed for basic usage
});
```

**Rationale**:
- Near-instant dev server startup with native ES modules
- Minimal configuration required
- Built-in TypeScript support
- Hot Module Replacement (HMR) out of the box
- Modern standard in web development
- Simple `npm run dev` workflow familiar to developers

---

## Decision 2: Vanilla TypeScript (No Framework)

### Problem

Should the sample use a UI framework like React, Vue, or Svelte?

### Options

1. **React**
   - Pros: Most popular, large ecosystem
   - Cons: Adds complexity, requires React knowledge

2. **Vue**
   - Pros: Gentle learning curve
   - Cons: Additional dependency, Vue-specific patterns

3. **Vanilla TypeScript**
   - Pros: No framework overhead, focuses on API usage
   - Cons: More verbose DOM manipulation

### Chosen Solution: Vanilla TypeScript

```typescript
const searchForm = document.getElementById('search-form') as HTMLFormElement;
searchForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  const results = await api.search(query, null);
  renderCards(results.data);
});
```

**Rationale**:
- Focuses attention on the `@devmugi/scryfall-api` library usage
- No framework knowledge required
- Demonstrates that the library works in plain browser JavaScript
- Smaller bundle size
- Developers can integrate into their framework of choice

---

## Decision 3: Search Form UI with Card Display

### Problem

What features should the browser sample demonstrate?

### Options

1. **Minimal** - Console.log output only
   - Pros: Simplest possible
   - Cons: Not showcasing browser capabilities

2. **Search Form** - Interactive search with visual results
   - Pros: Demonstrates real usage, shows card images
   - Cons: More code to maintain

3. **Full Application** - Multiple pages, routing, state management
   - Pros: Complete example
   - Cons: Overwhelming, scope creep

### Chosen Solution: Search Form UI

Features implemented:
- Search input with Scryfall query syntax support
- Random card button
- Responsive grid displaying card images and details
- Loading states and error handling

**Rationale**:
- Shows the library in a realistic browser context
- Demonstrates `CardsApiJs.search()` and `CardsApiJs.random()`
- Showcases `CardUtils.getNormalImageUrl()` for images
- Visual feedback makes the sample engaging
- Not overly complex

---

## Decision 4: Dark Theme MTG Aesthetic

### Problem

What visual styling should the sample use?

### Chosen Solution: Dark Theme

```css
body {
  background: #1a1a1a;
  color: #fff;
}

.card {
  background: #2a2a2a;
  border-radius: 8px;
}
```

**Rationale**:
- Matches Magic: The Gathering aesthetic
- Card images look better on dark backgrounds
- Modern, professional appearance
- Good contrast for readability

---

## Decision 5: npm Package Dependency

### Problem

How should the sample reference the scryfall-api package?

### Chosen Solution: npm Package

```json
{
  "dependencies": {
    "@devmugi/scryfall-api": "^1.0.0"
  }
}
```

**Rationale**:
- Same as node-npm samples (consistency)
- Demonstrates real-world npm usage pattern
- Works once the package is published to npm
- For local development without npm publishing, use the node-kmp sample

---

## Decision 6: Kotlin/JS UMD Module Handling

### Problem

Kotlin/JS produces UMD modules with mangled property names, which don't integrate well with Vite's ES module bundling.

### Challenges

1. **UMD Format**: Kotlin/JS outputs UMD modules expecting global dependencies
2. **Property Mangling**: Property names are minified (e.g., `name` becomes `n5p_1`)
3. **Singleton Pattern**: Kotlin `object` declarations use `getInstance()` pattern

### Chosen Solution: CommonJS Plugin + Accessor Functions

1. Use `@rollup/plugin-commonjs` to handle UMD modules
2. Alias `ws` to `isomorphic-ws` for browser WebSocket compatibility
3. Use `CardUtils` accessor functions instead of direct property access

```typescript
// vite.config.ts
import commonjs from '@rollup/plugin-commonjs';

export default defineConfig({
  plugins: [commonjs({ transformMixedEsModules: true })],
  resolve: { alias: { ws: 'isomorphic-ws/browser.js' } },
});

// main.ts - Use accessor functions
const cardUtils = CardUtilsFactory.getInstance();
const name = cardUtils.getName(card);  // Instead of card.name
```

**Rationale**:
- CommonJS plugin handles UMD-to-ESM conversion
- Accessor functions provide stable API despite property mangling
- Browser-compatible WebSocket via isomorphic-ws

---

## Consequences

### Positive

- Modern browser development workflow with Vite
- Interactive demo showcasing library capabilities
- No framework lock-in
- Visual card display demonstrates image URL utilities
- Fast development with hot reload

### Negative

- Requires `@devmugi/scryfall-api` to be published to npm (or local build)
- Vanilla JS more verbose than framework code
- Browser-specific (not Node.js)
- Must use accessor functions due to property mangling

### Neutral

- Search uses Scryfall query syntax (feature, not limitation)
- Dark theme is opinionated but appropriate
- CommonJS plugin adds build complexity but is necessary

---

## File Structure

```
scryfall-api/samples/browser-vite/
├── package.json          # npm config with Vite + commonjs plugin
├── tsconfig.json         # TypeScript configuration
├── vite.config.ts        # Vite config with UMD/CommonJS handling
├── index.html            # HTML entry point with search form
├── src/
│   ├── main.ts           # Application code
│   ├── style.css         # CSS styling
│   └── vite-env.d.ts     # Vite type declarations
└── README.md             # Documentation
```

---

## Usage

```bash
# Requires @devmugi/scryfall-api published to npm

cd scryfall-api/samples/browser-vite
npm install
npm run dev

# Open http://localhost:5173
# - Type "lightning bolt" and click Search
# - Click "Random Card" button
# - View cards in responsive grid
```

---

## API Demonstration

```typescript
import * as scryfallApi from '@devmugi/scryfall-api';

// Extract exports (Kotlin/JS uses getInstance() for singletons)
const { CardsApiJs, CardUtils: CardUtilsFactory } = scryfallApi as any;
const api = new CardsApiJs();
const cardUtils = CardUtilsFactory.getInstance();

// Get a random card
const card = await api.random(null);

// Use CardUtils accessor functions (property names are mangled)
const name = cardUtils.getName(card);
const typeLine = cardUtils.getTypeLine(card);
const imageUrl = cardUtils.getNormalImageUrl(card);
```

---

## Related Documentation

- [samples/README.md](../../samples/README.md) - Sample overview
- [ADR-005](005-node-kmp-sample.md) - Kotlin/JS Node.js sample
- [ADR-006](006-npm-support.md) - npm publishing support
- [ADR-007](007-npm-samples.md) - Pure npm samples (JS/TS)
