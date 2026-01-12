package devmugi.mtgcards.scryfall.api.models

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A List object represents a requested sequence of other objects (Cards, Sets, etc). List objects may be paginated, and also include information about issues raised when generating the list.
 * https://scryfall.com/docs/api/lists
 */
@Serializable
data class ScryfallList<T>(
    @SerialName("object") val objectType: String = "list",
    @SerialName("has_more") val hasMore: Boolean = false,
    @SerialName("next_page") val nextPage: String? = null,
    @SerialName("total_cards") val totalCards: Int? = null,
    @SerialName("data") val data: List<T> = emptyList(),
    @SerialName("warnings") val warnings: List<String>? = null,
    @SerialName("not_found") val notFound: List<NotFound>? = null,
)

@Serializable
data class NotFound(
    @SerialName("object") val objectType: String = "not_found",
    val name: String? = null,
    val set: String? = null,
    @SerialName("collector_number") val collectorNumber: String? = null,
    val type: String? = null
)

// Pagination helpers
suspend fun <T> ScryfallList<T>.loadNextPage(client: HttpClient): ScryfallList<T>? {
    val url = nextPage ?: return null
    return client.get(url).body()
}

fun <T> ScryfallList<T>.asFlow(client: HttpClient): Flow<T> =
    flow {
        var current: ScryfallList<T>? = this@asFlow
        while (current != null) {
            current.data.forEach { emit(it) }
            current = current.loadNextPage(client)
        }
    }
