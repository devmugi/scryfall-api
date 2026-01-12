package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScryfallError(
    @SerialName("object") val objectType: String = "error",
    val code: String? = null,
    val status: Int? = null,
    val details: String? = null,
    val type: String? = null,
    val warnings: List<String>? = null
)
