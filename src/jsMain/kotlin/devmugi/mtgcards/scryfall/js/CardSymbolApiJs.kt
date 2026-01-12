@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.CardSymbolApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsParsedManaCost
import devmugi.mtgcards.scryfall.js.models.JsScryfallCardSymbolList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for CardSymbolApi (Symbology).
 *
 * Usage:
 * ```javascript
 * const api = new CardSymbolApiJs();
 * const symbols = await api.all();
 * const parsed = await api.parseMana('{2}{G}{U}');
 * ```
 */
@JsExport
class CardSymbolApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = CardSymbolApi(config)

    /**
     * Returns a list of all card symbols.
     *
     * @return Promise resolving to a list of card symbols
     */
    @JsName("all")
    fun all(): Promise<JsScryfallCardSymbolList> =
        GlobalScope.promise {
            api.all().toJs()
        }

    /**
     * Parses a mana cost string into tokens and computes values.
     *
     * @param cost A mana cost string such as {2}{G}{U} or {G/U}
     * @return Promise resolving to the parsed mana cost
     */
    @JsName("parseMana")
    fun parseMana(cost: String): Promise<JsParsedManaCost> =
        GlobalScope.promise {
            api.parseMana(cost).toJs()
        }
}
