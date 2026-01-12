import * as scryfallApi from '@devmugi/scryfall-api';
import './style.css';

const { CardsApiJs } = scryfallApi as any;
const api = new CardsApiJs();

const searchForm = document.getElementById('search-form') as HTMLFormElement;
const searchInput = document.getElementById('search-input') as HTMLInputElement;
const randomBtn = document.getElementById('random-btn') as HTMLButtonElement;
const statusDiv = document.getElementById('status') as HTMLDivElement;
const resultsDiv = document.getElementById('results') as HTMLDivElement;

// Search handler
searchForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const query = searchInput.value.trim();
    if (!query) return;

    statusDiv.textContent = 'Searching...';
    resultsDiv.innerHTML = '';

    try {
        const results = await api.search(query, null);
        const totalCards = results.totalCards ?? results.data?.length ?? 0;
        const data = results.data ?? [];
        statusDiv.textContent = `Found ${totalCards} cards`;
        renderCards(data);
    } catch (error) {
        statusDiv.textContent = `Error: ${error}`;
        console.error('Search error:', error);
    }
});

// Random card handler
randomBtn.addEventListener('click', async () => {
    statusDiv.textContent = 'Fetching random card...';
    resultsDiv.innerHTML = '';

    try {
        const card = await api.random(null);
        statusDiv.textContent = 'Random card:';
        renderCards([card]);
    } catch (error) {
        statusDiv.textContent = `Error: ${error}`;
    }
});

// Card type for direct property access
interface JsCard {
    name: string;
    typeLine?: string;
    oracleText?: string;
    imageUris?: {
        small?: string;
        normal?: string;
        large?: string;
    };
}

// Render cards to the results div
function renderCards(cards: JsCard[]): void {
    resultsDiv.innerHTML = cards
        .map((card) => {
            // Direct property access on JsCard objects
            const name = card.name ?? 'Unknown';
            const typeLine = card.typeLine ?? '';
            const oracleText = card.oracleText ?? '';
            const imageUrl = card.imageUris?.normal ?? '';
            return `
                <div class="card">
                    <img src="${imageUrl}" alt="${name}" loading="lazy">
                    <div class="card-info">
                        <h3>${name}</h3>
                        <p class="type">${typeLine}</p>
                        <p class="text">${oracleText}</p>
                    </div>
                </div>
            `;
        })
        .join('');
}
