package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CardsApiRandomTest {

    @Test
    fun random_withoutParams_buildsCorrectRequestAndParsesCard() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val card = api.random()

            assertEquals("/cards/random", captured?.path)
            assertNull(captured?.params?.get("q"))
            assertEquals("json", captured?.params?.get("format"))
            assertNull(captured?.params?.get("face"))
            assertNull(captured?.params?.get("version"))
            assertEquals("false", captured?.params?.get("pretty"))

            assertEquals("id-123", card.id)
            assertEquals("Grizzly Bears", card.name)
        }

    @Test
    fun random_withParams_includesAllExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockCardEngine { captured = it }

            val api = CardsApi(engine = engine)
            val card = api.random(
                q = "t:dragon",
                format = "json",
                face = "back",
                version = "small",
                pretty = true
            )

            assertEquals("/cards/random", captured?.path)
            assertEquals("t:dragon", captured?.params?.get("q"))
            assertEquals("json", captured?.params?.get("format"))
            assertEquals("back", captured?.params?.get("face"))
            assertEquals("small", captured?.params?.get("version"))
            assertEquals("true", captured?.params?.get("pretty"))

            assertEquals("id-123", card.id)
            assertEquals("Grizzly Bears", card.name)
        }
}
