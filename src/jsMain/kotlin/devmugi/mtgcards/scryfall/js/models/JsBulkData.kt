@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Suppress("ArrayInDataClass")
data class JsScryfallBulkDataList(
    val objectType: String = "list",
    val hasMore: Boolean = false,
    val nextPage: String? = null,
    val data: Array<JsBulkData> = emptyArray(),
)

@JsExport
data class JsBulkData(
    val objectType: String = "bulk_data",
    val id: String,
    val uri: String,
    val type: String,
    val name: String,
    val description: String,
    val downloadUri: String,
    val updatedAt: String,
    val size: Int,
    val contentType: String,
    val contentEncoding: String,
)
