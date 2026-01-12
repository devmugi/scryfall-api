package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Available imagery for a card or card face.
 */
@Serializable
data class ImageUris(
    @SerialName("small")
    val small: String? = null,
    @SerialName("normal")
    val normal: String? = null,
    @SerialName("large")
    val large: String? = null,
    @SerialName("png")
    val png: String? = null,
    @SerialName("art_crop")
    val artCrop: String? = null,
    @SerialName("border_crop")
    val borderCrop: String? = null
)
