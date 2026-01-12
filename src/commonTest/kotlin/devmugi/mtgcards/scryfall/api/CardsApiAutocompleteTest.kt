package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CardsApiAutocompleteTest {

    @Test
    fun autocomplete_defaults_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockAutocompleteEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val result = api.autocomplete(q = "li")

            assertEquals("/cards/autocomplete", captured?.path)
            assertEquals("li", captured?.params?.get("q"))
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("pretty"))
            assertEquals("false", captured?.params?.get("include_extras"))

            assertEquals("catalog", result.objectType)
            assertEquals(2, result.totalValues)
            assertEquals(listOf("Lightning Bolt", "Llanowar Elves"), result.data)
        }

    @Test
    fun autocomplete_withParams_includesPrettyAndExtras() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockAutocompleteEngine { captured = it }

            val api = CardsApi(engine = engine)
            val result = api.autocomplete(q = "bear", pretty = true, includeExtras = true)

            assertEquals("/cards/autocomplete", captured?.path)
            assertEquals("bear", captured?.params?.get("q"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("true", captured?.params?.get("pretty"))
            assertEquals("true", captured?.params?.get("include_extras"))

            assertEquals("catalog", result.objectType)
            assertNotNull(result.data)
            assertEquals(2, result.totalValues)
        }
}
