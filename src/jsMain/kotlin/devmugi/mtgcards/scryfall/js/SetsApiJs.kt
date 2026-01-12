@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.SetsApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsScryfallSetList
import devmugi.mtgcards.scryfall.js.models.JsSet
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for SetsApi.
 *
 * Usage:
 * ```javascript
 * const api = new SetsApiJs();
 * const sets = await api.all();
 * const set = await api.byCode('neo');
 * ```
 */
@JsExport
class SetsApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = SetsApi(config)

    /**
     * Returns a list of all Sets on Scryfall.
     *
     * @return Promise resolving to a list of sets
     */
    @JsName("all")
    fun all(): Promise<JsScryfallSetList> =
        GlobalScope.promise {
            api.all().toJs()
        }

    /**
     * Returns a Set with the given set code.
     *
     * @param code The three to five-letter set code
     * @return Promise resolving to the set
     */
    @JsName("byCode")
    fun byCode(code: String): Promise<JsSet> =
        GlobalScope.promise {
            api.byCode(code).toJs()
        }

    /**
     * Returns a Set with the given TCGPlayer ID.
     *
     * @param id The TCGPlayer group ID
     * @return Promise resolving to the set
     */
    @JsName("byTcgPlayerId")
    fun byTcgPlayerId(id: String): Promise<JsSet> =
        GlobalScope.promise {
            api.byTcgPlayerId(id).toJs()
        }

    /**
     * Returns a Set with the given Scryfall ID.
     *
     * @param id The Scryfall UUID
     * @return Promise resolving to the set
     */
    @JsName("byId")
    fun byId(id: String): Promise<JsSet> =
        GlobalScope.promise {
            api.byId(id).toJs()
        }
}
