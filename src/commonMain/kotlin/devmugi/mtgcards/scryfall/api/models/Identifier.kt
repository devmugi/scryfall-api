package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Identifier(
    val id: String? = null,
    @SerialName("oracle_id") val oracleId: String? = null,
    @SerialName("illustration_id") val illustrationId: String? = null,
    @SerialName("multiverse_id") val multiverseId: Int? = null,
    @SerialName("mtgo_id") val mtgoId: Int? = null,
    @SerialName("arena_id") val arenaId: Int? = null,
    val set: String? = null,
    @SerialName("collector_number") val collectorNumber: String? = null,
    val name: String? = null,
)
