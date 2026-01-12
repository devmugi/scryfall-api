package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://scryfall.com/docs/api/card-symbols
 */
@Serializable
data class CardSymbol(
    @SerialName("object") val objectType: String = "card_symbol",
    val symbol: String,
    @SerialName("loose_variant") val looseVariant: String? = null,
    val english: String,
    val transposable: Boolean,
    @SerialName("represents_mana") val representsMana: Boolean,
    @SerialName("mana_value") val manaValue: Double? = null,
    @SerialName("appears_in_mana_costs") val appearsInManaCosts: Boolean,
    val funny: Boolean,
    val colors: List<String>,
    val hybrid: Boolean,
    val phyrexian: Boolean,
    @SerialName("gatherer_alternates") val gathererAlternates: List<String>? = null,
    @SerialName("svg_uri") val svgUri: String? = null,
)
