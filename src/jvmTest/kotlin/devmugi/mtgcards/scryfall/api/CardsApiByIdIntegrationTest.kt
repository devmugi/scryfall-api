package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for card ID-based endpoints against the real Scryfall API.
 */
class CardsApiByIdIntegrationTest {

    @Test
    fun bySetAndCollectorNumber_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            // Lightning Bolt from Magic 2010 (M10)
            val card = api.bySetAndCollectorNumber(code = "m10", number = "146")

            assertEquals("card", card.objectType)
            assertTrue(card.name.contains("Lightning Bolt", ignoreCase = true))
            assertEquals("m10", card.setCode)
        }

    @Test
    fun bySetAndCollectorNumber_withLang_returnsLocalizedCard() =
        runBlocking {
            val api = CardsApi()
            // Lightning Bolt from Magic 2010 in Japanese
            val card = api.bySetAndCollectorNumber(code = "m10", number = "146", lang = "ja")

            assertEquals("card", card.objectType)
            assertEquals("ja", card.language)
        }

    @Test
    fun byScryfallId_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            // Known Scryfall ID for Black Lotus (Alpha)
            val card = api.byScryfallId(id = "bd8fa327-dd41-4737-8f19-2cf5eb1f7cdd")

            assertEquals("card", card.objectType)
            assertTrue(card.name.contains("Black Lotus", ignoreCase = true))
        }

    @Test
    fun byMultiverseId_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            // Multiverse ID 489 is Earthquake from 4th Edition
            val card = api.byMultiverseId(id = 489)

            assertEquals("card", card.objectType)
            assertTrue(card.name.contains("Earthquake", ignoreCase = true))
        }

    @Test
    fun byArenaId_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            // Arena ID 67330 is Opt from Ixalan
            val card = api.byArenaId(id = 67330)

            assertEquals("card", card.objectType)
            assertTrue(card.name.isNotBlank())
        }

    @Test
    fun byTcgplayerId_realCall_returnsExpectedCard() =
        runBlocking {
            val api = CardsApi()
            // TCGPlayer ID 162145 is a known card
            val card = api.byTcgplayerId(id = 162145)

            assertEquals("card", card.objectType)
            assertTrue(card.name.isNotBlank())
        }
}
