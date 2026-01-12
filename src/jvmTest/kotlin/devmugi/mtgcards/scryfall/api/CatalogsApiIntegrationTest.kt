package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for /catalog endpoints.
 *
 * Note: These tests perform real network calls and are intended for local verification.
 */
class CatalogsApiIntegrationTest {

    @Test
    fun cardNames_realCall_returnsCatalogWithData() =
        runBlocking {
            val api = CatalogsApi()
            val result = api.cardNames()

            assertEquals("catalog", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty list of card names from /catalog/card-names")
        }

    @Test
    fun keywordAbilities_realCall_returnsCatalogWithData() =
        runBlocking {
            val api = CatalogsApi()
            val result = api.keywordAbilities()

            assertEquals("catalog", result.objectType)
            assertTrue(
                result.data.isNotEmpty(),
                "Expected non-empty list of keyword abilities from /catalog/keyword-abilities"
            )
        }
}
