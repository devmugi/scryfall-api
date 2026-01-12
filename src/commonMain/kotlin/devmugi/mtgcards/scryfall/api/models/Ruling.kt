package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ruling(
    @SerialName("object") val objectType: String = "ruling",
    @SerialName("oracle_id") val oracleId: String,
    @SerialName("source") val source: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("comment") val comment: String,
)
