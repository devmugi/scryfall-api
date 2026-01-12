package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Price information in multiple currencies.
 */
@Serializable
data class Prices(
    @SerialName("usd")
    val usd: String? = null,
    @SerialName("usd_foil")
    val usdFoil: String? = null,
    @SerialName("usd_etched")
    val usdEtched: String? = null,
    @SerialName("eur")
    val eur: String? = null,
    @SerialName("eur_foil")
    val eurFoil: String? = null,
    @SerialName("eur_etched")
    val eurEtched: String? = null,
    @SerialName("tix")
    val tix: String? = null
)
