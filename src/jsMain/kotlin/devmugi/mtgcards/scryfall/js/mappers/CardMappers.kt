@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.mappers

import devmugi.mtgcards.scryfall.api.models.AutocompleteResult
import devmugi.mtgcards.scryfall.api.models.Card
import devmugi.mtgcards.scryfall.api.models.CardFace
import devmugi.mtgcards.scryfall.api.models.Identifier
import devmugi.mtgcards.scryfall.api.models.ImageUris
import devmugi.mtgcards.scryfall.api.models.Legalities
import devmugi.mtgcards.scryfall.api.models.Preview
import devmugi.mtgcards.scryfall.api.models.Prices
import devmugi.mtgcards.scryfall.api.models.RelatedCard
import devmugi.mtgcards.scryfall.api.models.ScryfallList
import devmugi.mtgcards.scryfall.js.JsIdentifier
import devmugi.mtgcards.scryfall.js.models.JsAutocompleteResult
import devmugi.mtgcards.scryfall.js.models.JsCard
import devmugi.mtgcards.scryfall.js.models.JsCardFace
import devmugi.mtgcards.scryfall.js.models.JsImageUris
import devmugi.mtgcards.scryfall.js.models.JsLegalities
import devmugi.mtgcards.scryfall.js.models.JsPreview
import devmugi.mtgcards.scryfall.js.models.JsPrices
import devmugi.mtgcards.scryfall.js.models.JsRelatedCard
import devmugi.mtgcards.scryfall.js.models.JsScryfallCardList

internal fun ScryfallList<Card>.toJs(): JsScryfallCardList =
    JsScryfallCardList(
        objectType = objectType,
        hasMore = hasMore,
        nextPage = nextPage,
        totalCards = totalCards,
        data = data.map { it.toJs() }.toTypedArray(),
        warnings = warnings?.toTypedArray(),
        notFound = notFound?.map { it.toJs() }?.toTypedArray()
    )

internal fun AutocompleteResult.toJs(): JsAutocompleteResult =
    JsAutocompleteResult(
        objectType = objectType,
        totalValues = totalValues,
        data = data.toTypedArray()
    )

internal fun Card.toJs(): JsCard {
    val jsAllParts = allParts?.map { it.toJs() }?.toTypedArray()
    val jsCardFaces = cardFaces?.map { it.toJs() }?.toTypedArray()
    val jsLegalities = legalities.toJs()
    val jsImageUris = imageUris?.toJs()
    val jsPrices = prices?.toJs()
    val jsPreview = preview?.toJs()
    val jsPurchaseUris = purchaseUris?.toJsObject()
    val jsRelatedUris = relatedUris.toJsObject()

    return JsCard(
        arenaId = arenaId, id = id, language = language, mtgoId = mtgoId, mtgoFoilId = mtgoFoilId,
        multiverseIds = multiverseIds?.toTypedArray(),
        tcgplayerId = tcgplayerId, tcgplayerEtchedId = tcgplayerEtchedId,
        cardmarketId = cardmarketId, resourceId = resourceId, objectType = objectType,
        layout = layout, oracleId = oracleId, printsSearchUri = printsSearchUri,
        rulingsUri = rulingsUri, scryfallUri = scryfallUri, uri = uri,
        allParts = jsAllParts, cardFaces = jsCardFaces, manaValue = manaValue,
        colorIdentity = colorIdentity.toTypedArray(),
        colorIndicator = colorIndicator?.toTypedArray(),
        colors = colors?.toTypedArray(), defense = defense, edhrecRank = edhrecRank,
        gameChanger = gameChanger, handModifier = handModifier,
        keywords = keywords.toTypedArray(), legalities = jsLegalities,
        lifeModifier = lifeModifier, loyalty = loyalty, manaCost = manaCost, name = name,
        oracleText = oracleText, oversized = oversized, pennyRank = pennyRank, power = power,
        producedMana = producedMana?.toTypedArray(), reserved = reserved, toughness = toughness,
        typeLine = typeLine, artist = artist, artistIds = artistIds?.toTypedArray(),
        attractionLights = attractionLights?.toTypedArray(), booster = booster,
        borderColor = borderColor, cardBackId = cardBackId, collectorNumber = collectorNumber,
        contentWarning = contentWarning, digital = digital, finishes = finishes.toTypedArray(),
        flavorName = flavorName, flavorText = flavorText,
        frameEffects = frameEffects?.toTypedArray(), frame = frame, fullArt = fullArt,
        games = games.toTypedArray(), highresImage = highresImage,
        illustrationId = illustrationId, imageStatus = imageStatus,
        imageUris = jsImageUris, prices = jsPrices, printedName = printedName,
        printedText = printedText, printedTypeLine = printedTypeLine, promo = promo,
        promoTypes = promoTypes?.toTypedArray(), purchaseUris = jsPurchaseUris, rarity = rarity,
        relatedUris = jsRelatedUris, releasedAt = releasedAt, reprint = reprint,
        scryfallSetUri = scryfallSetUri, setName = setName, setSearchUri = setSearchUri,
        setType = setType, setUri = setUri, setCode = setCode, setId = setId,
        storySpotlight = storySpotlight, textless = textless, variation = variation,
        variationOf = variationOf, securityStamp = securityStamp, watermark = watermark,
        preview = jsPreview
    )
}

