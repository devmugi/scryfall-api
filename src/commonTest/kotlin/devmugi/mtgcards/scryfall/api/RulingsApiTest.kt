package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class RulingsApiTest {

    private val minimalRulingJson = """
        {
          "object": "ruling",
          "oracle_id": "oracle-uuid-123",
          "source": "wotc",
          "published_at": "2020-01-01",
          "comment": "Sample ruling text."
        }
    """.trimIndent()

    private fun listPayload(rulingJson: String, hasMore: Boolean = false): String =
        """
        {
          "object": "list",
          "has_more": $hasMore,
          "data": [
            $rulingJson
          ]
        }
        """.trimIndent()

    @Test
    fun byCardId_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalRulingJson, hasMore = true),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(config = ScryfallConfig(), engine = engine)
            val result = api.byCardId("card-uuid-123")

            assertEquals("/cards/card-uuid-123/rulings", capturedPath)
            assertEquals("list", result.objectType)
            assertTrue(result.hasMore)
            assertEquals(1, result.data.size)
            assertEquals("wotc", result.data.first().source)
            assertEquals("Sample ruling text.", result.data.first().comment)
        }

    @Test
    fun byMultiverseId_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalRulingJson),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(engine = engine)
            val result = api.byMultiverseId(12345)

            assertEquals("/cards/multiverse/12345/rulings", capturedPath)
            assertEquals(1, result.data.size)
        }

    @Test
    fun byMtgoId_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalRulingJson),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(engine = engine)
            val result = api.byMtgoId(777)

            assertEquals("/cards/mtgo/777/rulings", capturedPath)
            assertEquals(1, result.data.size)
        }

    @Test
    fun byArenaId_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalRulingJson),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(engine = engine)
            val result = api.byArenaId(999)

            assertEquals("/cards/arena/999/rulings", capturedPath)
            assertEquals(1, result.data.size)
        }

    @Test
    fun byCollectorId_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalRulingJson),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(engine = engine)
            val result = api.byCollectorId("lea", "233")

            assertEquals("/cards/lea/233/rulings", capturedPath)
            assertEquals(1, result.data.size)
        }

    @Test
    fun byCardId_notFound_mapsToScryfallNotFound() {
        runTest {
            val errorJson = """
                {
                  "object": "error",
                  "code": "not_found",
                  "status": 404,
                  "details": "Card not found."
                }
            """.trimIndent()

            val engine = MockEngine {
                respond(
                    content = errorJson,
                    status = HttpStatusCode.NotFound,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = RulingsApi(engine = engine)

            val ex = assertFailsWith<ScryfallApiException.NotFound> {
                api.byCardId("nope")
            }
            assertEquals(404, ex.error.status)
            assertEquals("Card not found.", ex.error.details)
            assertNotNull(ex.message)
        }
    }
}
