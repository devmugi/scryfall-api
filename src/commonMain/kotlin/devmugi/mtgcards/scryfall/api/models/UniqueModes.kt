package devmugi.mtgcards.scryfall.api.models

/**
 * The unique parameter specifies if Scryfall should remove “duplicate” results in your query. The options are:
 */
enum class UniqueModes(val value: String) {
    /**
     * Removes duplicate gameplay objects (cards that share a name and have the same functionality). For example, if your search matches more than one print of Pacifism, only one copy of Pacifism will be returned.
     */
    CARDS("cards"), // default value

    /**
     * Returns only one copy of each unique artwork for matching cards. For example, if your search matches more than one print of Pacifism, one card with each different illustration for Pacifism will be returned, but any cards that duplicate artwork already in the results will be omitted.
     */
    ART("art"),

    /**
     * Returns all prints for all cards matched (disables rollup). For example, if your search matches more than one print of Pacifism, all matching prints will be returned.
     */
    PRINTS("prints")
}
