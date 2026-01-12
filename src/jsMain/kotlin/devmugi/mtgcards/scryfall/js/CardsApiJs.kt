@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.CardsApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.SortDirection
import devmugi.mtgcards.scryfall.api.models.SortingCards
import devmugi.mtgcards.scryfall.api.models.UniqueModes
import devmugi.mtgcards.scryfall.js.mappers.toIdentifier
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsAutocompleteResult
import devmugi.mtgcards.scryfall.js.models.JsCard
import devmugi.mtgcards.scryfall.js.models.JsScryfallCardList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for CardsApi.
 *
 * Usage:
 * ```javascript
 * const api = new CardsApiJs();
 * const results = await api.search('lightning bolt', { unique: 'prints' });
 * const card = await api.random();
 * ```
 */
@JsExport
class CardsApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = CardsApi(config)

    /**
     * Search for cards using Scryfall fulltext search syntax.
     *
     * @param q The search query (e.g., "lightning bolt", "c:red cmc:1")
     * @param options Optional search parameters
     * @return Promise resolving to a list of cards
     */
    @JsName("search")
    fun search(q: String, options: SearchOptions?): Promise<JsScryfallCardList> =
        GlobalScope.promise {
            api.search(
                q = q,
                unique = options?.unique?.let { parseUnique(it) },
                order = options?.order?.let { parseOrder(it) },
                dir = options?.dir?.let { parseDir(it) },
                includeExtras = options?.includeExtras,
                includeMultilingual = options?.includeMultilingual,
                includeVariations = options?.includeVariations,
                page = options?.page
            ).toJs()
        }

    /**
     * Get autocomplete suggestions for card names.
     *
     * @param q The partial card name to autocomplete
     * @param options Optional autocomplete parameters
     * @return Promise resolving to autocomplete results
     */
    @JsName("autocomplete")
    fun autocomplete(q: String, options: AutocompleteOptions?): Promise<JsAutocompleteResult> =
        GlobalScope.promise {
            api.autocomplete(
                q = q,
                includeExtras = options?.includeExtras
            ).toJs()
        }

    /**
     * Get a card by exact name.
     *
     * @param name The exact card name (case insensitive)
     * @param options Optional parameters
     * @return Promise resolving to the card
     */
    @JsName("namedExact")
    fun namedExact(name: String, options: CardOptions?): Promise<JsCard> =
        GlobalScope.promise {
            api.namedExact(
                name = name,
                set = options?.set,
                version = options?.version,
                face = options?.face
            ).toJs()
        }

    /**
     * Get a card by fuzzy name match.
     *
     * @param name The fuzzy card name (e.g., "jac bele" matches "Jace Beleren")
     * @param options Optional parameters
     * @return Promise resolving to the card
     */
    @JsName("namedFuzzy")
    fun namedFuzzy(name: String, options: CardOptions?): Promise<JsCard> =
        GlobalScope.promise {
            api.namedFuzzy(
                name = name,
                set = options?.set,
                version = options?.version,
                face = options?.face
            ).toJs()
        }

    /**
     * Get a random card.
     *
     * @param options Optional parameters including a filter query
     * @return Promise resolving to a random card
     */
    @JsName("random")
    fun random(options: RandomOptions?): Promise<JsCard> =
        GlobalScope.promise {
            api.random(
                q = options?.q,
                version = options?.version,
                face = options?.face
            ).toJs()
        }

    /**
     * Get a card by set code and collector number.
     *
     * @param code The set code (e.g., "neo", "mh2")
     * @param number The collector number
     * @param options Optional parameters
     * @return Promise resolving to the card
     */
    @JsName("bySetAndCollectorNumber")
    fun bySetAndCollectorNumber(code: String, number: String, options: CollectorOptions?): Promise<JsCard> =
        GlobalScope.promise {
            api.bySetAndCollectorNumber(
                code = code,
                number = number,
                lang = options?.lang,
                version = options?.version,
                face = options?.face
            ).toJs()
        }

    /**
     * Get a card by Scryfall ID.
     *
     * @param id The Scryfall UUID
     * @return Promise resolving to the card
     */
    @JsName("byScryfallId")
    fun byScryfallId(id: String): Promise<JsCard> =
        GlobalScope.promise {
            api.byScryfallId(id).toJs()
        }

    /**
     * Get a card by Multiverse ID.
     *
     * @param id The Multiverse ID
     * @return Promise resolving to the card
     */
    @JsName("byMultiverseId")
    fun byMultiverseId(id: Int): Promise<JsCard> =
        GlobalScope.promise {
            api.byMultiverseId(id).toJs()
        }

    /**
     * Get a card by MTGO ID.
     *
     * @param id The MTGO ID
     * @return Promise resolving to the card
     */
    @JsName("byMtgoId")
    fun byMtgoId(id: Int): Promise<JsCard> =
        GlobalScope.promise {
            api.byMtgoId(id).toJs()
        }

    /**
     * Get a card by Arena ID.
     *
     * @param id The Arena ID
     * @return Promise resolving to the card
     */
    @JsName("byArenaId")
    fun byArenaId(id: Int): Promise<JsCard> =
        GlobalScope.promise {
            api.byArenaId(id).toJs()
        }

    /**
     * Get a card by TCGPlayer ID.
     *
     * @param id The TCGPlayer product ID
     * @return Promise resolving to the card
     */
    @JsName("byTcgplayerId")
    fun byTcgplayerId(id: Int): Promise<JsCard> =
        GlobalScope.promise {
            api.byTcgplayerId(id).toJs()
        }

    /**
     * Get a card by Cardmarket ID.
     *
     * @param id The Cardmarket product ID
     * @return Promise resolving to the card
     */
    @JsName("byCardmarketId")
    fun byCardmarketId(id: Int): Promise<JsCard> =
        GlobalScope.promise {
            api.byCardmarketId(id).toJs()
        }

    /**
     * Fetch multiple cards at once by their identifiers.
     * Maximum of 75 identifiers per request.
     *
     * @param identifiers Array of card identifiers (by id, name, set+collectorNumber, etc.)
     * @return Promise resolving to a list of cards. Cards not found are in the notFound field.
     *
     * @example
     * ```javascript
     * const cards = await api.collection([
     *   { id: "683a5707-cddb-494d-9b41-51b4584ber62" },
     *   { name: "Lightning Bolt" },
     *   { set: "mrd", collectorNumber: "150" }
     * ]);
     * ```
     */
    @JsName("collection")
    fun collection(identifiers: Array<JsIdentifier>): Promise<JsScryfallCardList> =
        GlobalScope.promise {
            api.collection(identifiers.map { it.toIdentifier() }).toJs()
        }

    // Helper functions to parse string enum values
    private fun parseUnique(value: String): UniqueModes? =
        UniqueModes.entries.firstOrNull { it.value.equals(value, ignoreCase = true) }

    private fun parseOrder(value: String): SortingCards? =
        SortingCards.entries.firstOrNull { it.value.equals(value, ignoreCase = true) }

    private fun parseDir(value: String): SortDirection? =
        SortDirection.entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
}
