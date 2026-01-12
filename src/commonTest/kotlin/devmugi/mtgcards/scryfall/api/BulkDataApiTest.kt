package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class BulkDataApiTest {

    private val minimalBulkJson = """
        {
          "object": "bulk_data",
          "id": "bulk-uuid-123",
          "uri": "https://api.scryfall.com/bulk-data/bulk-uuid-123",
          "type": "oracle-cards",
          "name": "Oracle Cards",
          "description": "A JSON file containing all Oracle card data",
          "download_uri": "https://data.scryfall.io/oracle-cards.json",
          "updated_at": "2024-01-01T00:00:00Z",
          "size": 123456,
          "content_type": "application/json",
          "content_encoding": "gzip"
        }
    """.trimIndent()

    private fun listPayload(itemJson: String, hasMore: Boolean = false, total: Int = 1): String =
        """
        {
          "object": "list",
          "has_more": $hasMore,
          "total_cards": $total,
          "data": [
            $itemJson
          ]
        }
        """.trimIndent()

    @Test
    fun all_buildsCorrectRequest_andParsesList() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = listPayload(minimalBulkJson, hasMore = true, total = 3),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = BulkDataApi(config = ScryfallConfig(), engine = engine)
            val result = api.all()

            assertEquals("/bulk-data", capturedPath)
            assertEquals("list", result.objectType)
            assertTrue(result.hasMore)
            assertEquals(3, result.totalCards)
            assertEquals(1, result.data.size)
            assertEquals("oracle-cards", result.data.first().type)
            assertEquals("Oracle Cards", result.data.first().name)
        }

    @Test
    fun byId_buildsCorrectRequest_andParsesBulkData() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalBulkJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = BulkDataApi(engine = engine)
            val id = "bulk-uuid-123"
            val item = api.byId(id)

            assertEquals("/bulk-data/$id", capturedPath)
            assertEquals(id, item.id)
            assertEquals("oracle-cards", item.type)
            assertEquals("Oracle Cards", item.name)
        }

    @Test
    fun byType_buildsCorrectRequest_andParsesBulkData() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalBulkJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = BulkDataApi(engine = engine)
            val type = "oracle-cards"
            val item = api.byType(type)

            assertEquals("/bulk-data/$type", capturedPath)
            assertEquals(type, item.type)
            assertEquals("Oracle Cards", item.name)
            assertEquals("bulk-uuid-123", item.id)
        }

    @Test
    fun byType_notFound_mapsToScryfallNotFound() {
        runTest {
            val errorJson = """
                {
                  "object": "error",
                  "code": "not_found",
                  "status": 404,
                  "details": "Bulk data type not found."
                }
            """.trimIndent()

            val engine = MockEngine {
                respond(
                    content = errorJson,
                    status = HttpStatusCode.NotFound,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = BulkDataApi(engine = engine)

            val ex = assertFailsWith<ScryfallApiException.NotFound> {
                api.byType("does-not-exist")
            }
            assertEquals(404, ex.error.status)
            assertEquals("Bulk data type not found.", ex.error.details)
            assertNotNull(ex.message)
        }
    }
}
