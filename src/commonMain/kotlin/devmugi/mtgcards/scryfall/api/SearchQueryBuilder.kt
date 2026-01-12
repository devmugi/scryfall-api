package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.models.Card
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.api.models.SortDirection
import devmugi.mtgcards.scryfall.api.models.SortingCards
import devmugi.mtgcards.scryfall.api.models.UniqueModes

/**
 * A DSL builder for constructing Scryfall search queries in a type-safe way.
 *
 * This builder provides a more intuitive and IDE-friendly way to construct search queries
 * compared to manually building query strings with Scryfall syntax.
 *
 * Example usage:
 * ```kotlin
 * val query = searchQuery {
 *     type("creature")
 *     color("red")
 *     powerRange(min = 4)
 *     cmcRange(max = 5)
 *     isLegal("standard")
 * }
 * ```
 *
 * @see [Scryfall Search Syntax](https://scryfall.com/docs/syntax)
 */
class SearchQueryBuilder {
    private val parts = mutableListOf<String>()

    /**
     * Adds an exact name match to the query.
     * This will match only cards with exactly the specified name.
     *
     * Example: `name("Lightning Bolt")` produces `!"Lightning Bolt"`
     *
     * @param value The exact card name to search for
     */
    fun name(value: String) {
        parts.add("!\"$value\"")
    }

    /**
     * Adds a partial name match to the query.
     * This will match cards whose names contain the specified text.
     *
     * Example: `nameContains("bolt")` matches "Lightning Bolt", "Firebolt", etc.
     *
     * @param value The text to search for in card names
     */
    fun nameContains(value: String) {
        parts.add(value)
    }

    /**
     * Filters by card type.
     *
     * Example: `type("creature")` produces `t:creature`
     *
     * @param value The type to search for (e.g., "creature", "instant", "artifact")
     */
    fun type(value: String) {
        parts.add("t:$value")
    }

    /**
     * Filters by exact color identity.
     *
     * Example: `color("R", "G")` produces `c:RG` (red and green only)
     *
     * @param colors The color codes (W, U, B, R, G, C for colorless)
     */
    fun color(vararg colors: String) {
        parts.add("c:${colors.joinToString("")}")
    }

    /**
     * Filters by exact converted mana cost/mana value.
     *
     * Example: `cmc(3)` produces `cmc=3`
     *
     * @param value The exact mana value
     */
    fun cmc(value: Int) {
        parts.add("cmc=$value")
    }

    /**
     * Filters by converted mana cost/mana value range.
     *
     * Example: `cmcRange(min = 2, max = 4)` produces `cmc>=2 cmc<=4`
     *
     * @param min The minimum mana value (inclusive), or null for no minimum
     * @param max The maximum mana value (inclusive), or null for no maximum
     */
    fun cmcRange(min: Int? = null, max: Int? = null) {
        if (min != null) parts.add("cmc>=$min")
        if (max != null) parts.add("cmc<=$max")
    }

    /**
     * Filters by set code.
     *
     * Example: `set("neo")` produces `e:neo`
     *
     * @param code The three to five-letter set code
     */
    fun set(code: String) {
        parts.add("e:$code")
    }

    /**
     * Filters by rarity.
     *
     * Example: `rarity("mythic")` produces `r:mythic`
     *
     * @param value The rarity (common, uncommon, rare, mythic)
     */
    fun rarity(value: String) {
        parts.add("r:$value")
    }

    /**
     * Searches for text in the oracle text.
     *
     * Example: `text("draw a card")` produces `o:"draw a card"`
     *
     * @param value The text to search for in oracle text
     */
    fun text(value: String) {
        parts.add("o:$value")
    }

    /**
     * Filters by exact power.
     *
     * Example: `power(3)` produces `pow=3`
     *
     * @param value The exact power value
     */
    fun power(value: Int) {
        parts.add("pow=$value")
    }

    /**
     * Filters by power range.
     *
     * Example: `powerRange(min = 3, max = 5)` produces `pow>=3 pow<=5`
     *
     * @param min The minimum power (inclusive), or null for no minimum
     * @param max The maximum power (inclusive), or null for no maximum
     */
    fun powerRange(min: Int? = null, max: Int? = null) {
        if (min != null) parts.add("pow>=$min")
        if (max != null) parts.add("pow<=$max")
    }

