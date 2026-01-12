package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for /rulings endpoints.
 *
 * Note: These tests perform real network calls and are intended for local verification.
 */
class RulingsApiIntegrationTest {

    @Test
    fun byCollectorId_realCall_returnsListWithData() =
        runBlocking {
            val api = RulingsApi()
            val result = api.byCollectorId(code = "lea", number = "233")

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty list of rulings for lea/233")
        }
}
