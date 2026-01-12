package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Preview metadata supplied by Scryfall.
 */
@Serializable
data class Preview(
    @SerialName("previewed_at")
    val previewedAt: String? = null,
    @SerialName("source_uri")
    val sourceUri: String? = null,
    @SerialName("source")
    val source: String? = null
)
