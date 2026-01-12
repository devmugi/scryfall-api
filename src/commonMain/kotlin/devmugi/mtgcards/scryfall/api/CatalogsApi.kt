package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallBaseApi
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.Catalog
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/**
 * Catalogs are small curated lists of Magic data such as creature types and card names.
 * https://scryfall.com/docs/api/catalogs
 */
class CatalogsApi(
    config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    logger: ((String) -> Unit)? = null,
) : ScryfallBaseApi(config, engine, logger) {

    /**
     * Returns a Catalog of all English card names on Scryfall.
     * https://scryfall.com/docs/api/catalogs/card-names
     */
    suspend fun cardNames(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/card-names") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all artist names on Scryfall.
     * https://scryfall.com/docs/api/catalogs/artist-names
     */
    suspend fun artistNames(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/artist-names") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all English words, of length 2 or more, that could appear in a card name.
     * https://scryfall.com/docs/api/catalogs/word-bank
     */
    suspend fun wordBank(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/word-bank") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all supertypes in the game.
     * https://scryfall.com/docs/api/catalogs/supertypes
     */
    suspend fun supertypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/supertypes") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all card types in the game.
     * https://scryfall.com/docs/api/catalogs/card-types
     */
    suspend fun cardTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/card-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all creature supertypes and subtypes.
     * https://scryfall.com/docs/api/catalogs/creature-types
     */
    suspend fun creatureTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/creature-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all Planeswalker types.
     * https://scryfall.com/docs/api/catalogs/planeswalker-types
     */
    suspend fun planeswalkerTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/planeswalker-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all land supertypes and subtypes.
     * https://scryfall.com/docs/api/catalogs/land-types
     */
    suspend fun landTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/land-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all artifact types.
     * https://scryfall.com/docs/api/catalogs/artifact-types
     */
    suspend fun artifactTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/artifact-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all battle types.
     * https://scryfall.com/docs/api/catalogs/battle-types
     */
    suspend fun battleTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/battle-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all enchantment types.
     * https://scryfall.com/docs/api/catalogs/enchantment-types
     */
    suspend fun enchantmentTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/enchantment-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all spell types (instant and sorcery types).
     * https://scryfall.com/docs/api/catalogs/spell-types
     */
    suspend fun spellTypes(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/spell-types") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all possible power values in the game.
     * https://scryfall.com/docs/api/catalogs/powers
     */
    suspend fun powers(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/powers") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all possible toughness values in the game.
     * https://scryfall.com/docs/api/catalogs/toughnesses
     */
    suspend fun toughnesses(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/toughnesses") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all possible loyalty values in the game.
     * https://scryfall.com/docs/api/catalogs/loyalties
     */
    suspend fun loyalties(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/loyalties") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all watermarks used on cards.
     * https://scryfall.com/docs/api/catalogs/watermarks
     */
    suspend fun watermarks(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/watermarks") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all keyword abilities on cards.
     * https://scryfall.com/docs/api/catalogs/keyword-abilities
     */
    suspend fun keywordAbilities(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/keyword-abilities") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all keyword actions on cards.
     * https://scryfall.com/docs/api/catalogs/keyword-actions
     */
    suspend fun keywordActions(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/keyword-actions") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all ability words on cards.
     * https://scryfall.com/docs/api/catalogs/ability-words
     */
    suspend fun abilityWords(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/ability-words") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()

    /**
     * Returns a Catalog of all flavor words on cards.
     * https://scryfall.com/docs/api/catalogs/flavor-words
     */
    suspend fun flavorWords(format: String? = "json", pretty: Boolean? = false): Catalog =
        client.get("/catalog/flavor-words") {
            if (format != null) parameter("format", format)
            if (pretty != null) parameter("pretty", pretty)
        }.body()
}
