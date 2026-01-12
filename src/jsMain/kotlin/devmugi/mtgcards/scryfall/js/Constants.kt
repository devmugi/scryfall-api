@file:OptIn(ExperimentalJsExport::class)

package devmugi.mtgcards.scryfall.js

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Sort order options for card search.
 *
 * Usage:
 * ```javascript
 * import { SortOrder } from '@devmugi/scryfall-api';
 * api.search("lightning bolt", { order: SortOrder.CMC });
 * ```
 */
@JsExport
object SortOrder {
    /** Sort cards by name, A → Z */
    const val NAME = "name"

    /** Sort cards by their set and collector number */
    const val SET = "set"

    /** Sort cards by their release date: Newest → Oldest */
    const val RELEASED = "released"

    /** Sort cards by rarity (Common → Mythic) */
    const val RARITY = "rarity"

    /** Sort cards by color and color identity */
    const val COLOR = "color"

    /** Sort cards by their USD price */
    const val USD = "usd"

    /** Sort cards by their Magic Online (tix) price */
    const val TIX = "tix"

    /** Sort cards by their EUR price */
    const val EUR = "eur"

    /** Sort cards by mana value (converted mana cost) */
    const val CMC = "cmc"

    /** Sort cards by power */
    const val POWER = "power"

    /** Sort cards by toughness */
    const val TOUGHNESS = "toughness"

    /** Sort by EDHREC ranking/popularity */
    const val EDHREC = "edhrec"

    /** Sort by Penny Dreadful deck usage */
    const val PENNY = "penny"

    /** Sort cards by the artist credit */
    const val ARTIST = "artist"

    /** Sort by Scryfall review score */
    const val REVIEW = "review"
}

/**
 * Sort direction options for card search.
 *
 * Usage:
 * ```javascript
 * import { SortDir } from '@devmugi/scryfall-api';
 * api.search("creature", { order: SortOrder.CMC, dir: SortDir.DESC });
 * ```
 */
@JsExport
object SortDir {
    /** Let Scryfall choose a reasonable default */
    const val AUTO = "auto"

    /** Sort ascending (A→Z, 0→9, oldest→newest) */
    const val ASC = "asc"

    /** Sort descending (Z→A, 9→0, newest→oldest) */
    const val DESC = "desc"
}

/**
 * Unique mode options for deduplicating search results.
 *
 * Usage:
 * ```javascript
 * import { UniqueMode } from '@devmugi/scryfall-api';
 * api.search("pacifism", { unique: UniqueMode.ART });
 * ```
 */
@JsExport
object UniqueMode {
    /** Remove duplicate gameplay objects (cards with same name and functionality) */
    const val CARDS = "cards"

    /** Return only one copy of each unique artwork */
    const val ART = "art"

    /** Return all prints for all matched cards (disables rollup) */
    const val PRINTS = "prints"
}
