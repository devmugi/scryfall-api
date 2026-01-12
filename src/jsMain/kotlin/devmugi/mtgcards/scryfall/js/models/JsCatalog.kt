@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Suppress("ArrayInDataClass")
data class JsCatalog(
    val objectType: String = "catalog",
    val uri: String,
    val totalValues: Int,
    val data: Array<String>,
)
