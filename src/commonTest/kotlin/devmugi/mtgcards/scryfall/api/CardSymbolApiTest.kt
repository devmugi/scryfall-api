package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class CardSymbolApiTest {

    private val minimalSymbolJson = """
        {
          "object": "card_symbol",
          "symbol": "{G}",
          "english": "green mana",
          "transposable": false,
          "represents_mana": true,
          "appears_in_mana_costs": true,
          "funny": false,
          "colors": ["G"],
          "hybrid": false,
          "phyrexian": false
        }
    """.trimIndent()

    private fun listPayload(symbolJson: String, hasMore: Boolean = false, total: Int = 1): String =
        """
        {
          "object": "list",
          "has_more": $hasMore,
          "total_cards": $total,
          "data": [
            $symbolJson
          ]
        }
        """.trimIndent()

    @Test
    fun all_returnsListOfSymbols_andPreservesEnvelope() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalSymbolJson, hasMore = true, total = 999),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CardSymbolApi(config = ScryfallConfig(), engine = engine)
            val result = api.all()

            assertEquals("/symbology", capturedPath)
            assertEquals("list", result.objectType)
            assertTrue(result.hasMore)
            assertEquals(999, result.totalCards)
            assertEquals(1, result.data.size)
            assertEquals("{G}", result.data.first().symbol)
        }

    @Test
    fun parseMana_buildsCorrectRequest_andParsesResult() =
        runTest {
            var capturedPath: String? = null
            var capturedCost: String? = null

            val responseJson = """
            {
              "cost": "{2}{G}{U}",
              "cmc": 4.0,
              "colors": ["G", "U"],
              "colorless": false,
              "monocolored": false,
              "multicolored": true
            }
            """.trimIndent()

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                capturedCost = request.url.parameters["cost"]
                respond(
                    content = responseJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CardSymbolApi(engine = engine)
            val parsed = api.parseMana("{2}{G}{U}")

            assertEquals("/symbology/parse-mana", capturedPath)
            assertEquals("{2}{G}{U}", capturedCost)
            assertEquals(4.0, parsed.cmc)
            assertEquals(listOf("G", "U"), parsed.colors)
            assertTrue(parsed.multicolored)
        }

    @Test
    fun parseMana_badRequest_mapsToInvalidRequest() {
        runTest {
            val errorJson = """
                {
                  "object": "error",
                  "code": "bad_request",
                  "status": 400,
                  "details": "Could not parse mana cost."
                }
            """.trimIndent()

            val engine = MockEngine {
                respond(
                    content = errorJson,
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CardSymbolApi(engine = engine)

            val ex = assertFailsWith<ScryfallApiException.InvalidRequest> {
                api.parseMana("{not a cost}")
            }
            assertEquals(400, ex.error.status)
            assertEquals("Could not parse mana cost.", ex.error.details)
            assertNotNull(ex.message)
        }
    }
}
