package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A Set object represents a group of related Magic cards. All Card objects on Scryfall belong to exactly one set.
 * https://scryfall.com/docs/api/sets
 */
@Serializable
data class Set(
    @SerialName("object") val objectType: String = "set",
    val id: String,
    val code: String,
    @SerialName("mtgo_code") val mtgoCode: String? = null,
    @SerialName("arena_code") val arenaCode: String? = null,
    @SerialName("tcgplayer_id") val tcgplayerId: Int? = null,
    val name: String,
    @SerialName("set_type") val setType: String,
    @SerialName("released_at") val releasedAt: String? = null,
    @SerialName("block_code") val blockCode: String? = null,
    @SerialName("block") val block: String? = null,
    @SerialName("parent_set_code") val parentSetCode: String? = null,
    @SerialName("card_count") val cardCount: Int,
    @SerialName("printed_size") val printedSize: Int? = null,
    @SerialName("digital") val digital: Boolean = false,
    @SerialName("foil_only") val foilOnly: Boolean,
    @SerialName("nonfoil_only") val nonfoilOnly: Boolean,
    @SerialName("scryfall_uri") val scryfallUri: String,
    @SerialName("uri") val uri: String,
    @SerialName("icon_svg_uri") val iconSvgUri: String,
    @SerialName("search_uri") val searchUri: String,
)
