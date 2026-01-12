package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for /cards/named endpoint against the real Scryfall API.
 */
class CardsApiNamedIntegrationTest {

    @Test
    fun namedExact_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            val card = api.namedExact(name = "Lightning Bolt")

            assertEquals("card", card.objectType)
            assertTrue(card.name.lowercase().contains("lightning bolt"))
        }

    @Test
    fun namedExact_withSet_returnsSpecificPrinting() =
        runBlocking {
            val api = CardsApi()
            val card = api.namedExact(name = "Lightning Bolt", set = "m10")

            assertEquals("card", card.objectType)
            assertTrue(card.name.lowercase().contains("lightning bolt"))
            assertEquals("m10", card.setCode)
        }

    @Test
    fun namedFuzzy_realCall_findsCardWithTypo() =
        runBlocking {
            val api = CardsApi()
            // Common misspelling (lightening vs lightning)
            val card = api.namedFuzzy(name = "lightening bolt")

            assertEquals("card", card.objectType)
            assertTrue(card.name.lowercase().contains("lightning bolt"))
        }

    @Test
    fun namedFuzzy_partialName_findsCard() =
        runBlocking {
            val api = CardsApi()
            val card = api.namedFuzzy(name = "aust com")

            assertEquals("card", card.objectType)
            // Should find Austere Command or similar
            assertTrue(card.name.isNotBlank())
        }
}
