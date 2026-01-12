package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API.
 * These tests require network connectivity and the public Scryfall service.
 */
class CardsApiRandomIntegrationTest {

    @Test
    fun random_realCall_returnsCard() =
        runBlocking {
            val api = CardsApi()
            val card = api.random()

            assertTrue(card.id.isNotBlank(), "id should not be blank")
            assertEquals("card", card.objectType)
            assertTrue(card.name.isNotBlank(), "name should not be blank")
            assertTrue(
                card.uri.startsWith("https://api.scryfall.com/cards/"),
                "uri should point to Scryfall API"
            )
        }

    @Test
    fun random_withQuery_realCall_returnsFilteredCard() =
        runBlocking {
            val api = CardsApi()
            val q = "t:dragon"
            val card = api.random(q = q)

            val combined = (card.typeLine + " " + card.name).lowercase()
            assertTrue(
                combined.contains("dragon"),
                "Expected a Dragon-related card for query '$q'. Got typeLine='${card.typeLine}', name='${card.name}'"
            )
        }
}
