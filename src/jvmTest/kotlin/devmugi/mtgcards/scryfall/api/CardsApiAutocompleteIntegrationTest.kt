package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for autocomplete endpoint.
 */
class CardsApiAutocompleteIntegrationTest {

    @Test
    fun autocomplete_realCall_returnsSomeResultsForTwoChars() =
        runBlocking {
            val api = CardsApi()
            val result = api.autocomplete(q = "li")

            assertEquals("catalog", result.objectType)
            assertTrue(result.totalValues >= 0)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty suggestions for 'li'")
        }

    @Test
    fun autocomplete_realCall_returnsEmptyForSingleChar() =
        runBlocking {
            val api = CardsApi()
            val result = api.autocomplete(q = "x")

            assertEquals("catalog", result.objectType)
            assertTrue(result.totalValues >= 0)
            assertEquals(0, result.data.size, "Expected empty suggestions for single-character query")
        }
}
