package devmugi.mtgcards.scryfall.api.models

/*
 * Extension functions for filtering lists of cards.
 */

/**
 * Filters cards by type line.
 *
 * @param type The type to filter by (e.g., "Creature", "Instant", "Dragon")
 * @return List of cards whose type line contains the specified type
 *
 * Example:
 * ```kotlin
 * val creatures = cards.filterByType("Creature")
 * val dragons = cards.filterByType("Dragon")
 * ```
 */
fun List<Card>.filterByType(type: String): List<Card> = filter { it.typeLine.contains(type, ignoreCase = true) }

/**
 * Filters cards by exact color identity.
 * Returns only cards that have ALL specified colors (and only those colors).
 *
 * @param colors Variable number of color codes (W, U, B, R, G)
 * @return List of cards with the exact color identity
 *
 * Example:
 * ```kotlin
 * val redGreen = cards.filterByColor("R", "G")
 * ```
 */
fun List<Card>.filterByColor(vararg colors: String): List<Card> =
    filter { card ->
        val cardColors = card.colors?.toSet() ?: emptySet()
        val targetColors = colors.toSet()
        cardColors == targetColors
    }

/**
 * Filters cards that contain any of the specified colors.
 *
 * @param colors Variable number of color codes (W, U, B, R, G)
 * @return List of cards that have at least one of the specified colors
 *
 * Example:
 * ```kotlin
 * val redOrBlue = cards.filterByColorIncludes("R", "U")
 * ```
 */
fun List<Card>.filterByColorIncludes(vararg colors: String): List<Card> =
    filter { card ->
        colors.any { color -> card.hasColor(color) }
    }

/**
 * Filters cards that are legal in the specified format.
 *
 * @param format The format name (e.g., "standard", "modern", "commander")
 * @return List of cards legal in the format
 *
 * Example:
 * ```kotlin
 * val modernLegal = cards.filterLegalIn("modern")
 * ```
 */
fun List<Card>.filterLegalIn(format: String): List<Card> = filter { it.isLegalIn(format) }

/**
 * Filters cards by rarity.
 *
 * @param rarity The rarity to filter by (common, uncommon, rare, mythic)
 * @return List of cards with the specified rarity
 *
 * Example:
 * ```kotlin
 * val mythics = cards.filterByRarity("mythic")
 * ```
 */
fun List<Card>.filterByRarity(rarity: String): List<Card> = filter { it.rarity.equals(rarity, ignoreCase = true) }

/**
 * Filters cards by converted mana cost (mana value).
 *
 * @param cmc The exact converted mana cost to filter by
 * @return List of cards with the specified CMC
 *
 * Example:
 * ```kotlin
 * val threeDrop = cards.filterByCmc(3.0)
 * ```
 */
fun List<Card>.filterByCmc(cmc: Double): List<Card> = filter { it.manaValue == cmc }

/**
 * Filters cards by CMC range.
 *
 * @param min Minimum CMC (inclusive), or null for no minimum
 * @param max Maximum CMC (inclusive), or null for no maximum
 * @return List of cards within the CMC range
 *
 * Example:
 * ```kotlin
 * val lowCost = cards.filterByCmcRange(min = 0.0, max = 3.0)
 * val expensive = cards.filterByCmcRange(min = 7.0)
 * ```
 */
fun List<Card>.filterByCmcRange(min: Double? = null, max: Double? = null): List<Card> =
    filter { card ->
        val cmc = card.manaValue
        val meetsMin = min == null || cmc >= min
        val meetsMax = max == null || cmc <= max
        meetsMin && meetsMax
    }

/**
 * Filters cards that are creatures.
 * @return List of creature cards
 */
fun List<Card>.filterCreatures(): List<Card> = filter { it.isCreature() }

/**
 * Filters cards that are instants.
 * @return List of instant cards
 */
fun List<Card>.filterInstants(): List<Card> = filter { it.isInstant() }

/**
 * Filters cards that are sorceries.
 * @return List of sorcery cards
 */
fun List<Card>.filterSorceries(): List<Card> = filter { it.isSorcery() }

/**
 * Filters cards that are artifacts.
 * @return List of artifact cards
 */
fun List<Card>.filterArtifacts(): List<Card> = filter { it.isArtifact() }

/**
 * Filters cards that are enchantments.
 * @return List of enchantment cards
 */
fun List<Card>.filterEnchantments(): List<Card> = filter { it.isEnchantment() }

/**
 * Filters cards that are planeswalkers.
 * @return List of planeswalker cards
 */
fun List<Card>.filterPlaneswalkers(): List<Card> = filter { it.isPlaneswalker() }

/**
 * Filters cards that are lands.
 * @return List of land cards
 */
fun List<Card>.filterLands(): List<Card> = filter { it.isLand() }

/**
 * Filters cards that are colorless.
 * @return List of colorless cards
 */
fun List<Card>.filterColorless(): List<Card> = filter { it.isColorless() }

/**
 * Filters cards that are multicolored.
 * @return List of multicolored cards
 */
fun List<Card>.filterMulticolored(): List<Card> = filter { it.isMulticolored() }
