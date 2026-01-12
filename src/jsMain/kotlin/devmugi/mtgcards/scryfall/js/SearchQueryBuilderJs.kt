@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.SearchQueryBuilder
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

/**
 * Fluent builder for constructing Scryfall search queries in JavaScript.
 *
 * Usage:
 * ```javascript
 * import { SearchQueryBuilderJs } from '@devmugi/scryfall-api';
 *
 * const query = new SearchQueryBuilderJs()
 *     .type("creature")
 *     .color("R", "G")
 *     .cmcRange(2, 4)
 *     .isLegal("standard")
 *     .build();
 *
 * // Use with CardsApiJs
 * const results = await api.search(query);
 * ```
 *
 * @see [Scryfall Search Syntax](https://scryfall.com/docs/syntax)
 */
@JsExport
class SearchQueryBuilderJs {
    private val builder = SearchQueryBuilder()

    /**
     * Adds an exact name match to the query.
     * @param value The exact card name to search for
     * @return this builder for chaining
     */
    @JsName("name")
    fun name(value: String): SearchQueryBuilderJs {
        builder.name(value)
        return this
    }

    /**
     * Adds a partial name match to the query.
     * @param value The text to search for in card names
     * @return this builder for chaining
     */
    @JsName("nameContains")
    fun nameContains(value: String): SearchQueryBuilderJs {
        builder.nameContains(value)
        return this
    }

    /**
     * Filters by card type.
     * @param value The type (e.g., "creature", "instant", "artifact")
     * @return this builder for chaining
     */
    @JsName("type")
    fun type(value: String): SearchQueryBuilderJs {
        builder.type(value)
        return this
    }

    /**
     * Filters by exact color identity.
     * @param colors The color codes (W, U, B, R, G, C for colorless)
     * @return this builder for chaining
     */
    @JsName("color")
    fun color(vararg colors: String): SearchQueryBuilderJs {
        builder.color(*colors)
        return this
    }

    /**
     * Filters by exact mana value.
     * @param value The exact mana value
     * @return this builder for chaining
     */
    @JsName("cmc")
    fun cmc(value: Int): SearchQueryBuilderJs {
        builder.cmc(value)
        return this
    }

    /**
     * Filters by mana value range.
     * @param min The minimum mana value (inclusive), or null for no minimum
     * @param max The maximum mana value (inclusive), or null for no maximum
     * @return this builder for chaining
     */
    @JsName("cmcRange")
    fun cmcRange(min: Int? = null, max: Int? = null): SearchQueryBuilderJs {
        builder.cmcRange(min, max)
        return this
    }

    /**
     * Filters by set code.
     * @param code The three to five-letter set code
     * @return this builder for chaining
     */
    @JsName("set")
    fun set(code: String): SearchQueryBuilderJs {
        builder.set(code)
        return this
    }

    /**
     * Filters by rarity.
     * @param value The rarity (common, uncommon, rare, mythic)
     * @return this builder for chaining
     */
    @JsName("rarity")
    fun rarity(value: String): SearchQueryBuilderJs {
        builder.rarity(value)
        return this
    }

    /**
     * Searches for text in the oracle text.
     * @param value The text to search for
     * @return this builder for chaining
     */
    @JsName("text")
    fun text(value: String): SearchQueryBuilderJs {
        builder.text(value)
        return this
    }

    /**
     * Filters by exact power.
     * @param value The exact power value
     * @return this builder for chaining
     */
    @JsName("power")
    fun power(value: Int): SearchQueryBuilderJs {
        builder.power(value)
        return this
    }

    /**
     * Filters by power range.
     * @param min The minimum power (inclusive), or null for no minimum
     * @param max The maximum power (inclusive), or null for no maximum
     * @return this builder for chaining
     */
    @JsName("powerRange")
    fun powerRange(min: Int? = null, max: Int? = null): SearchQueryBuilderJs {
        builder.powerRange(min, max)
        return this
    }

    /**
     * Filters by exact toughness.
     * @param value The exact toughness value
     * @return this builder for chaining
     */
    @JsName("toughness")
    fun toughness(value: Int): SearchQueryBuilderJs {
        builder.toughness(value)
        return this
    }

    /**
     * Filters by toughness range.
     * @param min The minimum toughness (inclusive), or null for no minimum
     * @param max The maximum toughness (inclusive), or null for no maximum
     * @return this builder for chaining
     */
    @JsName("toughnessRange")
    fun toughnessRange(min: Int? = null, max: Int? = null): SearchQueryBuilderJs {
        builder.toughnessRange(min, max)
        return this
    }

    /**
     * Filters by format (cards printed in the format).
     * @param format The format name (standard, modern, legacy, etc.)
     * @return this builder for chaining
     */
    @JsName("format")
    fun format(format: String): SearchQueryBuilderJs {
        builder.format(format)
        return this
    }

    /**
     * Filters by cards legal in a specific format.
     * @param format The format name (standard, modern, legacy, vintage, commander, etc.)
     * @return this builder for chaining
     */
    @JsName("isLegal")
    fun isLegal(format: String): SearchQueryBuilderJs {
        builder.isLegal(format)
        return this
    }

    /**
     * Searches for cards by artist name.
     * @param name The artist's name
     * @return this builder for chaining
     */
    @JsName("artist")
    fun artist(name: String): SearchQueryBuilderJs {
        builder.artist(name)
        return this
    }

    /**
     * Filters by keyword ability.
     * @param keyword The keyword ability name
     * @return this builder for chaining
     */
    @JsName("keyword")
    fun keyword(keyword: String): SearchQueryBuilderJs {
        builder.keyword(keyword)
        return this
    }

    /**
     * Adds a raw query string part for advanced queries.
     * @param queryPart The raw query string (e.g., "is:commander")
     * @return this builder for chaining
     */
    @JsName("raw")
    fun raw(queryPart: String): SearchQueryBuilderJs {
        builder.raw(queryPart)
        return this
    }

    /**
     * Builds the final query string.
     * @return The complete Scryfall search query string
     */
    @JsName("build")
    fun build(): String = builder.build()
}
