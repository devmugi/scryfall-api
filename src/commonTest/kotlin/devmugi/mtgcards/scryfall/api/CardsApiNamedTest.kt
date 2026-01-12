package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CardsApiNamedTest {

    @Test
    fun namedExact_defaults_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val card = api.namedExact(name = "Grizzly Bears")

            assertEquals("/cards/named", captured?.path)
            assertEquals("Grizzly Bears", captured?.params?.get("exact"))
            assertNull(captured?.params?.get("set"))
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))

            assertEquals("id-123", card.id)
            assertEquals("Grizzly Bears", card.name)
        }

    @Test
    fun namedExact_withParams_includesAllExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(engine = engine)
            val card = api.namedExact(
                name = "Grizzly Bears",
                set = "tst",
                format = "json",
                face = "back",
                version = "small",
                pretty = true
            )

            assertEquals("/cards/named", captured?.path)
            assertEquals("Grizzly Bears", captured?.params?.get("exact"))
            assertEquals("tst", captured?.params?.get("set"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("back", captured?.params?.get("face"))
            assertEquals("small", captured?.params?.get("version"))
            assertEquals("true", captured?.params?.get("pretty"))

            assertEquals("id-123", card.id)
        }

    @Test
    fun namedFuzzy_defaults_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val card = api.namedFuzzy(name = "grizly bers")

            assertEquals("/cards/named", captured?.path)
            assertEquals("grizly bers", captured?.params?.get("fuzzy"))
            assertNull(captured?.params?.get("set"))
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))

            assertEquals("Grizzly Bears", card.name)
        }

    @Test
    fun namedFuzzy_withParams_includesAllExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(engine = engine)
            val card = api.namedFuzzy(
                name = "grizly bers",
                set = "tsp",
                format = "json",
                face = "back",
                version = "small",
                pretty = true
            )

            assertEquals("/cards/named", captured?.path)
            assertEquals("grizly bers", captured?.params?.get("fuzzy"))
            assertEquals("tsp", captured?.params?.get("set"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("back", captured?.params?.get("face"))
            assertEquals("small", captured?.params?.get("version"))
            assertEquals("true", captured?.params?.get("pretty"))

            assertEquals("id-123", card.id)
        }
}
