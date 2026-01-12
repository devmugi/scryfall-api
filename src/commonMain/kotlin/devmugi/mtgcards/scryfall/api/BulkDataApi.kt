package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.BulkData
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Bulk Data endpoints.
 * https://scryfall.com/docs/api/bulk-data
 */
class BulkDataApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a List object of all Bulk Data items on Scryfall.
     * https://scryfall.com/docs/api/bulk-data/all
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun all(format: String? = "json", pretty: Boolean? = false): ScryfallList<BulkData> =
        client.get("/bulk-data") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Bulk Data object with the given Scryfall id.
     * https://scryfall.com/docs/api/bulk-data/id
     * @param id - The Scryfall ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byId(id: String, format: String? = "json", pretty: Boolean? = false): BulkData =
        client.get("/bulk-data/$id") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Bulk Data object with the given type slug (e.g., oracle-cards, default-cards).
     * https://scryfall.com/docs/api/bulk-data/type
     * @param type - The bulk data type slug.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byType(type: String, format: String? = "json", pretty: Boolean? = false): BulkData =
        client.get("/bulk-data/$type") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()
}
