package devmugi.mtgcards.scryfall.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    // Core Card Fields
    /**
     * This card's Arena ID, if any. Many cards are not supported on Arena and omit this value.
     */
    @SerialName("arena_id")
    val arenaId: Int? = null,

    /**
     * A unique ID for this card in Scryfall's database.
     */
    @SerialName("id")
    val id: String,

    /**
     * The language code for this printing.
     */
    @SerialName("lang")
    val language: String,

    /**
     * This card's Magic Online ID (also called the Catalog ID), if any.
     */
    @SerialName("mtgo_id")
    val mtgoId: Int? = null,

    /**
     * This card's foil Magic Online ID (also called the Catalog ID), if any.
     */
    @SerialName("mtgo_foil_id")
    val mtgoFoilId: Int? = null,

    /**
     * This card's multiverse IDs on Gatherer, if any.
     */
    @SerialName("multiverse_ids")
    val multiverseIds: List<Int>? = null,

    /**
     * This card's ID on TCGplayer's API.
     */
    @SerialName("tcgplayer_id")
    val tcgplayerId: Int? = null,

    /**
     * This card's ID on TCGplayer's API for its etched version, if applicable.
     */
    @SerialName("tcgplayer_etched_id")
    val tcgplayerEtchedId: Int? = null,

    /**
     * This card's ID on Cardmarket's API (idProduct).
     */
    @SerialName("cardmarket_id")
    val cardmarketId: Int? = null,

    /**
     * This card's resource ID on Scryfall, if any.
     */
    @SerialName("resource_id")
    val resourceId: String? = null,

    /**
     * Content type for this object, always "card".
     */
    @SerialName("object")
    val objectType: String,

    /**
     * Code for this card's layout (normal, transform, split, etc.).
     */
    @SerialName("layout")
    val layout: String,

    /**
     * Oracle ID that stays consistent across reprints of the same card.
     */
    @SerialName("oracle_id")
    val oracleId: String? = null,

    /**
     * Link to start paginating all prints for this card.
     */
    @SerialName("prints_search_uri")
    val printsSearchUri: String,

    /**
     * Link to this card's rulings list on the Scryfall API.
     */
    @SerialName("rulings_uri")
    val rulingsUri: String,

    /**
     * Link to this card's permapage on Scryfall's website.
     */
    @SerialName("scryfall_uri")
    val scryfallUri: String,

    /**
     * Link to this card object on the Scryfall API.
     */
    @SerialName("uri")
    val uri: String,

    // Gameplay Fields
    /**
     * Related card references, such as meld parts or combo pieces.
     */
    @SerialName("all_parts")
    val allParts: List<RelatedCard>? = null,

    /**
     * Faces for multiface cards. Null for single-face cards.
     */
    @SerialName("card_faces")
    val cardFaces: List<CardFace>? = null,

    /**
     * This card's mana value.
     */
    @SerialName("cmc")
    val manaValue: Double = 0.0,

    /**
     * Colors that appear in this card's color identity.
     */
    @SerialName("color_identity")
    val colorIdentity: List<String> = emptyList(),

    /**
     * Colors in this card's color indicator, if any.
     */
    @SerialName("color_indicator")
    val colorIndicator: List<String>? = null,

    /**
     * This card's colors, if defined on the overall card.
     */
    @SerialName("colors")
    val colors: List<String>? = null,

    /**
     * Battle defense value for this card, if any.
     */
    @SerialName("defense")
    val defense: String? = null,

    /**
     * Overall rank/popularity on EDHREC, when available.
     */
    @SerialName("edhrec_rank")
    val edhrecRank: Int? = null,

    /**
     * True if this card appears on the Commander Game Changer list.
     */
    @SerialName("game_changer")
    val gameChanger: Boolean? = null,

    /**
     * Hand modifier for Vanguard cards (delta value), if any.
     */
    @SerialName("hand_modifier")
    val handModifier: String? = null,

    /**
     * Keywords this card uses, such as "Flying".
     */
    @SerialName("keywords")
    val keywords: List<String> = emptyList(),

    /**
     * Legalities across supported play formats.
     */
    @SerialName("legalities")
    val legalities: Legalities = Legalities(),

    /**
     * Life modifier for Vanguard cards (delta value), if any.
     */
    @SerialName("life_modifier")
    val lifeModifier: String? = null,

    /**
     * Loyalty, if any. Some cards use non-numeric values like "X".
     */
    @SerialName("loyalty")
    val loyalty: String? = null,

    /**
     * Mana cost for this card. An empty string means no cost.
     */
    @SerialName("mana_cost")
    val manaCost: String? = null,

    /**
     * This card's name.
     */
    @SerialName("name")
    val name: String,

    /**
     * Oracle text for this card, if any.
     */
    @SerialName("oracle_text")
    val oracleText: String? = null,

    /**
     * True if this card is oversized.
     */
    @SerialName("oversized")
    val oversized: Boolean = false,

    /**
     * Penny Dreadful rank, when provided.
     */
    @SerialName("penny_rank")
    val pennyRank: Int? = null,

    /**
     * This card's power, if any. Can contain non-numeric values such as "*".
     */
    @SerialName("power")
    val power: String? = null,

    /**
     * Colors of mana that this card could produce.
     */
    @SerialName("produced_mana")
    val producedMana: List<String>? = null,

    /**
     * True if this card is on the Reserved List.
     */
    @SerialName("reserved")
    val reserved: Boolean = false,

    /**
     * This card's toughness, if any. Can contain non-numeric values such as "*".
     */
    @SerialName("toughness")
    val toughness: String? = null,

    /**
     * The type line for this card.
     */
    @SerialName("type_line")
    val typeLine: String,

    // Print Fields
    /**
     * Illustrator name, when known.
     */
    @SerialName("artist")
    val artist: String? = null,

    /**
     * IDs of the artists that illustrated this card.
     */
    @SerialName("artist_ids")
    val artistIds: List<String>? = null,

    /**
     * Lit Unfinity attraction lights on this card, if any.
     */
    @SerialName("attraction_lights")
    val attractionLights: List<Int>? = null,

    /**
     * True if this card is found in boosters.
     */
    @SerialName("booster")
    val booster: Boolean = false,

    /**
     * Border color value (black, white, borderless, yellow, silver, or gold).
     */
    @SerialName("border_color")
    val borderColor: String,

    /**
     * Scryfall ID for the card back design present on this card.
     * May be null for some cards.
     */
    @SerialName("card_back_id")
    val cardBackId: String? = null,

    /**
     * Collector number for this printing. May include non-numeric characters.
     */
    @SerialName("collector_number")
    val collectorNumber: String,

    /**
     * True if downstream consumers should avoid using this print.
     */
    @SerialName("content_warning")
    val contentWarning: Boolean? = null,

    /**
     * True if this card was released only digitally.
     */
    @SerialName("digital")
    val digital: Boolean,

    /**
     * Available finishes for this print (foil, nonfoil, etched).
     */
    @SerialName("finishes")
    val finishes: List<String> = emptyList(),

    /**
     * Flavor name printed on this card, if any.
     */
    @SerialName("flavor_name")
    val flavorName: String? = null,

    /**
     * Flavor text printed on this card, if any.
     */
    @SerialName("flavor_text")
    val flavorText: String? = null,

    /**
     * Frame effects that apply to this card, if any.
     */
    @SerialName("frame_effects")
    val frameEffects: List<String>? = null,

    /**
     * Frame layout code for this card.
     */
    @SerialName("frame")
    val frame: String,

    /**
     * True if the artwork is larger than normal (full art).
     */
    @SerialName("full_art")
    val fullArt: Boolean,

    /**
     * Games that this print is available in (paper, arena, mtgo).
     */
    @SerialName("games")
    val games: List<String> = emptyList(),

    /**
     * True if the card imagery is high resolution.
     */
    @SerialName("highres_image")
    val highresImage: Boolean,

    /**
     * Identifier for the artwork that stays consistent across reprints, when known.
     */
    @SerialName("illustration_id")
    val illustrationId: String? = null,

    /**
     * Computer-readable indicator for the state of this card's image (missing, placeholder, lowres, highres_scan).
     */
    @SerialName("image_status")
    val imageStatus: String,

    /**
     * Available imagery for this card. Present on single-faced cards.
     */
    @SerialName("image_uris")
    val imageUris: ImageUris? = null,

    /**
     * Object containing daily price information in multiple currencies (strings).
     */
    @SerialName("prices")
    val prices: Prices? = Prices(),

    /**
     * Localized name printed on this card, if any.
     */
    @SerialName("printed_name")
    val printedName: String? = null,

    /**
     * Localized rules text printed on this card, if any.
     */
    @SerialName("printed_text")
    val printedText: String? = null,

    /**
     * Localized type line printed on this card, if any.
     */
    @SerialName("printed_type_line")
    val printedTypeLine: String? = null,

    /**
     * True if this printing is promotional.
     */
    @SerialName("promo")
    val promo: Boolean,

    /**
     * Categories of promo this card belongs to, if any.
     */
    @SerialName("promo_types")
    val promoTypes: List<String>? = null,

    /**
     * URIs for this card on major marketplaces. Null if unpurchaseable.
     */
    @SerialName("purchase_uris")
    val purchaseUris: Map<String, String>? = null,

    /**
     * Card rarity (common, uncommon, rare, special, mythic, or bonus).
     */
    @SerialName("rarity")
    val rarity: String,

    /**
     * URIs to this card on other Magic resources.
     */
    @SerialName("related_uris")
    val relatedUris: Map<String, String>,

    /**
     * Date this card was first released (YYYY-MM-DD).
     */
    @SerialName("released_at")
    val releasedAt: String,

    /**
     * True if this card is a reprint.
     */
    @SerialName("reprint")
    val reprint: Boolean,

    /**
     * Link to this card's set on Scryfall's website.
     */
    @SerialName("scryfall_set_uri")
    val scryfallSetUri: String,

    /**
     * Full set name for this printing.
     */
    @SerialName("set_name")
    val setName: String,

    /**
     * Link to paginate this card's set on the Scryfall API.
     */
    @SerialName("set_search_uri")
    val setSearchUri: String,

    /**
     * Type of set that contains this printing.
     */
    @SerialName("set_type")
    val setType: String,

    /**
     * Link to this card's set object on the Scryfall API.
     */
    @SerialName("set_uri")
    val setUri: String,

    /**
     * Abbreviated set code.
     */
    @SerialName("set")
    val setCode: String,

    /**
     * UUID of this card's set object.
     */
    @SerialName("set_id")
    val setId: String,

    /**
     * True if this card is a Story Spotlight.
     */
    @SerialName("story_spotlight")
    val storySpotlight: Boolean,

    /**
     * True if the card is printed without rules text.
     */
    @SerialName("textless")
    val textless: Boolean,

    /**
     * True if this printing is marked as a variation of another card.
     */
    @SerialName("variation")
    val variation: Boolean,

    /**
     * Printing ID that this card is a variation of, if applicable.
     */
    @SerialName("variation_of")
    val variationOf: String? = null,

    /**
     * Security stamp on this card (oval, triangle, acorn, circle, arena, or heart).
     */
    @SerialName("security_stamp")
    val securityStamp: String? = null,

    /**
     * Watermark printed on this card, if any.
     */
    @SerialName("watermark")
    val watermark: String? = null,

    /**
     * Preview metadata for this printing.
     */
    @SerialName("preview")
    val preview: Preview? = null
)