internal fun RelatedCard.toJs(): JsRelatedCard =
    JsRelatedCard(
        id = id,
        objectType = objectType,
        component = component,
        name = name,
        typeLine = typeLine,
        uri = uri
    )

internal fun CardFace.toJs(): JsCardFace =
    JsCardFace(
        artist = artist,
        manaValue = manaValue,
        colors = colors?.toTypedArray(),
        flavorText = flavorText,
        illustrationId = illustrationId,
        imageUris = imageUris?.toJs(),
        layout = layout,
        loyalty = loyalty,
        manaCost = manaCost,
        name = name,
        objectType = objectType,
        oracleId = oracleId,
        oracleText = oracleText,
        power = power,
        printedName = printedName,
        printedText = printedText,
        printedTypeLine = printedTypeLine,
        toughness = toughness,
        typeLine = typeLine,
        watermark = watermark
    )

internal fun Legalities.toJs(): JsLegalities =
    JsLegalities(
        standard = standard,
        future = future,
        historic = historic,
        timeless = timeless,
        gladiator = gladiator,
        pioneer = pioneer,
        explorer = explorer,
        modern = modern,
        legacy = legacy,
        pauper = pauper,
        vintage = vintage,
        penny = penny,
        commander = commander,
        oathbreaker = oathbreaker,
        brawl = brawl,
        historicbrawl = historicBrawl,
        alchemy = alchemy,
        paupercommander = pauperCommander,
        duel = duel,
        oldschool = oldschool,
        premodern = premodern,
        predh = predh,
        standardbrawl = standardBrawl
    )

internal fun ImageUris.toJs(): JsImageUris =
    JsImageUris(
        small = small,
        normal = normal,
        large = large,
        png = png,
        artCrop = artCrop,
        borderCrop = borderCrop
    )

internal fun Prices.toJs(): JsPrices =
    JsPrices(
        usd = usd,
        usdFoil = usdFoil,
        usdEtched = usdEtched,
        eur = eur,
        eurFoil = eurFoil,
        tix = tix
    )

internal fun Preview.toJs(): JsPreview =
    JsPreview(
        source = source,
        sourceUri = sourceUri,
        previewedAt = previewedAt
    )

internal fun Map<String, String>.toJsObject(): dynamic {
    val obj = js("{}")
    forEach { (k, v) -> obj[k] = v }
    return obj
}

internal fun JsIdentifier.toIdentifier(): Identifier =
    Identifier(
        id = id,
        oracleId = oracleId,
        illustrationId = illustrationId,
        multiverseId = multiverseId,
        mtgoId = mtgoId,
        arenaId = arenaId,
        set = set,
        collectorNumber = collectorNumber,
        name = name
    )
