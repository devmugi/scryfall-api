package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * https://scryfall.com/docs/api/card-symbols/parse-mana
 */
@Serializable
data class ParsedManaCost(
    @SerialName("object") val objectType: String = "mana_cost",
    val cost: String,
    val cmc: Double,
    val colors: List<String>,
    val colorless: Boolean,
    val monocolored: Boolean,
    val multicolored: Boolean
)
