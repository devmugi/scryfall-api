package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.SortDirection
import devmugi.mtgcards.scryfall.api.models.SortingCards
import devmugi.mtgcards.scryfall.api.models.UniqueModes
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CardsApiSearchTest {

    @Test
    fun search_defaults_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val result = api.search(q = "t:bear")

            assertEquals("/cards/search", captured?.path)
            assertEquals("t:bear", captured?.params?.get("q"))
            assertNull(captured?.params?.get("unique"))
            assertNull(captured?.params?.get("order"))
            assertNull(captured?.params?.get("dir"))
            assertEquals("false", captured?.params?.get("include_extras"))
            assertEquals("false", captured?.params?.get("include_multilingual"))
            assertEquals("false", captured?.params?.get("include_variations"))
            assertEquals("1", captured?.params?.get("page"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("false", captured?.params?.get("pretty"))

            assertEquals("list", result.objectType)
            assertEquals(1, result.totalCards)
            assertEquals(1, result.data.size)
            assertEquals("id-123", result.data.first().id)
            assertEquals("Grizzly Bears", result.data.first().name)
        }

    @Test
    fun search_withAllParams_includesExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine(hasMore = true, totalCards = 123) { captured = it }

            val api = CardsApi(engine = engine)
            val result = api.search(
                q = "type:dragon",
                unique = UniqueModes.ART,
                order = SortingCards.RELEASED,
                dir = SortDirection.DESC,
                includeExtras = true,
                includeMultilingual = true,
                includeVariations = true,
                page = 2,
                format = "json",
                pretty = true
            )

            assertEquals("/cards/search", captured?.path)
            assertEquals("type:dragon", captured?.params?.get("q"))
            assertEquals(UniqueModes.ART.value, captured?.params?.get("unique"))
            assertEquals(SortingCards.RELEASED.value, captured?.params?.get("order"))
            assertEquals(SortDirection.DESC.value, captured?.params?.get("dir"))
            assertEquals("true", captured?.params?.get("include_extras"))
            assertEquals("true", captured?.params?.get("include_multilingual"))
            assertEquals("true", captured?.params?.get("include_variations"))
            assertEquals("2", captured?.params?.get("page"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("true", captured?.params?.get("pretty"))

            assertEquals("list", result.objectType)
            assertEquals(123, result.totalCards)
            assertEquals(true, result.hasMore)
            assertEquals("Grizzly Bears", result.data.first().name)
        }
}
