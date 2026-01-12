package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class CatalogsApiTest {

    private val minimalCatalogJson = """
        {
          "object": "catalog",
          "uri": "https://api.scryfall.com/catalog/card-names",
          "total_values": 3,
          "data": ["Alpha", "Beta", "Gamma"]
        }
    """.trimIndent()

    @Test
    fun cardNames_returnsCatalog_andPreservesEnvelope() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalCatalogJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CatalogsApi(config = ScryfallConfig(), engine = engine)
            val result = api.cardNames()

            assertEquals("/catalog/card-names", capturedPath)
            assertEquals("catalog", result.objectType)
            assertEquals(3, result.totalValues)
            assertEquals(listOf("Alpha", "Beta", "Gamma"), result.data)
        }

    @Test
    fun creatureTypes_buildsCorrectRequest_andParses() =
        runTest {
            var capturedPath: String? = null

            val engine = MockEngine { request ->
                capturedPath = request.url.encodedPath
                respond(
                    content = minimalCatalogJson,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CatalogsApi(engine = engine)
            val result = api.creatureTypes()

            assertEquals("/catalog/creature-types", capturedPath)
            assertEquals(3, result.totalValues)
            assertTrue(result.data.contains("Alpha"))
        }

    @Test
    fun cardNames_badRequest_mapsToInvalidRequest() {
        runTest {
            val errorJson = """
                {
                  "object": "error",
                  "code": "bad_request",
                  "status": 400,
                  "details": "Invalid parameters."
                }
            """.trimIndent()

            val engine = MockEngine {
                respond(
                    content = errorJson,
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }

            val api = CatalogsApi(engine = engine)

            val ex = assertFailsWith<ScryfallApiException.InvalidRequest> {
                api.cardNames()
            }
            assertEquals(400, ex.error.status)
            assertEquals("Invalid parameters.", ex.error.details)
            assertNotNull(ex.message)
        }
    }
}
