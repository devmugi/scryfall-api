package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.api.models.Set
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * A Set object represents a group of related Magic cards. All Card objects on Scryfall belong to exactly one set.
 * https://scryfall.com/docs/api/sets
 */
class SetsApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a List object of all Sets on Scryfall.
     * https://scryfall.com/docs/api/sets/all
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun all(format: String? = "json", pretty: Boolean? = false): ScryfallList<Set> =
        client.get("/sets") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Set with the given set code. The code can be either the code or the mtgo_code for the set.
     * https://scryfall.com/docs/api/sets/code
     * @param code - The three to five-letter set code.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byCode(code: String, format: String? = "json", pretty: Boolean? = false): Set =
        client.get("/sets/$code") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Set with the given tcgplayer_id, also known as the groupId on TCGplayer’s API.
     * https://scryfall.com/docs/api/sets/tcgplayer
     * @param id - The tcgplayer_id or groupId.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byTcgPlayerId(id: String, format: String? = "json", pretty: Boolean? = false): Set =
        client.get("/sets/tcgplayer/$id") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Set with the given Scryfall id.
     * https://scryfall.com/docs/api/sets/id
     * @param id - The Scryfall ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byId(id: String, format: String? = "json", pretty: Boolean? = false): Set =
        client.get("/sets/$id") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()
}
