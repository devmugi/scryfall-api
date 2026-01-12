package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class SetsApiTest {

    private val minimalSetJson = """
        {
          "object": "set",
          "id": "set-uuid-123",
          "code": "neo",
          "name": "Kamigawa: Neon Dynasty",
          "set_type": "expansion",
          "released_at": "2022-02-18",
          "uri": "https://api.scryfall.com/sets/set-uuid-123",
          "scryfall_uri": "https://scryfall.com/sets/neo",
          "search_uri": "https://api.scryfall.com/cards/search?q=e%3Aneo",
          "card_count": 302,
          "digital": false,
          "nonfoil_only": false,
          "foil_only": false,
          "icon_svg_uri": "https://svgs.scryfall.io/sets/neo.svg"
        }
    """.trimIndent()

    private fun listPayload(setJson: String, hasMore: Boolean = false, total: Int = 1): String =
        """
        {
          "object": "list",
          "has_more": $hasMore,
          "total_cards": $total,
          "data": [
            $setJson
          ]
        }
        """.trimIndent()

    @Test
    fun all_returnsListOfSets_andPreservesEnvelope() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalSetJson, hasMore = true, total = 456),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = SetsApi(config = ScryfallConfig(), engine = engine)
            val result = api.all()

            assertEquals("/sets", capturedPath)
            assertEquals("list", result.objectType)
            assertTrue(result.hasMore)
            assertEquals(456, result.totalCards)
            assertEquals(1, result.data.size)
            assertEquals("neo", result.data.first().code)
            assertEquals("Kamigawa: Neon Dynasty", result.data.first().name)
        }

    @Test
    fun byCode_buildsCorrectRequest_andParsesSet() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalSetJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = SetsApi(engine = engine)
            val set = api.byCode("neo")

            assertEquals("/sets/neo", capturedPath)
            assertEquals("set-uuid-123", set.id)
            assertEquals("neo", set.code)
            assertEquals("Kamigawa: Neon Dynasty", set.name)
            assertEquals("expansion", set.setType)
        }

    @Test
    fun byId_buildsCorrectRequest_andParsesSet() =
        runTest {
            val id = "set-uuid-123"
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalSetJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = SetsApi(engine = engine)
            val set = api.byId(id)

            assertEquals("/sets/$id", capturedPath)
            assertEquals(id, set.id)
            assertEquals("neo", set.code)
        }

    @Test
    fun byCode_notFound_mapsToScryfallNotFound() {
        runTest {
            val errorJson = """
            {
              "object": "error",
              "code": "not_found",
              "status": 404,
              "details": "Set not found."
            }
            """.trimIndent()

            val engine = MockEngine {
                respond(
                    content = errorJson,
                    status = HttpStatusCode.NotFound,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = SetsApi(engine = engine)

            val ex = assertFailsWith<ScryfallApiException.NotFound> {
                api.byCode("nope")
            }
            assertEquals(404, ex.error.status)
            assertEquals("Set not found.", ex.error.details)
            assertNotNull(ex.message)
        }
    }
}
