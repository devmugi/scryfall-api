# Node.js JavaScript Sample

A minimal **pure JavaScript** sample demonstrating how to use the `@devmugi/scryfall-api` npm package.

## Screenshot

<img src="../images/node-npm-js-result.png" width="500" alt="Node npm JS Output"/>

> **Note:** This sample requires the `@devmugi/scryfall-api` package to be published to npm.
> For Kotlin/JS usage without npm publishing, see the [node-kmp](../node-kmp/) sample.

## Prerequisites

- Node.js 18+ (for native ESM support)
- `@devmugi/scryfall-api` published to npm

## Setup

Install dependencies:
```bash
cd scryfall-api/samples/node-npm-js
npm install
```

## Running

```bash
npm start
```

## Expected Output

```
Random Card: Lightning Bolt
Type: Instant
Text: Lightning Bolt deals 3 damage to any target.
```

## Code Overview

```javascript
import { CardsApiJs } from '@devmugi/scryfall-api';

const api = new CardsApiJs();

async function main() {
    const card = await api.random(null);
    console.log(`Random Card: ${card.name}`);
    console.log(`Type: ${card.typeLine}`);
    console.log(`Text: ${card.oracleText || 'No text'}`);
}

main().catch(console.error);
```

## Key Points

- Uses **ESM** (`"type": "module"` in package.json)
- `CardsApiJs` returns **Promises** (use `async/await`)
- Card properties: `name`, `typeLine`, `oracleText`, etc.
- Pass `null` for optional parameters when no options needed

## Related

- [TypeScript Sample](../node-npm-ts/) - Same functionality with TypeScript types
- [Kotlin/JS Sample](../node-kmp/) - Uses Kotlin/JS directly
- [ADR-007](../../docs/adr/007-npm-samples.md) - Architecture decisions
