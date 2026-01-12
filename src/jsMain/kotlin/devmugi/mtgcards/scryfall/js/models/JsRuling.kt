@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Suppress("ArrayInDataClass")
data class JsScryfallRulingList(
    val objectType: String = "list",
    val hasMore: Boolean = false,
    val nextPage: String? = null,
    val data: Array<JsRuling> = emptyArray(),
)

@JsExport
data class JsRuling(
    val objectType: String = "ruling",
    val oracleId: String,
    val source: String,
    val publishedAt: String,
    val comment: String,
)
