@file:OptIn(ExperimentalJsExport::class)

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Configuration options for the Scryfall API client.
 *
 * @example
 * ```javascript
 * const scryfall = new ScryfallJs({
 *   maxRetries: 3,
 *   requestTimeoutMillis: 30000
 * });
 * ```
 */
@JsExport
external interface JsScryfallConfig {
    /** The base URL for the Scryfall API (default: https://api.scryfall.com) */
    val baseUrl: String?

    /** Connection timeout in milliseconds (default: 10000) */
    val connectTimeoutMillis: Int?

    /** Request timeout in milliseconds (default: 15000) */
    val requestTimeoutMillis: Int?

    /** Socket timeout in milliseconds (default: 30000) */
    val socketTimeoutMillis: Int?

    /** Maximum number of retry attempts for failed requests (default: 2) */
    val maxRetries: Int?

    /** Enable HTTP request/response logging (default: false) */
    val enableLogging: Boolean?
}

/**
 * Convert JS config to Kotlin ScryfallConfig.
 */
internal fun JsScryfallConfig?.toScryfallConfig(): ScryfallConfig {
    if (this == null) return ScryfallConfig()
    return ScryfallConfig(
        baseUrl = baseUrl ?: "https://api.scryfall.com",
        connectTimeoutMillis = connectTimeoutMillis?.toLong() ?: 10_000L,
        requestTimeoutMillis = requestTimeoutMillis?.toLong() ?: 15_000L,
        socketTimeoutMillis = socketTimeoutMillis?.toLong() ?: 30_000L,
        maxRetries = maxRetries ?: 2,
        enableLogging = enableLogging ?: false
    )
}

/**
 * Options for card search operations.
 */
@JsExport
external interface SearchOptions {
    /** The strategy for omitting similar cards: "cards", "art", or "prints" */
    val unique: String?

    /** The method to sort returned cards */
    val order: String?

    /** The direction to sort: "auto", "asc", or "desc" */
    val dir: String?

    /** If true, extra cards (tokens, planes, etc) will be included */
    val includeExtras: Boolean?

    /** If true, cards in every language will be included */
    val includeMultilingual: Boolean?

    /** If true, rare card variants will be included */
    val includeVariations: Boolean?

    /** The page of results to return (default: 1) */
    val page: Int?
}

/**
 * Options for fetching a single card.
 */
@JsExport
external interface CardOptions {
    /** A set code to limit the search to one set */
    val set: String?

    /** The image version: small, normal, large, png, art_crop, or border_crop */
    val version: String?

    /** If "back", returns the back face of the card */
    val face: String?
}

/**
 * Options for random card fetch.
 */
@JsExport
external interface RandomOptions {
    /** An optional fulltext search query to filter the pool of random cards */
    val q: String?

    /** The image version: small, normal, large, png, art_crop, or border_crop */
    val version: String?

    /** If "back", returns the back face of the card */
    val face: String?
}

/**
 * Options for autocomplete.
 */
@JsExport
external interface AutocompleteOptions {
    /** If true, extra cards (tokens, planes, etc) will be included */
    val includeExtras: Boolean?
}

/**
 * Options for fetching by set and collector number.
 */
@JsExport
external interface CollectorOptions {
    /** The language code (e.g., "en", "ja", "de") */
    val lang: String?

    /** The image version: small, normal, large, png, art_crop, or border_crop */
    val version: String?

    /** If "back", returns the back face of the card */
    val face: String?
}

/**
 * Card identifier for batch collection lookup.
 * Provide exactly one of the identifier fields.
 *
 * @example
 * // By Scryfall ID
 * { id: "683a5707-cddb-494d-9b41-51b4584ber62" }
 *
 * // By set and collector number
 * { set: "mrd", collectorNumber: "150" }
 *
 * // By name
 * { name: "Lightning Bolt" }
 *
 * // By name and set
 * { name: "Lightning Bolt", set: "a25" }
 */
@JsExport
external interface JsIdentifier {
    /** Scryfall ID (UUID) */
    val id: String?

    /** Oracle ID */
    val oracleId: String?

    /** Illustration ID */
    val illustrationId: String?

    /** Multiverse ID */
    val multiverseId: Int?

    /** MTGO ID */
    val mtgoId: Int?

    /** Arena ID */
    val arenaId: Int?

    /** Set code (e.g., "mrd", "neo") */
    val set: String?

    /** Collector number */
    val collectorNumber: String?

    /** Card name (case-insensitive) */
    val name: String?
}
