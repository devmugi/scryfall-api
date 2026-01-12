@file:OptIn(ExperimentalJsExport::class, DelicateCoroutinesApi::class)

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.models.BulkData
import devmugi.mtgcards.scryfall.api.models.Card
import devmugi.mtgcards.scryfall.api.models.CardSymbol
import devmugi.mtgcards.scryfall.api.models.Catalog
import devmugi.mtgcards.scryfall.api.models.ParsedManaCost
import devmugi.mtgcards.scryfall.api.models.Ruling
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.api.models.Set
import devmugi.mtgcards.scryfall.js.mappers.*
import devmugi.mtgcards.scryfall.js.models.JsScryfallCardList
import devmugi.mtgcards.scryfall.js.models.JsScryfallSetList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import kotlinx.serialization.json.Json
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.js.Promise

/**
 * Utility functions for card type checking.
 *
 * Usage:
 * ```javascript
 * import { CardTypeUtils } from '@devmugi/scryfall-api';
 *
 * if (CardTypeUtils.isCreature(card)) {
 *   console.log('Power:', card.power);
 * }
 * ```
 */
@JsExport
object CardTypeUtils {
    /** Returns true if the card is a creature */
    @JsName("isCreature")
    fun isCreature(card: Card): Boolean = card.typeLine?.contains("Creature", ignoreCase = true) == true

    /** Returns true if the card is an instant */
    @JsName("isInstant")
    fun isInstant(card: Card): Boolean = card.typeLine?.contains("Instant", ignoreCase = true) == true

    /** Returns true if the card is a sorcery */
    @JsName("isSorcery")
    fun isSorcery(card: Card): Boolean = card.typeLine?.contains("Sorcery", ignoreCase = true) == true

    /** Returns true if the card is an artifact */
    @JsName("isArtifact")
    fun isArtifact(card: Card): Boolean = card.typeLine?.contains("Artifact", ignoreCase = true) == true

    /** Returns true if the card is an enchantment */
    @JsName("isEnchantment")
    fun isEnchantment(card: Card): Boolean = card.typeLine?.contains("Enchantment", ignoreCase = true) == true

    /** Returns true if the card is a planeswalker */
    @JsName("isPlaneswalker")
    fun isPlaneswalker(card: Card): Boolean = card.typeLine?.contains("Planeswalker", ignoreCase = true) == true

    /** Returns true if the card is a land */
    @JsName("isLand")
    fun isLand(card: Card): Boolean = card.typeLine?.contains("Land", ignoreCase = true) == true

    /** Returns true if the card is a battle */
    @JsName("isBattle")
    fun isBattle(card: Card): Boolean = card.typeLine?.contains("Battle", ignoreCase = true) == true
}

/**
 * Utility functions for card color and game mechanics.
 *
 * Usage:
 * ```javascript
 * import { CardColorUtils } from '@devmugi/scryfall-api';
 *
 * if (CardColorUtils.isMulticolored(card)) {
 *   console.log('Gold card!');
 * }
 * ```
 */
@JsExport
object CardColorUtils {
    /** Returns true if the card has no colors (colorless) */
    @JsName("isColorless")
    fun isColorless(card: Card): Boolean = card.colors.isNullOrEmpty()

    /** Returns true if the card has more than one color */
    @JsName("isMulticolored")
    fun isMulticolored(card: Card): Boolean = (card.colors?.size ?: 0) > 1

    /** Returns true if the card contains the specified color (W, U, B, R, G) */
    @JsName("hasColor")
    fun hasColor(card: Card, color: String): Boolean = card.colors?.contains(color.uppercase()) == true

    /** Returns true if the card has the specified keyword ability */
    @JsName("hasKeyword")
    fun hasKeyword(card: Card, keyword: String): Boolean =
        card.keywords?.any { it.equals(keyword, ignoreCase = true) } == true

    /** Returns true if the card is legal in the specified format */
    @JsName("isLegalIn")
    fun isLegalIn(card: Card, format: String): Boolean {
        val legality = when (format.lowercase()) {
            "standard" -> card.legalities?.standard
            "pioneer" -> card.legalities?.pioneer
            "modern" -> card.legalities?.modern
            "legacy" -> card.legalities?.legacy
            "vintage" -> card.legalities?.vintage
            "commander", "edh" -> card.legalities?.commander
            "pauper" -> card.legalities?.pauper
            "historic" -> card.legalities?.historic
            "alchemy" -> card.legalities?.alchemy
            "explorer" -> card.legalities?.explorer
            "brawl" -> card.legalities?.brawl
            else -> null
        }
        return legality == "legal"
    }
}

