@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.Ruling
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.js.models.JsRuling
import devmugi.mtgcards.scryfall.js.models.JsScryfallRulingList

internal fun ScryfallList<Ruling>.toJs(): JsScryfallRulingList =
    JsScryfallRulingList(
        objectType = objectType,
        hasMore = hasMore,
        nextPage = nextPage,
        data = data.map { it.toJs() }.toTypedArray()
    )

internal fun Ruling.toJs(): JsRuling =
    JsRuling(
        objectType = objectType,
        oracleId = oracleId,
        source = source,
        publishedAt = publishedAt,
        comment = comment
    )
