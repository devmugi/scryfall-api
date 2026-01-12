package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BulkData(
    @SerialName("object") val objectType: String = "bulk_data",
    val id: String,
    val uri: String,
    val type: String,
    val name: String,
    val description: String,
    @SerialName("download_uri") val downloadUri: String,
    @SerialName("updated_at") val updatedAt: String,
    val size: Long,
    @SerialName("content_type") val contentType: String,
    @SerialName("content_encoding") val contentEncoding: String,
)
