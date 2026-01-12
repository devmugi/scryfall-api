package devmugi.mtgcards.scryfall.api.models

/*
 * Extension functions for checking card types.
 */

/**
 * Checks if this card is a creature.
 * @return true if the card's type line contains "Creature"
 */
fun Card.isCreature(): Boolean = typeLine.contains("Creature", ignoreCase = true)

/**
 * Checks if this card is an instant.
 * @return true if the card's type line contains "Instant"
 */
fun Card.isInstant(): Boolean = typeLine.contains("Instant", ignoreCase = true)

/**
 * Checks if this card is a sorcery.
 * @return true if the card's type line contains "Sorcery"
 */
fun Card.isSorcery(): Boolean = typeLine.contains("Sorcery", ignoreCase = true)

/**
 * Checks if this card is an artifact.
 * @return true if the card's type line contains "Artifact"
 */
fun Card.isArtifact(): Boolean = typeLine.contains("Artifact", ignoreCase = true)

/**
 * Checks if this card is an enchantment.
 * @return true if the card's type line contains "Enchantment"
 */
fun Card.isEnchantment(): Boolean = typeLine.contains("Enchantment", ignoreCase = true)

/**
 * Checks if this card is a planeswalker.
 * @return true if the card's type line contains "Planeswalker"
 */
fun Card.isPlaneswalker(): Boolean = typeLine.contains("Planeswalker", ignoreCase = true)

/**
 * Checks if this card is a land.
 * @return true if the card's type line contains "Land"
 */
fun Card.isLand(): Boolean = typeLine.contains("Land", ignoreCase = true)

/**
 * Checks if this card is a battle.
 * @return true if the card's type line contains "Battle"
 */
fun Card.isBattle(): Boolean = typeLine.contains("Battle", ignoreCase = true)
