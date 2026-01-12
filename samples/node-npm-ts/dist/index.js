import { CardsApiJs } from '@devmugi/scryfall-api';
const api = new CardsApiJs();
async function main() {
    const card = await api.random(null);
    console.log(`Random Card: ${card.name}`);
    console.log(`Type: ${card.typeLine}`);
    console.log(`Text: ${card.oracleText ?? 'No text'}`);
}
main().catch(console.error);
