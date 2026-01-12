@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Suppress("ArrayInDataClass")
data class JsScryfallCardSymbolList(
    val objectType: String = "list",
    val hasMore: Boolean = false,
    val nextPage: String? = null,
    val data: Array<JsCardSymbol> = emptyArray(),
)

@JsExport
@Suppress("ArrayInDataClass")
data class JsCardSymbol(
    val objectType: String = "card_symbol",
    val symbol: String,
    val looseVariant: String? = null,
    val english: String,
    val transposable: Boolean,
    val representsMana: Boolean,
    val manaValue: Double? = null,
    val appearsInManaCosts: Boolean,
    val funny: Boolean,
    val colors: Array<String>,
    val hybrid: Boolean,
    val phyrexian: Boolean,
    val gathererAlternates: Array<String>? = null,
    val svgUri: String? = null,
)

@JsExport
@Suppress("ArrayInDataClass")
data class JsParsedManaCost(
    val objectType: String = "mana_cost",
    val cost: String,
    val cmc: Double,
    val colors: Array<String>,
    val colorless: Boolean,
    val monocolored: Boolean,
    val multicolored: Boolean
)
