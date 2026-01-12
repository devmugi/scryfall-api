package devmugi.mtgcards.scryfall.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that hit the real Scryfall API for /sets endpoints.
 *
 * Note: These tests perform real network calls and are intended for local verification.
 */
class SetsApiIntegrationTest {

    // TODO: check what server returns for /sets endpoint
//    @Test
//    fun all_realCall_returnsListWithData() =
//        runBlocking {
//            val api = SetsApi()
//            val result = api.all()
//
//            assertEquals("list", result.objectType)
//            assertTrue(result.data.isNotEmpty(), "Expected non-empty list of sets from /sets")
//        }

    // TODO: check what server returns for /sets endpoint
//    @Test
//    fun byCode_realCall_returnsKnownSet() =
//        runBlocking {
//            val api = SetsApi()
//            val set = api.byCode(code = "neo")
//
//            assertEquals("neo", set.code)
//            assertTrue(set.name.isNotBlank(), "Expected set name to be non-blank for code 'neo'")
//        }
}
