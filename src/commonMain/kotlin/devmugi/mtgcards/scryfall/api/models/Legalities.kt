package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Legal status for each supported format.
 * Possible legalities are legal, not_legal, restricted, and banned.
 */
@Serializable
data class Legalities(
    @SerialName("standard")
    val standard: String? = null,
    @SerialName("future")
    val future: String? = null,
    @SerialName("historic")
    val historic: String? = null,
    @SerialName("timeless")
    val timeless: String? = null,
    @SerialName("gladiator")
    val gladiator: String? = null,
    @SerialName("pioneer")
    val pioneer: String? = null,
    @SerialName("explorer")
    val explorer: String? = null,
    @SerialName("modern")
    val modern: String? = null,
    @SerialName("legacy")
    val legacy: String? = null,
    @SerialName("pauper")
    val pauper: String? = null,
    @SerialName("vintage")
    val vintage: String? = null,
    @SerialName("penny")
    val penny: String? = null,
    @SerialName("commander")
    val commander: String? = null,
    @SerialName("oathbreaker")
    val oathbreaker: String? = null,
    @SerialName("brawl")
    val brawl: String? = null,
    @SerialName("historicbrawl")
    val historicBrawl: String? = null,
    @SerialName("alchemy")
    val alchemy: String? = null,
    @SerialName("paupercommander")
    val pauperCommander: String? = null,
    @SerialName("duel")
    val duel: String? = null,
    @SerialName("oldschool")
    val oldschool: String? = null,
    @SerialName("premodern")
    val premodern: String? = null,
    @SerialName("predh")
    val predh: String? = null,
    @SerialName("standardbrawl")
    val standardBrawl: String? = null
)
