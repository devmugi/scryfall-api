package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AutocompleteResult(
    @SerialName("object")
    val objectType: String,
    @SerialName("total_values")
    val totalValues: Int,
    @SerialName("data")
    val data: List<String>
)
