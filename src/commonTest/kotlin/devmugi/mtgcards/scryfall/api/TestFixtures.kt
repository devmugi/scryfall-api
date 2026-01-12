package devmugi.mtgcards.scryfall.api

import io.ktor.client.engine.mock.*
import io.ktor.http.*

/**
 * Shared test fixtures for CardsApi tests.
 */
object TestFixtures {

    /**
     * Minimal valid Card JSON containing all required fields.
     */
    val MINIMAL_CARD_JSON = """
        {
          "id": "id-123",
          "lang": "en",
          "object": "card",
          "layout": "normal",
          "prints_search_uri": "https://api.scryfall.com/cards/search?q=e%3Atst",
          "rulings_uri": "https://api.scryfall.com/cards/id-123/rulings",
          "scryfall_uri": "https://scryfall.com/card/tst/123/grizzly-bears",
          "uri": "https://api.scryfall.com/cards/id-123",
          "mana_cost": "",
          "name": "Grizzly Bears",
          "type_line": "Creature — Bear",
          "border_color": "black",
          "card_back_id": "00000000-0000-0000-0000-000000000000",
          "collector_number": "123",
          "digital": false,
          "frame": "2015",
          "full_art": false,
          "highres_image": true,
          "image_status": "highres_scan",
          "promo": false,
          "rarity": "common",
          "related_uris": {},
          "released_at": "1995-01-01",
          "reprint": false,
          "scryfall_set_uri": "https://scryfall.com/sets/tst",
          "set_name": "Test Set",
          "set_search_uri": "https://api.scryfall.com/cards/search?q=e%3Atst",
          "set_type": "core",
          "set_uri": "https://api.scryfall.com/sets/00000000-0000-0000-0000-000000000000",
          "set": "tst",
          "set_id": "11111111-1111-1111-1111-111111111111",
          "story_spotlight": false,
          "textless": false,
          "variation": false
        }
    """.trimIndent()

    /**
     * Sample autocomplete response JSON.
     */
    val AUTOCOMPLETE_JSON = """
        {
          "object": "catalog",
          "total_values": 2,
          "data": ["Lightning Bolt", "Llanowar Elves"]
        }
    """.trimIndent()

    /**
     * Wraps card JSON in a list payload.
     */
    fun listPayload(cardJson: String = MINIMAL_CARD_JSON, hasMore: Boolean = false, totalCards: Int = 1): String =
        """
        {
          "object": "list",
          "has_more": $hasMore,
          "total_cards": $totalCards,
          "data": [
            $cardJson
          ]
        }
        """.trimIndent()

    /**
     * Standard JSON response headers.
     */
    fun jsonHeaders(): Headers =
        headersOf(
            HttpHeaders.ContentType,
            ContentType.Application.Json.toString()
        )
}

/**
 * Data class to capture HTTP request details for verification.
 */
data class CapturedRequest(val path: String, val params: Map<String, String?>)

/**
 * Creates a MockEngine that captures request details and returns a JSON response.
 *
 * @param responseBody JSON response body to return
 * @param onRequest Callback invoked with captured request details
 */
fun mockJsonEngine(responseBody: String, onRequest: (CapturedRequest) -> Unit = {}): MockEngine =
    MockEngine { request ->
        val captured = CapturedRequest(
            path = request.url.encodedPath,
            params = request.url.parameters.names().associateWith { request.url.parameters[it] }
        )
        onRequest(captured)
        respond(
            content = responseBody,
            headers = TestFixtures.jsonHeaders()
        )
    }

/**
 * Creates a MockEngine for card responses.
 */
fun mockCardEngine(onRequest: (CapturedRequest) -> Unit = {}): MockEngine =
    mockJsonEngine(TestFixtures.MINIMAL_CARD_JSON, onRequest)

/**
 * Creates a MockEngine for list responses.
 */
fun mockListEngine(
    hasMore: Boolean = false,
    totalCards: Int = 1,
    onRequest: (CapturedRequest) -> Unit = {}
): MockEngine = mockJsonEngine(TestFixtures.listPayload(hasMore = hasMore, totalCards = totalCards), onRequest)

/**
 * Creates a MockEngine for autocomplete responses.
 */
fun mockAutocompleteEngine(onRequest: (CapturedRequest) -> Unit = {}): MockEngine =
    mockJsonEngine(TestFixtures.AUTOCOMPLETE_JSON, onRequest)
