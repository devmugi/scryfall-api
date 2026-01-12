@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
data class JsSet(
    val objectType: String = "set",
    val id: String,
    val code: String,
    val mtgoCode: String? = null,
    val arenaCode: String? = null,
    val tcgplayerId: Int? = null,
    val name: String,
    val setType: String,
    val releasedAt: String? = null,
    val blockCode: String? = null,
    val block: String? = null,
    val parentSetCode: String? = null,
    val cardCount: Int,
    val printedSize: Int? = null,
    val digital: Boolean = false,
    val foilOnly: Boolean,
    val nonfoilOnly: Boolean,
    val scryfallUri: String,
    val uri: String,
    val iconSvgUri: String,
    val searchUri: String,
)

@JsExport
@Suppress("ArrayInDataClass")
data class JsScryfallSetList(
    val objectType: String = "list",
    val hasMore: Boolean = false,
    val nextPage: String? = null,
    val totalCards: Int? = null,
    val data: Array<JsSet> = emptyArray(),
    val warnings: Array<String>? = null,
    val notFound: Array<JsNotFound>? = null,
)

@JsExport
data class JsNotFound(
    val objectType: String = "not_found",
    val name: String? = null,
    val set: String? = null,
    val collectorNumber: String? = null,
    val type: String? = null
)