/**
 * Utility functions for accessing card image URLs.
 *
 * Usage:
 * ```javascript
 * import { CardImageUtils } from '@devmugi/scryfall-api';
 *
 * const imageUrl = CardImageUtils.getNormalImageUrl(card);
 * ```
 */
@JsExport
object CardImageUtils {
    /** Gets the normal image URL for the card, if available */
    @JsName("getNormalImageUrl")
    fun getNormalImageUrl(card: Card): String? = card.imageUris?.normal

    /** Gets the small image URL for the card, if available */
    @JsName("getSmallImageUrl")
    fun getSmallImageUrl(card: Card): String? = card.imageUris?.small

    /** Gets the large image URL for the card, if available */
    @JsName("getLargeImageUrl")
    fun getLargeImageUrl(card: Card): String? = card.imageUris?.large

    /** Gets the art crop image URL for the card, if available */
    @JsName("getArtCropUrl")
    fun getArtCropUrl(card: Card): String? = card.imageUris?.artCrop
}

/**
 * Utility functions for accessing card text properties.
 * Kotlin/JS mangles property names, so use these functions to access card data.
 *
 * Usage:
 * ```javascript
 * import { CardTextUtils } from '@devmugi/scryfall-api';
 *
 * const name = CardTextUtils.getName(card);
 * ```
 */
@JsExport
object CardTextUtils {
    /** Gets the card name */
    @JsName("getName")
    fun getName(card: Card): String = card.name

    /** Gets the card type line */
    @JsName("getTypeLine")
    fun getTypeLine(card: Card): String? = card.typeLine

    /** Gets the oracle text */
    @JsName("getOracleText")
    fun getOracleText(card: Card): String? = card.oracleText

    /** Gets the mana cost */
    @JsName("getManaCost")
    fun getManaCost(card: Card): String? = card.manaCost

    /** Gets the converted mana cost / mana value */
    @JsName("getCmc")
    fun getCmc(card: Card): Double = card.manaValue

    /** Gets the power (for creatures) */
    @JsName("getPower")
    fun getPower(card: Card): String? = card.power

    /** Gets the toughness (for creatures) */
    @JsName("getToughness")
    fun getToughness(card: Card): String? = card.toughness
}

/**
 * Utility functions for accessing card identity/set properties.
 *
 * Usage:
 * ```javascript
 * import { CardIdentityUtils } from '@devmugi/scryfall-api';
 *
 * const setCode = CardIdentityUtils.getSetCode(card);
 * ```
 */
@JsExport
object CardIdentityUtils {
    /** Gets the Scryfall ID */
    @JsName("getId")
    fun getId(card: Card): String = card.id

    /** Gets the set code */
    @JsName("getSetCode")
    fun getSetCode(card: Card): String = card.setCode

    /** Gets the set name */
    @JsName("getSetName")
    fun getSetName(card: Card): String = card.setName

    /** Gets the rarity */
    @JsName("getRarity")
    fun getRarity(card: Card): String = card.rarity
}

/**
 * Utility functions for extracting data from ScryfallList.
 *
 * Kotlin/JS mangles property names, so use these functions to access list data.
 *
 * Usage:
 * ```javascript
 * const listUtils = ListUtils.getInstance();
 * const results = await api.search('lightning bolt', null);
 * const cards = listUtils.getData(results);
 * const total = listUtils.getTotalCards(results);
 * ```
 */
@JsExport
object ListUtils {
    /** Gets the data array from a ScryfallList */
    @JsName("getData")
    fun <T> getData(list: ScryfallList<T>): Array<T> = list.data.toTypedArray()

    /** Gets the total number of cards matching the query */
    @JsName("getTotalCards")
    fun <T> getTotalCards(list: ScryfallList<T>): Int = list.totalCards ?: list.data.size

    /** Returns true if there are more pages of results */
    @JsName("hasMore")
    fun <T> hasMore(list: ScryfallList<T>): Boolean = list.hasMore

    /** Gets the URL for the next page of results, if available */
    @JsName("getNextPage")
    fun <T> getNextPage(list: ScryfallList<T>): String? = list.nextPage

    /** Gets the object type (usually "list") */
    @JsName("getObjectType")
    fun <T> getObjectType(list: ScryfallList<T>): String = list.objectType
}

