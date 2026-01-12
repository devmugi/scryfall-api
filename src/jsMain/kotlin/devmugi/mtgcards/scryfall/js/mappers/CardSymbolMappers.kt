@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.CardSymbol
import devmugi.mtgcards.scryfall.api.models.ParsedManaCost
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.js.models.JsCardSymbol
import devmugi.mtgcards.scryfall.js.models.JsParsedManaCost
import devmugi.mtgcards.scryfall.js.models.JsScryfallCardSymbolList

internal fun ScryfallList<CardSymbol>.toJs(): JsScryfallCardSymbolList =
    JsScryfallCardSymbolList(
        objectType = objectType,
        hasMore = hasMore,
        nextPage = nextPage,
        data = data.map { it.toJs() }.toTypedArray()
    )

internal fun CardSymbol.toJs(): JsCardSymbol =
    JsCardSymbol(
        objectType = objectType,
        symbol = symbol,
        looseVariant = looseVariant,
        english = english,
        transposable = transposable,
        representsMana = representsMana,
        manaValue = manaValue,
        appearsInManaCosts = appearsInManaCosts,
        funny = funny,
        colors = colors.toTypedArray(),
        hybrid = hybrid,
        phyrexian = phyrexian,
        gathererAlternates = gathererAlternates?.toTypedArray(),
        svgUri = svgUri
    )

internal fun ParsedManaCost.toJs(): JsParsedManaCost =
    JsParsedManaCost(
        objectType = objectType,
        cost = cost,
        cmc = cmc,
        colors = colors.toTypedArray(),
        colorless = colorless,
        monocolored = monocolored,
        multicolored = multicolored
    )
