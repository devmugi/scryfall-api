package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for symbology endpoints.
 *
 * Note: These tests perform real network calls and are intended for local verification.
 */
class CardSymbolApiIntegrationTest {

    @Test
    fun all_realCall_returnsListWithData() =
        runBlocking {
            val api = CardSymbolApi()
            val result = api.all()

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty list of symbols from /symbology")
        }

    @Test
    fun parseMana_realCall_parsesKnownCost() =
        runBlocking {
            val api = CardSymbolApi()
            val parsed = api.parseMana(cost = "{2}{G}{U}")

            assertEquals(4.0, parsed.cmc)
            assertTrue(parsed.colors.isNotEmpty(), "Expected colors to be non-empty for {2}{G}{U}")
            assertTrue(parsed.multicolored, "Expected multicolored to be true for {2}{G}{U}")
        }
}