/**
 * Utility functions for accessing Set identity properties.
 *
 * Usage:
 * ```javascript
 * import { SetIdentityUtils } from '@devmugi/scryfall-api';
 *
 * const name = SetIdentityUtils.getName(set);
 * ```
 */
@JsExport
object SetIdentityUtils {
    @JsName("getId")
    fun getId(set: Set): String = set.id

    @JsName("getCode")
    fun getCode(set: Set): String = set.code

    @JsName("getMtgoCode")
    fun getMtgoCode(set: Set): String? = set.mtgoCode

    @JsName("getArenaCode")
    fun getArenaCode(set: Set): String? = set.arenaCode

    @JsName("getTcgplayerId")
    fun getTcgplayerId(set: Set): Int? = set.tcgplayerId

    @JsName("getName")
    fun getName(set: Set): String = set.name

    @JsName("getSetType")
    fun getSetType(set: Set): String = set.setType

    @JsName("getReleasedAt")
    fun getReleasedAt(set: Set): String? = set.releasedAt

    @JsName("getBlockCode")
    fun getBlockCode(set: Set): String? = set.blockCode

    @JsName("getBlock")
    fun getBlock(set: Set): String? = set.block
}

/**
 * Utility functions for accessing Set metadata properties.
 *
 * Usage:
 * ```javascript
 * import { SetMetadataUtils } from '@devmugi/scryfall-api';
 *
 * const cardCount = SetMetadataUtils.getCardCount(set);
 * ```
 */
@JsExport
object SetMetadataUtils {
    @JsName("getParentSetCode")
    fun getParentSetCode(set: Set): String? = set.parentSetCode

    @JsName("getCardCount")
    fun getCardCount(set: Set): Int = set.cardCount

    @JsName("getPrintedSize")
    fun getPrintedSize(set: Set): Int? = set.printedSize

    @JsName("isDigital")
    fun isDigital(set: Set): Boolean = set.digital

    @JsName("isFoilOnly")
    fun isFoilOnly(set: Set): Boolean = set.foilOnly

    @JsName("isNonfoilOnly")
    fun isNonfoilOnly(set: Set): Boolean = set.nonfoilOnly

    @JsName("getScryfallUri")
    fun getScryfallUri(set: Set): String = set.scryfallUri

    @JsName("getUri")
    fun getUri(set: Set): String = set.uri

    @JsName("getIconSvgUri")
    fun getIconSvgUri(set: Set): String = set.iconSvgUri

    @JsName("getSearchUri")
    fun getSearchUri(set: Set): String = set.searchUri
}

/**
 * Utility functions for accessing Ruling properties.
 *
 * Usage:
 * ```javascript
 * const rulingUtils = RulingUtils.getInstance();
 * const comment = rulingUtils.getComment(ruling);
 * ```
 */
@JsExport
object RulingUtils {
    @JsName("getOracleId")
    fun getOracleId(ruling: Ruling): String = ruling.oracleId

    @JsName("getSource")
    fun getSource(ruling: Ruling): String = ruling.source

    @JsName("getPublishedAt")
    fun getPublishedAt(ruling: Ruling): String = ruling.publishedAt

    @JsName("getComment")
    fun getComment(ruling: Ruling): String = ruling.comment
}

/**
 * Utility functions for accessing Catalog properties.
 *
 * Usage:
 * ```javascript
 * const catalogUtils = CatalogUtils.getInstance();
 * const data = catalogUtils.getData(catalog);
 * ```
 */
@JsExport
object CatalogUtils {
    @JsName("getUri")
    fun getUri(catalog: Catalog): String = catalog.uri

    @JsName("getTotalValues")
    fun getTotalValues(catalog: Catalog): Int = catalog.totalValues

    @JsName("getData")
    fun getData(catalog: Catalog): Array<String> = catalog.data.toTypedArray()
}

/**
 * Utility functions for accessing CardSymbol basic properties.
 *
 * Usage:
 * ```javascript
 * import { CardSymbolPropertyUtils } from '@devmugi/scryfall-api';
 *
 * const symbol = CardSymbolPropertyUtils.getSymbol(cardSymbol);
 * ```
 */
@JsExport
object CardSymbolPropertyUtils {
    @JsName("getSymbol")
    fun getSymbol(symbol: CardSymbol): String = symbol.symbol

    @JsName("getLooseVariant")
    fun getLooseVariant(symbol: CardSymbol): String? = symbol.looseVariant

    @JsName("getEnglish")
    fun getEnglish(symbol: CardSymbol): String = symbol.english

