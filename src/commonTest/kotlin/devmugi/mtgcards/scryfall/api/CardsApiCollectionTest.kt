package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.Identifier
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CardsApiCollectionTest {

    @Test
    fun collection_postsJsonAndParsesList() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val result = api.collection(
                identifiers = listOf(Identifier(name = "Grizzly Bears"))
            )

            assertEquals("/cards/collection", captured?.path)

            assertEquals("list", result.objectType)
            assertEquals(1, result.totalCards)
            assertNotNull(result.data.firstOrNull())
            assertEquals("Grizzly Bears", result.data.first().name)
        }

    @Test
    fun collection_withMultipleIdentifiers_postsCorrectly() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine { captured = it }

            val api = CardsApi(engine = engine)
            val result = api.collection(
                identifiers = listOf(
                    Identifier(id = "id-123"),
                    Identifier(name = "Lightning Bolt"),
                    Identifier(set = "neo", collectorNumber = "123"),
                    Identifier(multiverseId = 1234)
                )
            )

            assertEquals("/cards/collection", captured?.path)
            assertEquals("list", result.objectType)
        }
}
