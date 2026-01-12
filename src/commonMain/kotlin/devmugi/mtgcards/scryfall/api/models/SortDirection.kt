package devmugi.mtgcards.scryfall.api.models

/**
 * The direction to sort the cards. If not provided, Scryfall chooses a reasonable default for the order.
 */
enum class SortDirection(val value: String) {
    AUTO("auto"), // default value
    ASC("asc"),
    DESC("desc")
}