    @JsName("getManaValue")
    fun getManaValue(symbol: CardSymbol): Double? = symbol.manaValue

    @JsName("getColors")
    fun getColors(symbol: CardSymbol): Array<String> = symbol.colors.toTypedArray()

    @JsName("getGathererAlternates")
    fun getGathererAlternates(symbol: CardSymbol): Array<String>? = symbol.gathererAlternates?.toTypedArray()

    @JsName("getSvgUri")
    fun getSvgUri(symbol: CardSymbol): String? = symbol.svgUri
}

/**
 * Utility functions for accessing CardSymbol boolean flags.
 *
 * Usage:
 * ```javascript
 * import { CardSymbolFlagsUtils } from '@devmugi/scryfall-api';
 *
 * if (CardSymbolFlagsUtils.representsMana(symbol)) {
 *   console.log('This symbol represents mana');
 * }
 * ```
 */
@JsExport
object CardSymbolFlagsUtils {
    @JsName("isTransposable")
    fun isTransposable(symbol: CardSymbol): Boolean = symbol.transposable

    @JsName("representsMana")
    fun representsMana(symbol: CardSymbol): Boolean = symbol.representsMana

    @JsName("appearsInManaCosts")
    fun appearsInManaCosts(symbol: CardSymbol): Boolean = symbol.appearsInManaCosts

    @JsName("isFunny")
    fun isFunny(symbol: CardSymbol): Boolean = symbol.funny

    @JsName("isHybrid")
    fun isHybrid(symbol: CardSymbol): Boolean = symbol.hybrid

    @JsName("isPhyrexian")
    fun isPhyrexian(symbol: CardSymbol): Boolean = symbol.phyrexian
}

/**
 * Utility functions for accessing ParsedManaCost properties.
 *
 * Usage:
 * ```javascript
 * const manaCostUtils = ParsedManaCostUtils.getInstance();
 * const cmc = manaCostUtils.getCmc(parsedCost);
 * ```
 */
@JsExport
object ParsedManaCostUtils {
    @JsName("getCost")
    fun getCost(cost: ParsedManaCost): String = cost.cost

    @JsName("getCmc")
    fun getCmc(cost: ParsedManaCost): Double = cost.cmc

    @JsName("getColors")
    fun getColors(cost: ParsedManaCost): Array<String> = cost.colors.toTypedArray()

    @JsName("isColorless")
    fun isColorless(cost: ParsedManaCost): Boolean = cost.colorless

    @JsName("isMonocolored")
    fun isMonocolored(cost: ParsedManaCost): Boolean = cost.monocolored

    @JsName("isMulticolored")
    fun isMulticolored(cost: ParsedManaCost): Boolean = cost.multicolored
}

/**
 * Utility functions for accessing BulkData properties.
 *
 * Usage:
 * ```javascript
 * const bulkDataUtils = BulkDataUtils.getInstance();
 * const downloadUri = bulkDataUtils.getDownloadUri(bulkData);
 * ```
 */
@JsExport
object BulkDataUtils {
    @JsName("getId")
    fun getId(data: BulkData): String = data.id

    @JsName("getUri")
    fun getUri(data: BulkData): String = data.uri

    @JsName("getType")
    fun getType(data: BulkData): String = data.type

    @JsName("getName")
    fun getName(data: BulkData): String = data.name

    @JsName("getDescription")
    fun getDescription(data: BulkData): String = data.description

    @JsName("getDownloadUri")
    fun getDownloadUri(data: BulkData): String = data.downloadUri

    @JsName("getUpdatedAt")
    fun getUpdatedAt(data: BulkData): String = data.updatedAt

    @JsName("getSize")
    fun getSize(data: BulkData): Double = data.size.toDouble()

    @JsName("getContentType")
    fun getContentType(data: BulkData): String = data.contentType

    @JsName("getContentEncoding")
    fun getContentEncoding(data: BulkData): String = data.contentEncoding
}

// Rarity order constants for sorting
private object RarityOrder {
    const val COMMON = 1
    const val UNCOMMON = 2
    const val RARE = 3
    const val MYTHIC = 4
    const val SPECIAL = 5
    const val BONUS = 6
    const val UNKNOWN = 999
}

private val rarityOrderMap = mapOf(
    "common" to RarityOrder.COMMON,
    "uncommon" to RarityOrder.UNCOMMON,
    "rare" to RarityOrder.RARE,
    "mythic" to RarityOrder.MYTHIC,
    "special" to RarityOrder.SPECIAL,
    "bonus" to RarityOrder.BONUS
)

