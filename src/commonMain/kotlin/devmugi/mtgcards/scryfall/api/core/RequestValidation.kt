@file:Suppress("MagicNumber")

package devmugi.mtgcards.scryfall.api.core

/**
 * Validates a search query string.
 *
 * @param query The search query to validate
 * @throws IllegalArgumentException if the query is invalid
 */
internal fun validateQuery(query: String) {
    require(query.isNotBlank()) { "Query cannot be blank" }
    require(query.length <= 1000) { "Query too long (max 1000 characters)" }
}

/**
 * Validates a card name.
 *
 * @param name The card name to validate
 * @throws IllegalArgumentException if the name is invalid
 */
internal fun validateCardName(name: String) {
    require(name.isNotBlank()) { "Card name cannot be blank" }
    require(name.length <= 500) { "Card name too long (max 500 characters)" }
}

/**
 * Validates a page number.
 *
 * @param page The page number to validate
 * @throws IllegalArgumentException if the page number is invalid
 */
internal fun validatePage(page: Int) {
    require(page > 0) { "Page must be greater than 0" }
    require(page <= 10000) { "Page number too large (max 10000)" }
}

/**
 * Validates a set code.
 *
 * @param code The set code to validate
 * @throws IllegalArgumentException if the code is invalid
 */
internal fun validateSetCode(code: String) {
    require(code.isNotBlank()) { "Set code cannot be blank" }
    require(code.length in 3..5) { "Set code must be 3-5 characters" }
    require(code.all { it.isLetterOrDigit() }) { "Set code must contain only letters and digits" }
}

/**
 * Validates a Scryfall ID (UUID format).
 *
 * @param id The ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateScryfallId(id: String) {
    require(id.isNotBlank()) { "ID cannot be blank" }
    // UUID format: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx (36 characters with hyphens)
    val uuidRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$")
    require(uuidRegex.matches(id)) { "ID must be a valid UUID format" }
}

/**
 * Validates a collector number.
 *
 * @param number The collector number to validate
 * @throws IllegalArgumentException if the number is invalid
 */
internal fun validateCollectorNumber(number: String) {
    require(number.isNotBlank()) { "Collector number cannot be blank" }
    require(number.length <= 10) { "Collector number too long (max 10 characters)" }
}

/**
 * Validates a list of identifiers for the collection endpoint.
 *
 * @param identifiers The list of identifiers to validate
 * @throws IllegalArgumentException if the list is invalid
 */
internal fun validateIdentifiers(identifiers: List<*>) {
    require(identifiers.isNotEmpty()) { "Identifiers list cannot be empty" }
    require(identifiers.size <= 75) { "Too many identifiers (max 75 per request)" }
}

/**
 * Validates a language code (ISO 639-1 format).
 *
 * @param lang The language code to validate
 * @throws IllegalArgumentException if the language code is invalid
 */
internal fun validateLanguageCode(lang: String) {
    require(lang.isNotBlank()) { "Language code cannot be blank" }
    require(lang.length == 2) { "Language code must be 2 characters (ISO 639-1 format)" }
    require(lang.all { it.isLowerCase() && it.isLetter() }) { "Language code must be lowercase letters" }
}

/**
 * Validates a TCGPlayer ID.
 *
 * @param id The TCGPlayer ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateTcgplayerId(id: Int) {
    require(id > 0) { "TCGPlayer ID must be positive" }
}

/**
 * Validates a Multiverse ID.
 *
 * @param id The Multiverse ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateMultiverseId(id: Int) {
    require(id > 0) { "Multiverse ID must be positive" }
}

/**
 * Validates an Arena ID.
 *
 * @param id The Arena ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateArenaId(id: Int) {
    require(id > 0) { "Arena ID must be positive" }
}

/**
 * Validates an MTGO ID.
 *
 * @param id The MTGO ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateMtgoId(id: Int) {
    require(id > 0) { "MTGO ID must be positive" }
}

/**
 * Validates a Cardmarket ID.
 *
 * @param id The Cardmarket ID to validate
 * @throws IllegalArgumentException if the ID is invalid
 */
internal fun validateCardmarketId(id: Int) {
    require(id > 0) { "Cardmarket ID must be positive" }
}
