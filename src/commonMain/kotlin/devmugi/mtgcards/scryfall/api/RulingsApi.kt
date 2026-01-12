package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.Ruling
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Rulings represent Oracle rulings, Wizards of the Coast set release notes, or Scryfall notes for a particular card.
 * https://scryfall.com/docs/api/rulings
 */
class RulingsApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a List of rulings for a card with the given Scryfall ID.
     * https://scryfall.com/docs/api/rulings/id
     * @param id - The Scryfall ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byCardId(id: String, format: String? = "json", pretty: Boolean? = false): ScryfallList<Ruling> =
        client.get("/cards/$id/rulings") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a List of rulings for a card with the given Multiverse ID. If the card has multiple multiverse IDs, this method can find either of them.
     * https://scryfall.com/docs/api/rulings/multiverse
     * @param id - The multiverse ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byMultiverseId(id: Int, format: String? = "json", pretty: Boolean? = false): ScryfallList<Ruling> =
        client.get("/cards/multiverse/$id/rulings") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns rulings for a card with the given MTGO ID (also known as the Catalog ID). The ID can either be the card’s mtgo_id or its mtgo_foil_id.
     * https://scryfall.com/docs/api/rulings/mtgo
     * @param id - The MTGO ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byMtgoId(id: Int, format: String? = "json", pretty: Boolean? = false): ScryfallList<Ruling> =
        client.get("/cards/mtgo/$id/rulings") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns rulings for a card with the given Magic: The Gathering Arena ID.
     * https://scryfall.com/docs/api/rulings/arena
     * @param id - The MTGO ID.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byArenaId(id: Int, format: String? = "json", pretty: Boolean? = false): ScryfallList<Ruling> =
        client.get("/cards/arena/$id/rulings") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a List of rulings for the card with the given set code and collector number.
     * https://scryfall.com/docs/api/rulings/collector
     * @param code - The three to five-letter set code.
     * @param number - The collector number.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byCollectorId(
        code: String,
        number: String,
        format: String? = "json",
        pretty: Boolean? = false
    ): ScryfallList<Ruling> =
        client.get("/cards/$code/$number/rulings") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()
}
