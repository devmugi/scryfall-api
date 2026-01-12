package devmugi.mtgcards.scryfall.api.models

/*
 * Extension functions for checking card properties (legality, colors, keywords).
 */

/**
 * Map of format names to their corresponding legality getter functions.
 * Using a map reduces cyclomatic complexity compared to a when expression.
 */
private val FORMAT_LEGALITY_MAP: Map<String, (Legalities) -> String?> = mapOf(
    "standard" to { it.standard },
    "future" to { it.future },
    "historic" to { it.historic },
    "timeless" to { it.timeless },
    "gladiator" to { it.gladiator },
    "pioneer" to { it.pioneer },
    "explorer" to { it.explorer },
    "modern" to { it.modern },
    "legacy" to { it.legacy },
    "pauper" to { it.pauper },
    "vintage" to { it.vintage },
    "penny" to { it.penny },
    "commander" to { it.commander },
    "oathbreaker" to { it.oathbreaker },
    "brawl" to { it.brawl },
    "historicbrawl" to { it.historicBrawl },
    "alchemy" to { it.alchemy },
    "paupercommander" to { it.pauperCommander },
    "duel" to { it.duel },
    "oldschool" to { it.oldschool },
    "premodern" to { it.premodern },
    "predh" to { it.predh },
    "standardbrawl" to { it.standardBrawl }
)

/**
 * Checks if this card is legal in the specified format.
 *
 * @param format The format name (e.g., "standard", "modern", "commander")
 * @return true if the card is legal in the format, false otherwise
 *
 * Example:
 * ```kotlin
 * val card = scryfall.cards.namedExact("Lightning Bolt")
 * if (card.isLegalIn("modern")) {
 *     println("Legal in Modern!")
 * }
 * ```
 */
fun Card.isLegalIn(format: String): Boolean {
    val getter = FORMAT_LEGALITY_MAP[format.lowercase()] ?: return false
    return getter(legalities)?.lowercase() == "legal"
}

/**
 * Checks if this card has the specified color.
 *
 * @param color The color code (W, U, B, R, G, or C for colorless)
 * @return true if the card has the specified color
 *
 * Example:
 * ```kotlin
 * if (card.hasColor("R")) {
 *     println("Card is red!")
 * }
 * ```
 */
fun Card.hasColor(color: String): Boolean = colors?.contains(color) == true

/**
 * Checks if this card is colorless.
 * @return true if the card has no colors
 */
fun Card.isColorless(): Boolean = colors.isNullOrEmpty()

/**
 * Checks if this card is multicolored (has more than one color).
 * @return true if the card has multiple colors
 */
fun Card.isMulticolored(): Boolean = (colors?.size ?: 0) > 1

/**
 * Checks if this card has the specified keyword ability.
 *
 * @param keyword The keyword to check (e.g., "flying", "haste")
 * @return true if the card has the keyword
 *
 * Example:
 * ```kotlin
 * if (card.hasKeyword("flying")) {
 *     println("Card has flying!")
 * }
 * ```
 */
fun Card.hasKeyword(keyword: String): Boolean = keywords.contains(keyword)
