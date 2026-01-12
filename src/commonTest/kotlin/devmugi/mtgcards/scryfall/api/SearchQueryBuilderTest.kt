package devmugi.mtgcards.scryfall.api

import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import devmugi.mtgcards.scryfall.api.models.SortDirection
import devmugi.mtgcards.scryfall.api.models.SortingCards
import devmugi.mtgcards.scryfall.api.models.UniqueModes
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchQueryBuilderTest {

    @Test
    fun name_producesExactNameQuery() {
        val query = searchQuery {
            name("Lightning Bolt")
        }
        assertEquals("!\"Lightning Bolt\"", query)
    }

    @Test
    fun nameContains_producesPartialNameQuery() {
        val query = searchQuery {
            nameContains("bolt")
        }
        assertEquals("bolt", query)
    }

    @Test
    fun type_producesTypeQuery() {
        val query = searchQuery {
            type("creature")
        }
        assertEquals("t:creature", query)
    }

    @Test
    fun color_singleColor_producesColorQuery() {
        val query = searchQuery {
            color("R")
        }
        assertEquals("c:R", query)
    }

    @Test
    fun color_multipleColors_producesColorQuery() {
        val query = searchQuery {
            color("R", "G")
        }
        assertEquals("c:RG", query)
    }

    @Test
    fun cmc_producesExactCmcQuery() {
        val query = searchQuery {
            cmc(3)
        }
        assertEquals("cmc=3", query)
    }

    @Test
    fun cmcRange_withMinOnly_producesMinQuery() {
        val query = searchQuery {
            cmcRange(min = 3)
        }
        assertEquals("cmc>=3", query)
    }

    @Test
    fun cmcRange_withMaxOnly_producesMaxQuery() {
        val query = searchQuery {
            cmcRange(max = 5)
        }
        assertEquals("cmc<=5", query)
    }

    @Test
    fun cmcRange_withBoth_producesRangeQuery() {
        val query = searchQuery {
            cmcRange(min = 2, max = 4)
        }
        assertEquals("cmc>=2 cmc<=4", query)
    }

    @Test
    fun set_producesSetQuery() {
        val query = searchQuery {
            set("neo")
        }
        assertEquals("e:neo", query)
    }

    @Test
    fun rarity_producesRarityQuery() {
        val query = searchQuery {
            rarity("mythic")
        }
        assertEquals("r:mythic", query)
    }

    @Test
    fun text_producesOracleTextQuery() {
        val query = searchQuery {
            text("draw a card")
        }
        assertEquals("o:draw a card", query)
    }

    @Test
    fun power_producesExactPowerQuery() {
        val query = searchQuery {
            power(3)
        }
        assertEquals("pow=3", query)
    }

    @Test
    fun powerRange_withMinOnly_producesMinQuery() {
        val query = searchQuery {
            powerRange(min = 4)
        }
        assertEquals("pow>=4", query)
    }

    @Test
    fun powerRange_withMaxOnly_producesMaxQuery() {
        val query = searchQuery {
            powerRange(max = 10)
        }
        assertEquals("pow<=10", query)
    }

    @Test
    fun powerRange_withBoth_producesRangeQuery() {
        val query = searchQuery {
            powerRange(min = 3, max = 5)
        }
        assertEquals("pow>=3 pow<=5", query)
    }

    @Test
    fun toughness_producesExactToughnessQuery() {
        val query = searchQuery {
            toughness(5)
        }
        assertEquals("tou=5", query)
    }

    @Test
    fun toughnessRange_withBoth_producesRangeQuery() {
        val query = searchQuery {
            toughnessRange(min = 3, max = 5)
        }
        assertEquals("tou>=3 tou<=5", query)
    }

    @Test
    fun format_producesFormatQuery() {
        val query = searchQuery {
            format("standard")
        }
        assertEquals("f:standard", query)
    }

    @Test
    fun isLegal_producesLegalityQuery() {
        val query = searchQuery {
            isLegal("modern")
        }
        assertEquals("legal:modern", query)
    }

    @Test
    fun artist_producesArtistQuery() {
        val query = searchQuery {
            artist("Seb McKinnon")
        }
        assertEquals("a:\"Seb McKinnon\"", query)
    }

    @Test
    fun keyword_producesKeywordQuery() {
        val query = searchQuery {
            keyword("flying")
        }
        assertEquals("o:flying", query)
    }

    @Test
    fun raw_addsRawQueryPart() {
        val query = searchQuery {
            raw("is:commander")
        }
        assertEquals("is:commander", query)
    }

    @Test
    fun complexQuery_combinesMultipleParts() {
        val query = searchQuery {
            type("creature")
            color("R")
            powerRange(min = 4)
            cmcRange(max = 5)
            isLegal("standard")
        }
        assertEquals("t:creature c:R pow>=4 cmc<=5 legal:standard", query)
    }

    @Test
    fun complexQuery_withMixedFilters() {
        val query = searchQuery {
            nameContains("dragon")
            type("creature")
            rarity("rare")
            set("dmu")
            text("flying")
        }
        assertEquals("dragon t:creature r:rare e:dmu o:flying", query)
    }

    @Test
    fun emptyBuilder_producesEmptyQuery() {
        val query = searchQuery {
            // No operations
        }
        assertEquals("", query)
    }

    @Test
    fun searchWithBuilder_defaults_sendsCorrectQuery() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine { captured = it }

            val api = CardsApi(config = ScryfallConfig(), engine = engine)
            val result = api.searchWithBuilder {
                type("creature")
                color("R")
            }

            assertEquals("/cards/search", captured?.path)
            assertEquals("t:creature c:R", captured?.params?.get("q"))
            assertEquals("list", result.objectType)
            assertEquals(1, result.data.size)
        }

    @Test
    fun searchWithBuilder_withAllParams_includesExpectedQueryParams() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine(hasMore = true, totalCards = 123) { captured = it }

            val api = CardsApi(engine = engine)
            val result = api.searchWithBuilder(
                unique = UniqueModes.ART,
                order = SortingCards.RELEASED,
                dir = SortDirection.DESC,
                includeExtras = true,
                includeMultilingual = true,
                includeVariations = true,
                page = 2
            ) {
                type("dragon")
                powerRange(min = 4)
                isLegal("standard")
            }

            assertEquals("/cards/search", captured?.path)
            assertEquals("t:dragon pow>=4 legal:standard", captured?.params?.get("q"))
            assertEquals(UniqueModes.ART.value, captured?.params?.get("unique"))
            assertEquals(SortingCards.RELEASED.value, captured?.params?.get("order"))
            assertEquals(SortDirection.DESC.value, captured?.params?.get("dir"))
            assertEquals("true", captured?.params?.get("include_extras"))
            assertEquals("true", captured?.params?.get("include_multilingual"))
            assertEquals("true", captured?.params?.get("include_variations"))
            assertEquals("2", captured?.params?.get("page"))
            assertEquals(true, result.hasMore)
            assertEquals(123, result.totalCards)
        }

    @Test
    fun searchWithBuilder_complexQuery_buildsCorrectly() =
        runTest {
            var captured: CapturedRequest? = null
            val engine = mockListEngine { captured = it }

            val api = CardsApi(engine = engine)
            api.searchWithBuilder {
                type("creature")
                color("R", "G")
                powerRange(min = 4)
                toughnessRange(min = 4)
                cmcRange(max = 5)
                isLegal("standard")
                rarity("rare")
                text("trample")
            }

            assertEquals(
                "t:creature c:RG pow>=4 tou>=4 cmc<=5 legal:standard r:rare o:trample",
                captured?.params?.get("q")
            )
        }
}
