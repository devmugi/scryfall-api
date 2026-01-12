@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Unified entry point for the Scryfall API.
 *
 * Usage:
 * ```javascript
 * import { ScryfallJs } from '@devmugi/scryfall-api';
 *
 * // With default configuration
 * const scryfall = new ScryfallJs();
 *
 * // With custom configuration
 * const scryfall = new ScryfallJs({
 *   maxRetries: 3,
 *   requestTimeoutMillis: 30000
 * });
 *
 * // Access APIs
 * const card = await scryfall.cards.byScryfallId('...');
 * const sets = await scryfall.sets.all();
 * ```
 */
@JsExport
class ScryfallJs(config: JsScryfallConfig? = null) {
    private val kotlinConfig: ScryfallConfig = config.toScryfallConfig()

    /**
     * API for searching and retrieving cards.
     */
    @JsName("cards")
    val cards: CardsApiJs = CardsApiJs(kotlinConfig)

    /**
     * API for retrieving set information.
     */
    @JsName("sets")
    val sets: SetsApiJs = SetsApiJs(kotlinConfig)

    /**
     * API for retrieving card rulings.
     */
    @JsName("rulings")
    val rulings: RulingsApiJs = RulingsApiJs(kotlinConfig)

    /**
     * API for retrieving catalogs (card names, types, keywords, etc.).
     */
    @JsName("catalogs")
    val catalogs: CatalogsApiJs = CatalogsApiJs(kotlinConfig)

    /**
     * API for card symbols and mana cost parsing.
     */
    @JsName("symbology")
    val symbology: CardSymbolApiJs = CardSymbolApiJs(kotlinConfig)

    /**
     * API for bulk data downloads.
     */
    @JsName("bulk")
    val bulk: BulkDataApiJs = BulkDataApiJs(kotlinConfig)
}
