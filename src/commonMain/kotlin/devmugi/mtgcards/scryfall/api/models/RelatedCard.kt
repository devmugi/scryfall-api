package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Related card reference (meld parts, combo pieces, tokens, etc.).
 */
@Serializable
data class RelatedCard(
    /** Related object's unique ID. */
    @SerialName("id")
    val id: String,
    /** Content type, always "related_card". */
    @SerialName("object")
    val objectType: String,
    /** Role this card plays (combo_piece, token, meld_part, etc.). */
    @SerialName("component")
    val component: String,
    /** Name of the related card. */
    @SerialName("name")
    val name: String,
    /** Type line of the related card. */
    @SerialName("type_line")
    val typeLine: String,
    /** API URI for the related card. */
    @SerialName("uri")
    val uri: String
)
