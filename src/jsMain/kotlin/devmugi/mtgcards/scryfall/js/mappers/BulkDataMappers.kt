@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.BulkData
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.js.models.JsBulkData
import devmugi.mtgcards.scryfall.js.models.JsScryfallBulkDataList

internal fun ScryfallList<BulkData>.toJs(): JsScryfallBulkDataList =
    JsScryfallBulkDataList(
        objectType = objectType,
        hasMore = hasMore,
        nextPage = nextPage,
        data = data.map { it.toJs() }.toTypedArray()
    )

internal fun BulkData.toJs(): JsBulkData =
    JsBulkData(
        objectType = objectType,
        id = id,
        uri = uri,
        type = type,
        name = name,
        description = description,
        downloadUri = downloadUri,
        updatedAt = updatedAt,
        size = size.toInt(),
        contentType = contentType,
        contentEncoding = contentEncoding
    )
