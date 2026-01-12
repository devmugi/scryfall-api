@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.NotFound
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.api.models.Set
import devmugi.mtgcards.scryfall.js.models.JsNotFound
import devmugi.mtgcards.scryfall.js.models.JsScryfallSetList
import devmugi.mtgcards.scryfall.js.models.JsSet

internal fun Set.toJs(): JsSet =
    JsSet(
        objectType = objectType,
        id = id,
        code = code,
        mtgoCode = mtgoCode,
        arenaCode = arenaCode,
        tcgplayerId = tcgplayerId,
        name = name,
        setType = setType,
        releasedAt = releasedAt,
        blockCode = blockCode,
        block = block,
        parentSetCode = parentSetCode,
        cardCount = cardCount,
        printedSize = printedSize,
        digital = digital,
        foilOnly = foilOnly,
        nonfoilOnly = nonfoilOnly,
        scryfallUri = scryfallUri,
        uri = uri,
        iconSvgUri = iconSvgUri,
        searchUri = searchUri,
    )

internal fun NotFound.toJs(): JsNotFound =
    JsNotFound(
        objectType = objectType,
        name = name,
        set = set,
        collectorNumber = collectorNumber,
        type = type
    )

internal fun ScryfallList<Set>.toJs(): JsScryfallSetList =
    JsScryfallSetList(
        objectType = objectType,
        hasMore = hasMore,
        nextPage = nextPage,
        totalCards = totalCards,
        data = data.map { it.toJs() }.toTypedArray(),
        warnings = warnings?.toTypedArray(),
        notFound = notFound?.map { it.toJs() }?.toTypedArray()
    )