/**
 * Utility functions for filtering cards by type, rarity, CMC, and legality.
 *
 * Usage:
 * ```javascript
 * import { CardFilterUtils } from '@devmugi/scryfall-api';
 *
 * const creatures = CardFilterUtils.filterByType(cards, "Creature");
 * const rares = CardFilterUtils.filterByRarity(cards, "rare");
 * ```
 */
@JsExport
object CardFilterUtils {
    /** Filter cards by type line (e.g., "Creature", "Instant", "Dragon") */
    @JsName("filterByType")
    fun filterByType(cards: Array<Card>, type: String): Array<Card> =
        cards.filter { it.typeLine.contains(type, ignoreCase = true) }.toTypedArray()

    /** Filter cards by rarity (common, uncommon, rare, mythic) */
    @JsName("filterByRarity")
    fun filterByRarity(cards: Array<Card>, rarity: String): Array<Card> =
        cards.filter { it.rarity.equals(rarity, ignoreCase = true) }.toTypedArray()

    /** Filter cards by exact mana value */
    @JsName("filterByCmc")
    fun filterByCmc(cards: Array<Card>, cmc: Double): Array<Card> = cards.filter { it.manaValue == cmc }.toTypedArray()

    /** Filter cards by mana value range */
    @JsName("filterByCmcRange")
    fun filterByCmcRange(cards: Array<Card>, min: Double?, max: Double?): Array<Card> =
        cards.filter { card ->
            val cmc = card.manaValue
            val meetsMin = min == null || cmc >= min
            val meetsMax = max == null || cmc <= max
            meetsMin && meetsMax
        }.toTypedArray()

    /** Filter cards legal in a format (standard, modern, commander, etc.) */
    @JsName("filterLegalIn")
    fun filterLegalIn(cards: Array<Card>, format: String): Array<Card> =
        cards.filter { card ->
            val legality = when (format.lowercase()) {
                "standard" -> card.legalities?.standard
                "pioneer" -> card.legalities?.pioneer
                "modern" -> card.legalities?.modern
                "legacy" -> card.legalities?.legacy
                "vintage" -> card.legalities?.vintage
                "commander", "edh" -> card.legalities?.commander
                "pauper" -> card.legalities?.pauper
                else -> null
            }
            legality == "legal"
        }.toTypedArray()

    /** Filter creature cards */
    @JsName("filterCreatures")
    fun filterCreatures(cards: Array<Card>): Array<Card> =
        cards.filter { it.typeLine.contains("Creature", ignoreCase = true) }.toTypedArray()

    /** Filter instant cards */
    @JsName("filterInstants")
    fun filterInstants(cards: Array<Card>): Array<Card> =
        cards.filter { it.typeLine.contains("Instant", ignoreCase = true) }.toTypedArray()

    /** Filter sorcery cards */
    @JsName("filterSorceries")
    fun filterSorceries(cards: Array<Card>): Array<Card> =
        cards.filter { it.typeLine.contains("Sorcery", ignoreCase = true) }.toTypedArray()

    /** Filter land cards */
    @JsName("filterLands")
    fun filterLands(cards: Array<Card>): Array<Card> =
        cards.filter { it.typeLine.contains("Land", ignoreCase = true) }.toTypedArray()
}

/**
 * Utility functions for filtering cards by color.
 *
 * Usage:
 * ```javascript
 * import { CardColorFilterUtils } from '@devmugi/scryfall-api';
 *
 * const redCards = CardColorFilterUtils.filterByColor(cards, "R");
 * const colorless = CardColorFilterUtils.filterColorless(cards);
 * ```
 */
@JsExport
object CardColorFilterUtils {
    /** Filter cards by exact color identity */
    @JsName("filterByColor")
    fun filterByColor(cards: Array<Card>, vararg colors: String): Array<Card> {
        val targetColors = colors.toSet()
        return cards.filter { card ->
            val cardColors = card.colors?.toSet() ?: emptySet()
            cardColors == targetColors
        }.toTypedArray()
    }

    /** Filter cards that contain any of the specified colors */
    @JsName("filterByColorIncludes")
    fun filterByColorIncludes(cards: Array<Card>, vararg colors: String): Array<Card> =
        cards.filter { card ->
            colors.any { color -> card.colors?.contains(color) == true }
        }.toTypedArray()

    /** Filter colorless cards */
    @JsName("filterColorless")
    fun filterColorless(cards: Array<Card>): Array<Card> = cards.filter { it.colors.isNullOrEmpty() }.toTypedArray()