    /**
     * Filters by exact toughness.
     *
     * Example: `toughness(5)` produces `tou=5`
     *
     * @param value The exact toughness value
     */
    fun toughness(value: Int) {
        parts.add("tou=$value")
    }

    /**
     * Filters by toughness range.
     *
     * Example: `toughnessRange(min = 3, max = 5)` produces `tou>=3 tou<=5`
     *
     * @param min The minimum toughness (inclusive), or null for no minimum
     * @param max The maximum toughness (inclusive), or null for no maximum
     */
    fun toughnessRange(min: Int? = null, max: Int? = null) {
        if (min != null) parts.add("tou>=$min")
        if (max != null) parts.add("tou<=$max")
    }

    /**
     * Filters by format (Standard, Modern, Legacy, Vintage, Commander, etc.).
     * This finds cards printed in a format, regardless of legality.
     *
     * Example: `format("standard")` produces `f:standard`
     *
     * @param format The format name
     */
    fun format(format: String) {
        parts.add("f:$format")
    }

    /**
     * Filters by cards that are legal in a specific format.
     *
     * Example: `isLegal("modern")` produces `legal:modern`
     *
     * @param format The format name (standard, modern, legacy, vintage, commander, etc.)
     */
    fun isLegal(format: String) {
        parts.add("legal:$format")
    }

    /**
     * Searches for cards by artist name.
     *
     * Example: `artist("Seb McKinnon")` produces `a:"Seb McKinnon"`
     *
     * @param name The artist's name
     */
    fun artist(name: String) {
        parts.add("a:\"$name\"")
    }

    /**
     * Filters by keyword ability.
     *
     * Example: `keyword("flying")` produces `o:flying`
     *
     * @param keyword The keyword ability name
     */
    fun keyword(keyword: String) {
        parts.add("o:$keyword")
    }

    /**
     * Adds a raw query string part.
     * Use this for advanced queries that aren't covered by the DSL methods.
     *
     * Example: `raw("is:commander")` adds `is:commander` to the query
     *
     * @param queryPart The raw query string to add
     */
    fun raw(queryPart: String) {
        parts.add(queryPart)
    }

    /**
     * Builds the final query string by joining all parts with spaces.
     *
     * @return The complete Scryfall search query string
     */
    fun build(): String = parts.joinToString(" ")
}

/**
 * Creates a Scryfall search query using the DSL builder.
 *
 * Example:
 * ```kotlin
 * val query = searchQuery {
 *     type("creature")
 *     color("red")
 *     powerRange(min = 4)
 *     cmcRange(max = 5)
 *     isLegal("standard")
 * }
 * // Result: "t:creature c:red pow>=4 cmc<=5 legal:standard"
 * ```
 *
 * @param builder A lambda with [SearchQueryBuilder] as the receiver
 * @return The constructed query string
 */
fun searchQuery(builder: SearchQueryBuilder.() -> Unit): String = SearchQueryBuilder().apply(builder).build()

/**
 * Searches for cards using the DSL query builder instead of a raw query string.
 *
 * This extension function provides a more intuitive way to build and execute searches
 * without needing to know the Scryfall search syntax.
 *
 * Example:
 * ```kotlin
 * val results = cardsApi.searchWithBuilder {
 *     type("creature")
 *     color("red")
 *     powerRange(min = 4)
 *     cmcRange(max = 5)
 *     isLegal("standard")
 * }
 * ```
 *
 * @param unique The strategy for omitting similar cards
 * @param order The method to sort returned cards
 * @param dir The direction to sort returned cards
 * @param includeExtras If true, extra cards (tokens, planes, etc) will be included
 * @param includeMultilingual If true, cards in every language will be included
 * @param includeVariations If true, rare card variants will be included
 * @param page The page of results to return (defaults to 1)
 * @param builder A lambda with [SearchQueryBuilder] as the receiver
 * @return A [ScryfallList] containing the matching cards
 */
@Suppress("LongParameterList") // DSL function matching CardsApi.search() parameters for consistency
suspend fun CardsApi.searchWithBuilder(
    unique: UniqueModes? = null,
    order: SortingCards? = null,
    dir: SortDirection? = null,
    includeExtras: Boolean = false,
    includeMultilingual: Boolean = false,
    includeVariations: Boolean = false,
    page: Int = 1,
    builder: SearchQueryBuilder.() -> Unit
): ScryfallList<Card> {
    val query = searchQuery(builder)
    return search(query, unique, order, dir, includeExtras, includeMultilingual, includeVariations, page)
}
