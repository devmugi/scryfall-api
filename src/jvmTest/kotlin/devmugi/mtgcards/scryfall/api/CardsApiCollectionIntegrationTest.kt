package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.models.Identifier
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for /cards/collection endpoint against the real Scryfall API.
 */
class CardsApiCollectionIntegrationTest {

    @Test
    fun collection_withNameIdentifiers_returnsMatchingCards() =
        runBlocking {
            val api = CardsApi()
            val result = api.collection(
                identifiers = listOf(
                    Identifier(name = "Lightning Bolt"),
                    Identifier(name = "Counterspell")
                )
            )

            assertEquals("list", result.objectType)
            assertTrue(result.data.size >= 2, "Expected at least 2 cards")

            val names = result.data.map { it.name.lowercase() }
            assertTrue(names.any { it.contains("lightning bolt") })
            assertTrue(names.any { it.contains("counterspell") })
        }

    @Test
    fun collection_withSetAndCollectorNumber_returnsSpecificPrintings() =
        runBlocking {
            val api = CardsApi()
            val result = api.collection(
                identifiers = listOf(
                    Identifier(set = "m10", collectorNumber = "146"), // Lightning Bolt
                    Identifier(set = "lea", collectorNumber = "232") // Counterspell Alpha
                )
            )

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected at least one card")
        }

    @Test
    fun collection_withMixedIdentifiers_returnsCards() =
        runBlocking {
            val api = CardsApi()
            val result = api.collection(
                identifiers = listOf(
                    Identifier(name = "Black Lotus"),
                    Identifier(set = "m10", collectorNumber = "146"),
                    Identifier(id = "bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd") // Black Lotus Alpha
                )
            )

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected at least one card")
        }
}
