package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for /cards/search endpoint.
 */
class CardsApiSearchIntegrationTest {

    @Test
    fun search_realCall_simpleQuery_returnsListWithData() =
        runBlocking {
            val api = CardsApi()
            // Use a very common, narrowly scoped query to reduce response size/latency
            val result = api.search(q = "name:\"Lightning Bolt\"")

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty results for query 'name:\"Lightning Bolt\"'")

            // Sanity check that at least some of the first page items look like Bears
            val anyBolt = result.data.take(10).any {
                it.name.lowercase().contains("lightning bolt")
            }
            assertTrue(anyBolt, "Expected at least one Lightning Bolt card in first results page")
        }
}
