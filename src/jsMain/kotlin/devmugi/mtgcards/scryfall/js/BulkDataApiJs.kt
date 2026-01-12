@file:OptIn(DelicateCoroutinesApi::class, ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.BulkDataApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.js.mappers.toJs
import devmugi.mtgcards.scryfall.js.models.JsBulkData
import devmugi.mtgcards.scryfall.js.models.JsScryfallBulkDataList
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * JavaScript-friendly wrapper for BulkDataApi.
 *
 * Usage:
 * ```javascript
 * const api = new BulkDataApiJs();
 * const bulkData = await api.all();
 * const oracleCards = await api.byType('oracle-cards');
 * ```
 */
@JsExport
class BulkDataApiJs(config: ScryfallConfig = ScryfallConfig()) {
    private val api = BulkDataApi(config)

    /**
     * Returns a list of all Bulk Data items on Scryfall.
     *
     * @return Promise resolving to a list of bulk data items
     */
    @JsName("all")
    fun all(): Promise<JsScryfallBulkDataList> =
        GlobalScope.promise {
            api.all().toJs()
        }

    /**
     * Returns a Bulk Data object with the given Scryfall ID.
     *
     * @param id The Scryfall UUID
     * @return Promise resolving to the bulk data item
     */
    @JsName("byId")
    fun byId(id: String): Promise<JsBulkData> =
        GlobalScope.promise {
            api.byId(id).toJs()
        }

    /**
     * Returns a Bulk Data object with the given type slug.
     *
     * @param type The bulk data type slug (e.g., "oracle-cards", "default-cards")
     * @return Promise resolving to the bulk data item
     */
    @JsName("byType")
    fun byType(type: String): Promise<JsBulkData> =
        GlobalScope.promise {
            api.byType(type).toJs()
        }
}
