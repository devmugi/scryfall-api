package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// https://scryfall.com/docs/api/cards/collection
@Serializable
data class IdentifiersRequest(
    @SerialName("identifiers") val identifiers: List<Identifier>,
    @SerialName("pretty") val pretty: Boolean? = false
)
