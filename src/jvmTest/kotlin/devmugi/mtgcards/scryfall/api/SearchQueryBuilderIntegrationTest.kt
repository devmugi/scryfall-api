package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.models.SortingCards
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests for searchWithBuilder DSL against the real Scryfall API.
 */
class SearchQueryBuilderIntegrationTest {

    @Test
    fun searchWithBuilder_simpleTypeQuery_returnsCreatures() =
        runBlocking {
            val api = CardsApi()
            val result = api.searchWithBuilder {
                type("creature")
                cmc(1)
                color("R")
            }

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected creatures with CMC 1 and red color")

            // Verify all returned cards are creatures
            result.data.forEach { card ->
                assertTrue(
                    card.typeLine.contains("Creature", ignoreCase = true),
                    "Expected creature type, got: ${card.typeLine}"
                )
            }
        }

    @Test
    fun searchWithBuilder_complexQuery_returnsDragons() =
        runBlocking {
            val api = CardsApi()
            val result = api.searchWithBuilder(
                order = SortingCards.CMC
            ) {
                type("dragon")
                type("creature")
                powerRange(min = 5)
            }

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected dragon creatures with power >= 5")

            result.data.forEach { card ->
                assertTrue(
                    card.typeLine.contains("Dragon", ignoreCase = true),
                    "Expected Dragon type, got: ${card.typeLine}"
                )
            }
        }

    @Test
    fun searchWithBuilder_withRarityAndSet_returnsFilteredCards() =
        runBlocking {
            val api = CardsApi()
            val result = api.searchWithBuilder {
                rarity("mythic")
                set("neo") // Kamigawa: Neon Dynasty
            }

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected mythic rares from NEO")

            result.data.forEach { card ->
                assertEquals("mythic", card.rarity, "Expected mythic rarity")
                assertEquals("neo", card.setCode, "Expected NEO set")
            }
        }
}
