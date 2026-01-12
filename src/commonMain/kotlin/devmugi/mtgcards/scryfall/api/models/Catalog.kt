package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Catalog(
    @SerialName("object") val objectType: String = "catalog",
    val uri: String,
    @SerialName("total_values") val totalValues: Int,
    val data: List<String>,
)
