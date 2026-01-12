package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for /bulk-data endpoints.
 *
 * Note: These tests perform real network calls and are intended for local verification.
 */
class BulkDataApiIntegrationTest {

    @Test
    fun all_realCall_returnsListWithData() =
        runBlocking {
            val api = BulkDataApi()
            val result = api.all()

            assertEquals("list", result.objectType)
            assertTrue(result.data.isNotEmpty(), "Expected non-empty list of bulk data from /bulk-data")
        }

    @Test
    fun byType_oracleCards_realCall_returnsKnownType() =
        runBlocking {
            val api = BulkDataApi()
            val item = api.byType("oracle-cards")

            assertEquals("bulk_data", item.objectType)
            assertEquals("oracle_cards", item.type)
            assertTrue(item.name.isNotBlank(), "Expected non-blank name for oracle-cards bulk type")
            assertTrue(item.downloadUri.isNotBlank(), "Expected download_uri to be provided")
        }
}
