package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CardsApiByIdTest {

    @Test
    fun bySetAndCollectorNumber_defaults_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val card = api.bySetAndCollectorNumber(code = "tst", number = "123")

            assertEquals("/cards/tst/123", captured?.path)
            assertNull(captured?.params?.get("lang"))
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
            assertEquals("Grizzly Bears", card.name)
        }

    @Test
    fun bySetAndCollectorNumber_withParams_includesAllExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(engine = engine)
            val card = api.bySetAndCollectorNumber(
                code = "tst",
                number = "123",
                lang = "ja",
                format = "json",
                face = "back",
                version = "small",
                pretty = true
            )

            assertEquals("/cards/tst/123", captured?.path)
            assertEquals("ja", captured?.params?.get("lang"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("back", captured?.params?.get("face"))
            assertEquals("small", captured?.params?.get("version"))
            assertEquals("true", captured?.params?.get("pretty"))
            assertEquals("id-123", card.id)
        }

    @Test
    fun byMultiverseId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val card = api.byMultiverseId(id = 1234)

            assertEquals("/cards/multiverse/1234", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
            assertEquals("card", card.objectType)
        }

    @Test
    fun byMtgoId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            api.byMtgoId(id = 5678)

            assertEquals("/cards/mtgo/5678", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
        }

    @Test
    fun byArenaId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            api.byArenaId(id = 9999)

            assertEquals("/cards/arena/9999", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
        }

    @Test
    fun byTcgplayerId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            api.byTcgplayerId(id = 10101)

            assertEquals("/cards/tcgplayer/10101", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
        }

    @Test
    fun byCardmarketId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            api.byCardmarketId(id = 20202)

            assertEquals("/cards/cardmarket/20202", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
        }

    @Test
    fun byScryfallId_buildsCorrectRequestAndParses() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val cardId = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee"
            api.byScryfallId(id = cardId)

            assertEquals("/cards/$cardId", captured?.path)
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))
        }
}
