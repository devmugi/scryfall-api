@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js.models

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@JsExport
@Suppress("ArrayInDataClass")
data class JsScryfallCardList(
    val objectType: String = "list",
    val hasMore: Boolean = false,
    val nextPage: String? = null,
    val totalCards: Int? = null,
    val data: Array<JsCard> = emptyArray(),
    val warnings: Array<String>? = null,
    val notFound: Array<JsNotFound>? = null,
)

@JsExport
@Suppress("ArrayInDataClass")
data class JsAutocompleteResult(val objectType: String = "catalog", val totalValues: Int, val data: Array<String>,)

@JsExport
@Suppress("ArrayInDataClass")
data class JsCard(
    // Core Card Fields
    val arenaId: Int? = null,
    val id: String,
    val language: String,
    val mtgoId: Int? = null,
    val mtgoFoilId: Int? = null,
    val multiverseIds: Array<Int>? = null,
    val tcgplayerId: Int? = null,
    val tcgplayerEtchedId: Int? = null,
    val cardmarketId: Int? = null,
    val resourceId: String? = null,
    val objectType: String,
    val layout: String,
    val oracleId: String? = null,
    val printsSearchUri: String,
    val rulingsUri: String,
    val scryfallUri: String,
    val uri: String,

    // Gameplay Fields
    val allParts: Array<JsRelatedCard>? = null,
    val cardFaces: Array<JsCardFace>? = null,
    val manaValue: Double = 0.0,
    val colorIdentity: Array<String> = emptyArray(),
    val colorIndicator: Array<String>? = null,
    val colors: Array<String>? = null,
    val defense: String? = null,
    val edhrecRank: Int? = null,
    val gameChanger: Boolean? = null,
    val handModifier: String? = null,
    val keywords: Array<String> = emptyArray(),
    val legalities: JsLegalities = JsLegalities(),
    val lifeModifier: String? = null,
    val loyalty: String? = null,
    val manaCost: String? = null,
    val name: String,
    val oracleText: String? = null,
    val oversized: Boolean = false,
    val pennyRank: Int? = null,
    val power: String? = null,
    val producedMana: Array<String>? = null,
    val reserved: Boolean = false,
    val toughness: String? = null,
    val typeLine: String,

    // Print Fields
    val artist: String? = null,
    val artistIds: Array<String>? = null,
    val attractionLights: Array<Int>? = null,
    val booster: Boolean = false,
    val borderColor: String,
    val cardBackId: String? = null,
    val collectorNumber: String,
    val contentWarning: Boolean? = null,
    val digital: Boolean,
    val finishes: Array<String> = emptyArray(),
    val flavorName: String? = null,
    val flavorText: String? = null,
    val frameEffects: Array<String>? = null,
    val frame: String,
    val fullArt: Boolean,
    val games: Array<String> = emptyArray(),
    val highresImage: Boolean,
    val illustrationId: String? = null,
    val imageStatus: String,
    val imageUris: JsImageUris? = null,
    val prices: JsPrices? = JsPrices(),
    val printedName: String? = null,
    val printedText: String? = null,
    val printedTypeLine: String? = null,
    val promo: Boolean,
    val promoTypes: Array<String>? = null,
    val purchaseUris: dynamic = null,
    val rarity: String,
    val relatedUris: dynamic,
    val releasedAt: String,
    val reprint: Boolean,
    val scryfallSetUri: String,
    val setName: String,
    val setSearchUri: String,
    val setType: String,
    val setUri: String,
    val setCode: String,
    val setId: String,
    val storySpotlight: Boolean,
    val textless: Boolean,
    val variation: Boolean,
    val variationOf: String? = null,
    val securityStamp: String? = null,
    val watermark: String? = null,
    val preview: JsPreview? = null
)

@JsExport
data class JsRelatedCard(
    val id: String,
    val objectType: String,
    val component: String,
    val name: String,
    val typeLine: String,
    val uri: String
)

@JsExport
@Suppress("ArrayInDataClass")
data class JsCardFace(
    val artist: String? = null,
    val manaValue: Double? = null,
    val colors: Array<String>? = null,
    val flavorText: String? = null,
    val illustrationId: String? = null,
    val imageUris: JsImageUris? = null,
    val layout: String? = null,
    val loyalty: String? = null,
    val manaCost: String,
    val name: String,
    val objectType: String,
    val oracleId: String? = null,
    val oracleText: String? = null,
    val power: String? = null,
    val printedName: String? = null,
    val printedText: String? = null,
    val printedTypeLine: String? = null,
    val toughness: String? = null,
    val typeLine: String? = null,
    val watermark: String? = null
)

@JsExport
data class JsLegalities(
    val standard: String? = null,
    val future: String? = null,
    val historic: String? = null,
    val timeless: String? = null,
    val gladiator: String? = null,
    val pioneer: String? = null,
    val explorer: String? = null,
    val modern: String? = null,
    val legacy: String? = null,
    val pauper: String? = null,
    val vintage: String? = null,
    val penny: String? = null,
    val commander: String? = null,
    val oathbreaker: String? = null,
    val brawl: String? = null,
    val historicbrawl: String? = null,
    val alchemy: String? = null,
    val paupercommander: String? = null,
    val duel: String? = null,
    val oldschool: String? = null,
    val premodern: String? = null,
    val predh: String? = null,
    val standardbrawl: String? = null
)

@JsExport
data class JsImageUris(
    val small: String? = null,
    val normal: String? = null,
    val large: String? = null,
    val png: String? = null,
    val artCrop: String? = null,
    val borderCrop: String? = null
)

@JsExport
data class JsPrices(
    val usd: String? = null,
    val usdFoil: String? = null,
    val usdEtched: String? = null,
    val eur: String? = null,
    val eurFoil: String? = null,
    val tix: String? = null
)

@JsExport
data class JsPreview(val source: String? = null, val sourceUri: String? = null, val previewedAt: String? = null)
