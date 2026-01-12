package devmugi.mtgcards.scryfall.api.models

/**
 * The order parameter determines how Scryfall should sort the returned cards.
 */
enum class SortingCards(val value: String) {
    /**
     * Sort cards by name, A → Z
     */
    NAME("name"),

    /**
     * Sort cards by their set and collector number: AAA/#1 → ZZZ/#999
     */
    SET("set"),

    /**
     * Sort cards by their release date: Newest → Oldest
     */
    RELEASED("released"),

    /**
     * Sort cards by rarity (e.g., Common → Mythic)
     */
    RARITY("rarity"),

    /**
     * Sort cards by color and color identity
     */
    COLOR("color"),

    /**
     * Sort cards by their USD price
     */
    USD("usd"),

    /**
     * Sort cards by their Magic Online (tix) price
     */
    TIX("tix"),

    /**
     * Sort cards by their EUR price
     */
    EUR("eur"),

    /**
     * Sort cards by mana value (converted mana cost)
     */
    CMC("cmc"),

    /**
     * Sort cards by power
     */
    POWER("power"),

    /**
     * Sort cards by toughness
     */
    TOUGHNESS("toughness"),

    /**
     * Sort by EDHREC ranking/popularity
     */
    EDHREC("edhrec"),

    /**
     * Sort by Penny Dreadful deck usage and legality data
     */
    PENNY("penny"),

    /**
     * Sort cards by the artist credit
     */
    ARTIST("artist"),

    /**
     * Sort by Scryfall review score
     */
    REVIEW("review")
}
