@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.Catalog
import devmugi.mtgcards.scryfall.js.models.JsCatalog

internal fun Catalog.toJs(): JsCatalog =
    JsCatalog(
        objectType = objectType,
        uri = uri,
        totalValues = totalValues,
        data = data.toTypedArray()
    )
