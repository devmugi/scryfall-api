package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.CardSymbol
import devmugi.mtgcards.scryfall.api.models.ParsedManaCost
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Card Symbols (Symbology) endpoints.
 * https://scryfall.com/docs/api/card-symbols
 */
class CardSymbolApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a List object of all card symbols.
     * https://scryfall.com/docs/api/card-symbols/all
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun all(format: String? = "json", pretty: Boolean? = false): ScryfallList<CardSymbol> =
        client.get("/symbology") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Parses a mana cost string into tokens and computes values like converted mana cost and colors.
     * https://scryfall.com/docs/api/card-symbols/parse-mana
     * @param cost - A mana cost string such as {2}{G}{U} or {G/U}.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun parseMana(cost: String, format: String? = "json", pretty: Boolean? = false): ParsedManaCost =
        client.get("/symbology/parse-mana") {
            parameter("cost", cost)
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()
}