    /** Filter multicolored cards */
    @JsName("filterMulticolored")
    fun filterMulticolored(cards: Array<Card>): Array<Card> = cards.filter { (it.colors?.size ?: 0) > 1 }.toTypedArray()
}

/**
 * Utility functions for sorting card arrays.
 *
 * Usage:
 * ```javascript
 * import { CardSortUtils } from '@devmugi/scryfall-api';
 *
 * const sorted = CardSortUtils.sortByCmc(cards);
 * const byRarity = CardSortUtils.sortByRarity(cards);
 * ```
 */
@JsExport
object CardSortUtils {
    /** Sort cards alphabetically by name */
    @JsName("sortByName")
    fun sortByName(cards: Array<Card>): Array<Card> = cards.sortedBy { it.name }.toTypedArray()

    /** Sort cards by mana value */
    @JsName("sortByCmc")
    fun sortByCmc(cards: Array<Card>): Array<Card> = cards.sortedBy { it.manaValue }.toTypedArray()

    /** Sort cards by USD price (nulls at end) */
    @JsName("sortByPrice")
    fun sortByPrice(cards: Array<Card>): Array<Card> =
        cards.sortedBy { it.prices?.usd?.toDoubleOrNull() ?: Double.MAX_VALUE }.toTypedArray()

    /** Sort cards by rarity (common -> mythic) */
    @JsName("sortByRarity")
    fun sortByRarity(cards: Array<Card>): Array<Card> =
        cards.sortedBy { rarityOrderMap[it.rarity.lowercase()] ?: RarityOrder.UNKNOWN }.toTypedArray()
}

/**
 * Utility functions for grouping card arrays.
 *
 * Usage:
 * ```javascript
 * import { CardGroupUtils } from '@devmugi/scryfall-api';
 *
 * const bySet = CardGroupUtils.groupBySet(cards);
 * const byRarity = CardGroupUtils.groupByRarity(cards);
 * ```
 */
@JsExport
object CardGroupUtils {
    /** Group cards by set code */
    @JsName("groupBySet")
    fun groupBySet(cards: Array<Card>): dynamic {
        val obj = js("{}")
        cards.groupBy { it.setCode }.forEach { (setCode, setCards) ->
            obj[setCode] = setCards.toTypedArray()
        }
        return obj
    }

    /** Group cards by rarity */
    @JsName("groupByRarity")
    fun groupByRarity(cards: Array<Card>): dynamic {
        val obj = js("{}")
        cards.groupBy { it.rarity }.forEach { (rarity, rarityCards) ->
            obj[rarity] = rarityCards.toTypedArray()
        }
        return obj
    }

    /** Group cards by mana value (CMC) */
    @JsName("groupByCmc")
    fun groupByCmc(cards: Array<Card>): dynamic {
        val obj = js("{}")
        cards.groupBy { it.manaValue.toInt() }.forEach { (cmc, cmcCards) ->
            obj[cmc.toString()] = cmcCards.toTypedArray()
        }
        return obj
    }
}

/**
 * Utility for loading additional pages of paginated results.
 *
 * Usage:
 * ```javascript
 * const pager = new PaginationHelper();
 * const results = await api.search('creature');
 * if (results.hasMore) {
 *   const nextResults = await pager.loadNextCardPage(results.nextPage);
 * }
 * ```
 */
@JsExport
class PaginationHelper {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }

    /**
     * Load the next page of card results.
     *
     * @param nextPageUrl The nextPage URL from a previous search result
     * @return Promise resolving to the next page of cards, or null if URL is empty
     */
    @JsName("loadNextCardPage")
    fun loadNextCardPage(nextPageUrl: String?): Promise<JsScryfallCardList?> =
        GlobalScope.promise {
            if (nextPageUrl.isNullOrBlank()) {
                null
            } else {
                val response: ScryfallList<Card> = client.get(nextPageUrl).body()
                response.toJs()
            }
        }

    /**
     * Load the next page of set results.
     *
     * @param nextPageUrl The nextPage URL from a previous result
     * @return Promise resolving to the next page of sets, or null if URL is empty
     */
    @JsName("loadNextSetPage")
    fun loadNextSetPage(nextPageUrl: String?): Promise<JsScryfallSetList?> =
        GlobalScope.promise {
            if (nextPageUrl.isNullOrBlank()) {
                null
            } else {
                val response: ScryfallList<Set> = client.get(nextPageUrl).body()
                response.toJs()
            }
        }
}
