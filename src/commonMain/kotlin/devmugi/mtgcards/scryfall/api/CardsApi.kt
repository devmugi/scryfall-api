package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.core.validateArenaId
import devmugi.mtgcards.scryfall.api.core.validateCardName
import devmugi.mtgcards.scryfall.api.core.validateCardmarketId
import devmugi.mtgcards.scryfall.api.core.validateCollectorNumber
import devmugi.mtgcards.scryfall.api.core.validateIdentifiers
import devmugi.mtgcards.scryfall.api.core.validateLanguageCode
import devmugi.mtgcards.scryfall.api.core.validateMtgoId
import devmugi.mtgcards.scryfall.api.core.validateMultiverseId
import devmugi.mtgcards.scryfall.api.core.validatePage
import devmugi.mtgcards.scryfall.api.core.validateQuery
import devmugi.mtgcards.scryfall.api.core.validateScryfallId
import devmugi.mtgcards.scryfall.api.core.validateSetCode
import devmugi.mtgcards.scryfall.api.core.validateTcgplayerId
import devmugi.mtgcards.scryfall.api.models.AutocompleteResult
import devmugi.mtgcards.scryfall.api.models.Card
import devmugi.mtgcards.scryfall.api.models.Identifier
import devmugi.mtgcards.scryfall.api.models.IdentifiersRequest
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.api.models.SortDirection
import devmugi.mtgcards.scryfall.api.models.SortingCards
import devmugi.mtgcards.scryfall.api.models.UniqueModes
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * https://scryfall.com/docs/api/cards
 */
class CardsApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a List object containing Cards found using a fulltext search string. This string supports the same fulltext search system that the main site uses.
     * https://scryfall.com/docs/api/cards/search
     * https://scryfall.com/docs/syntax
     *
     * @param q - A fulltext search query. Make sure that your parameter is properly encoded. Maximum length: 1000 Unicode characters.
     * @param unique - The strategy for omitting similar cards. See [UniqueModes]
     * @param order - The method to sort returned cards. See [SortingCards]
     * @param dir - The direction to sort returned cards. See [SortDirection]
     * @param includeExtras - If true, extra cards (tokens, planes, etc) will be included. Equivalent to adding include:extras to the fulltext search. Defaults to false.
     * @param includeMultilingual - If true, cards in every language supported by Scryfall will be included. Defaults to false.
     * @param includeVariations - If true, rare care variants will be included, like the Hairy Runesword. Defaults to false.
     * @param page - The page of results to return. Defaults to 1.
     * @param format - The data format to return: json or csv. Defaults to json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    @Suppress("LongParameterList") // API wrapper method matching Scryfall API parameters
    suspend fun search(
        q: String,
        unique: UniqueModes? = null,
        order: SortingCards? = null,
        dir: SortDirection? = null,
        includeExtras: Boolean? = false,
        includeMultilingual: Boolean? = false,
        includeVariations: Boolean? = false,
        page: Int? = 1,
        format: String? = "json",
        pretty: Boolean? = false
    ): ScryfallList<Card> {
        validateQuery(q)
        if (page != null) validatePage(page)

        val response = client.get("/cards/search") {
            parameter("q", q)
            if (unique != null) parameter("unique", unique.value)
            if (order != null) parameter("order", order.value)
            if (dir != null) parameter("dir", dir.value)
            if (includeExtras != null) parameter("include_extras", includeExtras)
            if (includeMultilingual != null) parameter("include_multilingual", includeMultilingual)
            if (includeVariations != null) parameter("include_variations", includeVariations)
            if (page != null) parameter("page", page)
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a Catalog-like object containing up to 20 full English card names
     * that could be autocompletions of the given string parameter.
     * If q is less than 2 characters long, or if no names match, the result will contain 0 items.
     * https://scryfall.com/docs/api/cards/autocomplete
     *
     * @param q - The string to autocomplete.
     * @param format - The data format to return. This method only supports json.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     * @param includeExtras - If true, extra cards (tokens, planes, etc) will be included.
     */
    suspend fun autocomplete(
        q: String,
        format: String? = "json",
        pretty: Boolean? = null,
        includeExtras: Boolean? = false,
    ): AutocompleteResult {
        require(q.isNotBlank()) { "Autocomplete query cannot be blank" }

        val response = client.get("/cards/autocomplete") {
            parameter("q", q)
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
            if (includeExtras != null) parameter("include_extras", includeExtras)
        }
        return response.body()
    }

    /**
     * Returns a Card based on a name search string.
     * https://scryfall.com/docs/api/cards/named
     *
     * @param name - The exact card name to search for, case insenstive.
     * @param set - A set code to limit the search to one set.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun namedExact(
        name: String,
        set: String? = null,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateCardName(name)
        if (set != null) validateSetCode(set)

        val response = client.get("/cards/named") {
            parameter("exact", name)
            if (set != null) parameter("set", set)
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a Card based on a name search string.
     * The server allows misspellings and partial words to be provided. For example: jac bele will match Jace Beleren.
     * https://scryfall.com/docs/api/cards/named
     *
     * @param name - A fuzzy card name to search for.
     * @param set - A set code to limit the search to one set.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun namedFuzzy(
        name: String,
        set: String? = null,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateCardName(name)
        if (set != null) validateSetCode(set)

        val response = client.get("/cards/named") {
            parameter("fuzzy", name)
            if (set != null) parameter("set", set)
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single random Card object.
     * https://scryfall.com/docs/api/cards/random
     *
     * @param q - An optional fulltext search query to filter the pool of random cards. Make sure that your parameter is properly encoded.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun random(
        q: String? = null,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        if (!q.isNullOrBlank()) validateQuery(q)

        val response = client.get("/cards/random") {
            if (!q.isNullOrBlank()) parameter("q", q)
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Accepts a JSON array of card identifiers, and returns a List object with the collection of requested cards. A maximum of 75 card references may be submitted per request. The request must be posted with Content-Type as application/json.
     * https://scryfall.com/docs/api/cards/collection
     *
     * @param identifiers - An array of JSON objects, each one a card identifier.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun collection(identifiers: List<Identifier>, pretty: Boolean? = false): ScryfallList<Card> {
        validateIdentifiers(identifiers)

        val response = client.post("/cards/collection") {
            contentType(ContentType.Application.Json)
            setBody(IdentifiersRequest(identifiers = identifiers, pretty = pretty))
        }
        return response.body()
    }

    /**
     * Returns a single card with the given set code and collector number. You may optionally also append a lang part to the URL to retrieve a non-English version of the card.
     * https://scryfall.com/docs/api/cards/collector
     *
     * @param code - The three to five-letter set code.
     * @param number - The collector number.
     * @param lang - The language code to retrieve a non-English version of the card.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun bySetAndCollectorNumber(
        code: String,
        number: String,
        lang: String? = null,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateSetCode(code)
        validateCollectorNumber(number)
        if (lang != null) validateLanguageCode(lang)

        val response = client.get("/cards/$code/$number") {
            if (lang != null) parameter("lang", lang)
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given Multiverse ID. If the card has multiple multiverse IDs, this method can find either of them.
     * https://scryfall.com/docs/api/cards/multiverse
     *
     * @param id - The multiverse ID.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byMultiverseId(
        id: Int,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateMultiverseId(id)

        val response = client.get("/cards/multiverse/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given MTGO ID (also known as the Catalog ID). The ID can either be the card’s mtgo_id or its mtgo_foil_id.
     * https://scryfall.com/docs/api/cards/mtgo
     *
     * @param id - The MTGO ID.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byMtgoId(
        id: Int,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateMtgoId(id)

        val response = client.get("/cards/mtgo/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given Magic: The Gathering Arena ID.
     * https://scryfall.com/docs/api/cards/arena
     *
     * @param id - The Arena ID.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byArenaId(
        id: Int,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateArenaId(id)

        val response = client.get("/cards/arena/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given tcgplayer_id or tcgplayer_etched_id, also known as the productId on TCGplayer’s API.
     * https://scryfall.com/docs/api/cards/tcgplayer
     *
     * @param id - The tcgplayer_id or productId.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byTcgplayerId(
        id: Int,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateTcgplayerId(id)

        val response = client.get("/cards/tcgplayer/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given cardmarket_id, also known as the idProduct" or the Product ID on Cardmarket’s APIs.
     * https://scryfall.com/docs/api/cards/cardmarket
     *
     * @param id - The cardmarket_id.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byCardmarketId(
        id: Int,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateCardmarketId(id)

        val response = client.get("/cards/cardmarket/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }

    /**
     * Returns a single card with the given Scryfall ID.
     * https://scryfall.com/docs/api/cards/id
     *
     * @param id - The Scryfall ID.
     * @param format - The data format to return: json, text, or image. Defaults to json.
     * @param face - If using the image format and this parameter has the value back, the back face of the card will be returned. Will return a 422 if this card has no back face.
     * @param version - The image version to return when using the image format: small, normal, large, png, art_crop, or border_crop. Defaults to large.
     * @param pretty - If true, the returned JSON will be prettified. Avoid using for production code.
     */
    suspend fun byScryfallId(
        id: String,
        format: String? = "json",
        face: String? = null,
        version: String? = null,
        pretty: Boolean? = false
    ): Card {
        validateScryfallId(id)

        val response = client.get("/cards/$id") {
            if (format != null) parameter("format", format)
            if (face != null) parameter("face", face)
            if (version != null) parameter("version", version)
            if (pretty != null) parameter("pretty", pretty)
        }
        return response.body()
    }
}
