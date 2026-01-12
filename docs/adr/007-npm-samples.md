# ADR-007: Pure npm Samples for JavaScript/TypeScript Developers

**Date**: 2026-01-11
**Status**: Accepted
**Context**: Creating samples that demonstrate npm package consumption for JS/TS developers

---

## Summary

This ADR documents decisions made for creating pure JavaScript and TypeScript samples that consume the `@devmugi/scryfall-api` npm package, targeting developers who want to use the library without any Kotlin tooling.

---

## Decision 1: Separate from Kotlin Samples

### Problem

The existing `node-kmp` sample uses Kotlin/JS, which requires Gradle and Kotlin knowledge. JavaScript/TypeScript developers need samples that use familiar npm-based workflows.

### Proposed Solutions

1. **Extend node-kmp with JS examples**
   - Pros: Single sample directory
   - Cons: Mixes Kotlin and JS tooling, confusing for JS developers

2. **Create separate pure npm samples**
   - Pros: Familiar npm workflow, no Gradle needed, clear separation
   - Cons: More directories to maintain

### Chosen Solution: Separate Pure npm Samples

Created two new samples:
- `node-npm-js/` - Pure JavaScript sample
- `node-npm-ts/` - Pure TypeScript sample

**Rationale**:
- JS/TS developers don't need to understand Kotlin or Gradle
- Clear copy-paste starting point for npm projects
- Each sample is self-contained with familiar package.json workflow

---

## Decision 2: ESM Module Format

### Problem

Which JavaScript module format to use?

### Options

1. **CommonJS** (`require()`)
   - Pros: Wider compatibility with older Node.js
   - Cons: Older standard, doesn't work well with TypeScript ESM

2. **ESM** (`import/export`)
   - Pros: Modern standard, native TypeScript support, tree-shaking
   - Cons: Requires Node.js 14+ (18+ for full support)

### Chosen Solution: ESM

```json
{
  "type": "module"
}
```

**Rationale**:
- Modern JavaScript standard
- Better TypeScript integration
- Tree-shaking support for smaller bundles
- Node.js 18+ LTS is widely available

---

## Decision 3: File-Based Dependency

### Problem

How should samples reference the scryfall-api package?

### Options

1. **Published npm package** (`"@devmugi/scryfall-api": "^1.0.0"`)
   - Pros: Real-world usage pattern
   - Cons: Requires package to be published first

2. **File dependency** (`"@devmugi/scryfall-api": "file:../../build/..."`)
   - Pros: Works with local builds, no publishing needed
   - Cons: Requires building the library first

3. **npm link**
   - Pros: Dynamic linking
   - Cons: Requires manual setup, not portable

### Chosen Solution: npm Package Dependency

```json
{
  "dependencies": {
    "@devmugi/scryfall-api": "^1.0.0"
  }
}
```

**Rationale**:
- Demonstrates real-world npm usage pattern
- Samples work once the package is published to npm
- Shows exact workflow that external developers will use
- For local development without npm publishing, use the node-kmp sample instead

---

## Decision 4: Minimal Feature Scope

### Problem

What functionality should the samples demonstrate?

### Options

1. **Minimal** - Single random card
2. **Standard** - Search, random, utilities
3. **Comprehensive** - All API methods

### Chosen Solution: Minimal (random card)

```javascript
const card = await api.random(null);
console.log(`Random Card: ${card.name}`);
```

**Rationale**:
- Simplest possible working example
- Demonstrates core pattern (create API, call method, use result)
- Easy to understand at a glance
- Developers can expand based on their needs
- More complex examples exist in node-kmp sample

---

## Decision 5: Both JavaScript and TypeScript

### Problem

Should we provide JavaScript, TypeScript, or both?

### Chosen Solution: Both

Created two separate samples:
- `node-npm-js/` - For JavaScript developers
- `node-npm-ts/` - For TypeScript developers with type safety

**Rationale**:
- Different developer preferences
- TypeScript sample showcases generated `.d.ts` files
- JavaScript sample shows it works without TypeScript
- Minimal code duplication (same logic, different syntax)

---

## Consequences

### Positive

- Clear path for JS/TS developers to use the library
- No Kotlin/Gradle knowledge required
- Familiar npm workflow
- Copy-paste ready starting points
- TypeScript users get full type safety

### Negative

- Two more sample directories to maintain
- Must build library before running samples
- File dependency path must be updated if published

### Neutral

- Samples are extremely minimal (single file each)
- TypeScript sample requires `npm run build` step

---

## File Structure

```
scryfall-api/samples/
├── node-npm-js/              # Pure JavaScript
│   ├── package.json          # ESM, file dependency
│   ├── index.js              # Entry point
│   └── README.md             # Documentation
├── node-npm-ts/              # Pure TypeScript
│   ├── package.json          # ESM, TypeScript dev dependency
│   ├── tsconfig.json         # TypeScript configuration
│   ├── src/index.ts          # Entry point
│   └── README.md             # Documentation
└── README.md                 # Updated with new samples
```

---

## Usage

### JavaScript Sample

```bash
# Build library
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Run sample
cd scryfall-api/samples/node-npm-js
npm install
npm start
```

### TypeScript Sample

```bash
# Build library
./gradlew :scryfall-api:jsBrowserProductionLibraryDistribution

# Run sample
cd scryfall-api/samples/node-npm-ts
npm install
npm run build
npm start
```

---

## Related Documentation

- [samples/README.md](../../samples/README.md) - Sample overview
- [ADR-005](005-node-kmp-sample.md) - Kotlin/JS Node.js sample
- [ADR-006](006-npm-support.md) - npm publishing support
