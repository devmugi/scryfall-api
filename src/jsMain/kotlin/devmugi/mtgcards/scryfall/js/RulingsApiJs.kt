@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.RulingsApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsScryfallRulingList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for RulingsApi.
 *
 * Usage:
 * ```javascript
 * const api = new RulingsApiJs();
 * const rulings = await api.byCardId('card-uuid');
 * ```
 */
@JsExport
class RulingsApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = RulingsApi(config)

    /**
     * Returns rulings for a card with the given Scryfall ID.
     *
     * @param id The Scryfall UUID
     * @return Promise resolving to a list of rulings
     */
    @JsName("byCardId")
    fun byCardId(id: String): Promise<JsScryfallRulingList> =
        GlobalScope.promise {
            api.byCardId(id).toJs()
        }

    /**
     * Returns rulings for a card with the given Multiverse ID.
     *
     * @param id The Multiverse ID
     * @return Promise resolving to a list of rulings
     */
    @JsName("byMultiverseId")
    fun byMultiverseId(id: Int): Promise<JsScryfallRulingList> =
        GlobalScope.promise {
            api.byMultiverseId(id).toJs()
        }

    /**
     * Returns rulings for a card with the given MTGO ID.
     *
     * @param id The MTGO ID
     * @return Promise resolving to a list of rulings
     */
    @JsName("byMtgoId")
    fun byMtgoId(id: Int): Promise<JsScryfallRulingList> =
        GlobalScope.promise {
            api.byMtgoId(id).toJs()
        }

    /**
     * Returns rulings for a card with the given Arena ID.
     *
     * @param id The Arena ID
     * @return Promise resolving to a list of rulings
     */
    @JsName("byArenaId")
    fun byArenaId(id: Int): Promise<JsScryfallRulingList> =
        GlobalScope.promise {
            api.byArenaId(id).toJs()
        }

    /**
     * Returns rulings for a card with the given set code and collector number.
     *
     * @param code The three to five-letter set code
     * @param number The collector number
     * @return Promise resolving to a list of rulings
     */
    @JsName("byCollectorId")
    fun byCollectorId(code: String, number: String): Promise<JsScryfallRulingList> =
        GlobalScope.promise {
            api.byCollectorId(code, number).toJs()
        }
}
