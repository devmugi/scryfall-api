package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Individual face information for multiface cards.
 */
@Serializable
data class CardFace(
    /** Illustrator for this face, if any. */
    @SerialName("artist")
    val artist: String? = null,
    /** Illustrator ID for this face, if known. */
    @SerialName("artist_id")
    val artistId: String? = null,
    /** Mana value for this face, if the card is reversible. */
    @SerialName("cmc")
    val manaValue: Double? = null,
    /** Colors shown in this face's color indicator, if any. */
    @SerialName("color_indicator")
    val colorIndicator: List<String>? = null,
    /** Colors defined for this face. */
    @SerialName("colors")
    val colors: List<String>? = null,
    /** Defense value for this face, if any. */
    @SerialName("defense")
    val defense: String? = null,
    /** Flavor text printed on this face, if any. */
    @SerialName("flavor_text")
    val flavorText: String? = null,
    /** Artwork identifier that stays consistent across reprints, if known. */
    @SerialName("illustration_id")
    val illustrationId: String? = null,
    /** Imagery specific to this face (double-faced cards). */
    @SerialName("image_uris")
    val imageUris: ImageUris? = null,
    /** Layout for this particular face. */
    @SerialName("layout")
    val layout: String? = null,
    /** Loyalty value printed on this face, if any. */
    @SerialName("loyalty")
    val loyalty: String? = null,
    /** Mana cost for this face. */
    @SerialName("mana_cost")
    val manaCost: String,
    /** Name of this face. */
    @SerialName("name")
    val name: String,
    /** Content type, always "card_face". */
    @SerialName("object")
    val objectType: String,
    /** Oracle ID for this face, if reversible. */
    @SerialName("oracle_id")
    val oracleId: String? = null,
    /** Oracle text printed on this face, if any. */
    @SerialName("oracle_text")
    val oracleText: String? = null,
    /** Power for this face, if any. Can include non-numeric values. */
    @SerialName("power")
    val power: String? = null,
    /** Localized name printed on this face, if any. */
    @SerialName("printed_name")
    val printedName: String? = null,
    /** Localized text printed on this face, if any. */
    @SerialName("printed_text")
    val printedText: String? = null,
    /** Localized type line printed on this face, if any. */
    @SerialName("printed_type_line")
    val printedTypeLine: String? = null,
    /** Toughness for this face, if any. */
    @SerialName("toughness")
    val toughness: String? = null,
    /** Type line for this face. */
    @SerialName("type_line")
    val typeLine: String? = null,
    /** Watermark printed on this face, if any. */
    @SerialName("watermark")
    val watermark: String? = null
)
