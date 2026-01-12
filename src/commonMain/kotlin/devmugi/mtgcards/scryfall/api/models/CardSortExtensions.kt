package devmugi.mtgcards.scryfall.api.models

/*
 * Extension functions for sorting lists of cards.
 */

/**
 * Sorts cards alphabetically by name.
 * @return List of cards sorted by name
 */
fun List<Card>.sortByName(): List<Card> = sortedBy { it.name }

/**
 * Sorts cards by converted mana cost (mana value).
 * @return List of cards sorted by CMC
 */
fun List<Card>.sortByCmc(): List<Card> = sortedBy { it.manaValue }

/**
 * Sorts cards by USD price.
 * Cards with null or unparseable prices are sorted to the end.
 * @return List of cards sorted by price
 */
fun List<Card>.sortByPrice(): List<Card> = sortedBy { it.prices?.usd?.toDoubleOrNull() ?: Double.MAX_VALUE }

/**
 * Sorts cards by rarity in order: common, uncommon, rare, mythic.
 * @return List of cards sorted by rarity
 */
@Suppress("MagicNumber") // Ordering weights, not magic numbers
fun List<Card>.sortByRarity(): List<Card> {
    val rarityOrder = mapOf(
        "common" to 1,
        "uncommon" to 2,
        "rare" to 3,
        "mythic" to 4,
        "special" to 5,
        "bonus" to 6
    )
    return sortedBy { card ->
        rarityOrder[card.rarity.lowercase()] ?: 999
    }
}

/**
 * Sorts cards by release date.
 * @return List of cards sorted by release date (oldest first)
 */
fun List<Card>.sortByReleaseDate(): List<Card> = sortedBy { it.releasedAt }

/**
 * Sorts cards by collector number.
 * Numeric collector numbers are sorted numerically, others by hash code.
 * @return List of cards sorted by collector number
 */
fun List<Card>.sortByCollectorNumber(): List<Card> =
    sortedWith(
        compareBy(
            naturalOrder()
        ) { card ->
            // Try to parse as integer if possible, otherwise use string comparison
            card.collectorNumber.toIntOrNull() ?: card.collectorNumber.hashCode()
        }
    )
