package devmugi.mtgcards.scryfall.api.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CardExtensionsTest {

    // Helper function to create test cards with specific properties
    private fun createCard(
        name: String = "Test Card",
        typeLine: String = "Creature — Human",
        colors: List<String>? = null,
        manaValue: Double? = null,
        rarity: String? = null,
        legalities: Legalities = Legalities(),
        keywords: List<String>? = null,
        releasedAt: String = "2020-01-01",
        collectorNumber: String? = null,
        prices: Prices? = null
    ): Card =
        Card(
            id = "test-id",
            objectType = "card",
            name = name,
            typeLine = typeLine,
            colors = colors,
            manaValue = manaValue ?: 0.0,
            rarity = rarity ?: "common",
            legalities = legalities,
            keywords = keywords ?: emptyList(),
            releasedAt = releasedAt,
            collectorNumber = collectorNumber ?: "1",
            prices = prices,
            // Required Core Card Fields
            language = "en",
            layout = "normal",
            printsSearchUri = "https://api.scryfall.com/cards/search?q=test",
            rulingsUri = "https://api.scryfall.com/cards/test-id/rulings",
            scryfallUri = "https://scryfall.com/card/test/1/test-card",
            uri = "https://api.scryfall.com/cards/test-id",
            // Required Print Fields
            borderColor = "black",
            cardBackId = "0aeebaf5-8c7d-4636-9e82-8c27447861f7",
            digital = false,
            frame = "2015",
            fullArt = false,
            highresImage = true,
            imageStatus = "highres_scan",
            promo = false,
            relatedUris = emptyMap(),
            reprint = false,
            scryfallSetUri = "https://scryfall.com/sets/test",
            setName = "Test Set",
            setSearchUri = "https://api.scryfall.com/cards/search?q=set:test",
            setType = "expansion",
            setUri = "https://api.scryfall.com/sets/test-id",
            setCode = "tst",
            setId = "test-set-id",
            storySpotlight = false,
            textless = false,
            variation = false
        )

    // ==================== Legality Tests ====================

    @Test
    fun isLegalIn_returnsTrue_whenLegalInFormat() {
        val card = createCard(
            legalities = Legalities(modern = "legal", standard = "not_legal")
        )
        assertTrue(card.isLegalIn("modern"))
    }

    @Test
    fun isLegalIn_returnsFalse_whenNotLegalInFormat() {
        val card = createCard(
            legalities = Legalities(modern = "not_legal", standard = "legal")
        )
        assertFalse(card.isLegalIn("modern"))
    }

    @Test
    fun isLegalIn_returnsFalse_whenFormatNotInLegalities() {
        val card = createCard(
            legalities = Legalities(modern = "legal")
        )
        assertFalse(card.isLegalIn("vintage"))
    }

    @Test
    fun isLegalIn_returnsFalse_whenLegalitiesHasNullForFormat() {
        val card = createCard(legalities = Legalities())
        assertFalse(card.isLegalIn("modern"))
    }

    @Test
    fun isLegalIn_returnsFalse_whenRestricted() {
        val card = createCard(
            legalities = Legalities(vintage = "restricted")
        )
        assertFalse(card.isLegalIn("vintage"))
    }

    @Test
    fun isLegalIn_returnsFalse_whenBanned() {
        val card = createCard(
            legalities = Legalities(modern = "banned")
        )
        assertFalse(card.isLegalIn("modern"))
    }

    // ==================== Type Check Tests ====================

    @Test
    fun isCreature_returnsTrue_forCreature() {
        val card = createCard(typeLine = "Creature — Human Wizard")
        assertTrue(card.isCreature())
    }

    @Test
    fun isCreature_returnsFalse_forNonCreature() {
        val card = createCard(typeLine = "Instant")
        assertFalse(card.isCreature())
    }

    @Test
    fun isInstant_returnsTrue_forInstant() {
        val card = createCard(typeLine = "Instant")
        assertTrue(card.isInstant())
    }

    @Test
    fun isInstant_returnsFalse_forNonInstant() {
        val card = createCard(typeLine = "Sorcery")
        assertFalse(card.isInstant())
    }

    @Test
    fun isSorcery_returnsTrue_forSorcery() {
        val card = createCard(typeLine = "Sorcery")
        assertTrue(card.isSorcery())
    }

    @Test
    fun isArtifact_returnsTrue_forArtifact() {
        val card = createCard(typeLine = "Artifact — Equipment")
        assertTrue(card.isArtifact())
    }

    @Test
    fun isArtifact_returnsTrue_forArtifactCreature() {
        val card = createCard(typeLine = "Artifact Creature — Golem")
        assertTrue(card.isArtifact())
        assertTrue(card.isCreature())
    }

    @Test
    fun isEnchantment_returnsTrue_forEnchantment() {
        val card = createCard(typeLine = "Enchantment — Aura")
        assertTrue(card.isEnchantment())
    }

    @Test
    fun isPlaneswalker_returnsTrue_forPlaneswalker() {
        val card = createCard(typeLine = "Legendary Planeswalker — Jace")
        assertTrue(card.isPlaneswalker())
    }

    @Test
    fun isLand_returnsTrue_forLand() {
        val card = createCard(typeLine = "Land")
        assertTrue(card.isLand())
    }

    @Test
    fun isLand_returnsTrue_forBasicLand() {
        val card = createCard(typeLine = "Basic Land — Forest")
        assertTrue(card.isLand())
    }

    @Test
    fun isBattle_returnsTrue_forBattle() {
        val card = createCard(typeLine = "Battle — Siege")
        assertTrue(card.isBattle())
    }

    // ==================== Color and Keyword Tests ====================

    @Test
    fun hasColor_returnsTrue_whenCardHasColor() {
        val card = createCard(colors = listOf("R", "G"))
        assertTrue(card.hasColor("R"))
        assertTrue(card.hasColor("G"))
    }

    @Test
    fun hasColor_returnsFalse_whenCardDoesNotHaveColor() {
        val card = createCard(colors = listOf("R", "G"))
        assertFalse(card.hasColor("U"))
    }

    @Test
    fun hasColor_returnsFalse_whenColorsIsNull() {
        val card = createCard(colors = null)
        assertFalse(card.hasColor("R"))
    }

    @Test
    fun isColorless_returnsTrue_whenColorsIsNull() {
        val card = createCard(colors = null)
        assertTrue(card.isColorless())
    }

    @Test
    fun isColorless_returnsTrue_whenColorsIsEmpty() {
        val card = createCard(colors = emptyList())
        assertTrue(card.isColorless())
    }

    @Test
    fun isColorless_returnsFalse_whenCardHasColors() {
        val card = createCard(colors = listOf("R"))
        assertFalse(card.isColorless())
    }

    @Test
    fun isMulticolored_returnsTrue_whenCardHasMultipleColors() {
        val card = createCard(colors = listOf("R", "G"))
        assertTrue(card.isMulticolored())
    }

    @Test
    fun isMulticolored_returnsFalse_whenCardHasOneColor() {
        val card = createCard(colors = listOf("R"))
        assertFalse(card.isMulticolored())
    }

    @Test
    fun isMulticolored_returnsFalse_whenCardIsColorless() {
        val card = createCard(colors = emptyList())
        assertFalse(card.isMulticolored())
    }

    @Test
    fun hasKeyword_returnsTrue_whenCardHasKeyword() {
        val card = createCard(keywords = listOf("flying", "haste"))
        assertTrue(card.hasKeyword("flying"))
        assertTrue(card.hasKeyword("haste"))
    }

    @Test
    fun hasKeyword_returnsFalse_whenCardDoesNotHaveKeyword() {
        val card = createCard(keywords = listOf("flying"))
        assertFalse(card.hasKeyword("trample"))
    }

    @Test
    fun hasKeyword_returnsFalse_whenKeywordsIsNull() {
        val card = createCard(keywords = null)
        assertFalse(card.hasKeyword("flying"))
    }

    // ==================== List Filtering Tests ====================

    @Test
    fun filterByType_returnsMatchingCards() {
        val cards = listOf(
            createCard(name = "Dragon 1", typeLine = "Creature — Dragon"),
            createCard(name = "Instant 1", typeLine = "Instant"),
            createCard(name = "Dragon 2", typeLine = "Creature — Dragon Warrior")
        )

        val dragons = cards.filterByType("Dragon")
        assertEquals(2, dragons.size)
        assertEquals("Dragon 1", dragons[0].name)
        assertEquals("Dragon 2", dragons[1].name)
    }

    @Test
    fun filterByType_returnsEmptyList_whenNoMatches() {
        val cards = listOf(
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Instant")
        )

        val dragons = cards.filterByType("Dragon")
        assertTrue(dragons.isEmpty())
    }

    @Test
    fun filterByColor_returnsExactColorMatch() {
        val cards = listOf(
            createCard(name = "Red", colors = listOf("R")),
            createCard(name = "Red-Green", colors = listOf("R", "G")),
            createCard(name = "Green", colors = listOf("G")),
            createCard(name = "Blue", colors = listOf("U"))
        )

        val redGreen = cards.filterByColor("R", "G")
        assertEquals(1, redGreen.size)
        assertEquals("Red-Green", redGreen[0].name)
    }

    @Test
    fun filterByColorIncludes_returnsCardsWithAnyColor() {
        val cards = listOf(
            createCard(name = "Red", colors = listOf("R")),
            createCard(name = "Red-Green", colors = listOf("R", "G")),
            createCard(name = "Green", colors = listOf("G")),
            createCard(name = "Blue", colors = listOf("U"))
        )

        val redOrGreen = cards.filterByColorIncludes("R", "G")
        assertEquals(3, redOrGreen.size)
        assertTrue(redOrGreen.any { it.name == "Red" })
        assertTrue(redOrGreen.any { it.name == "Red-Green" })
        assertTrue(redOrGreen.any { it.name == "Green" })
    }

    @Test
    fun filterLegalIn_returnsOnlyLegalCards() {
        val cards = listOf(
            createCard(name = "Legal", legalities = Legalities(modern = "legal")),
            createCard(name = "Banned", legalities = Legalities(modern = "banned")),
            createCard(name = "Not Legal", legalities = Legalities(modern = "not_legal"))
        )

        val modernLegal = cards.filterLegalIn("modern")
        assertEquals(1, modernLegal.size)
        assertEquals("Legal", modernLegal[0].name)
    }

    @Test
    fun filterByRarity_returnsMatchingCards() {
        val cards = listOf(
            createCard(name = "Common", rarity = "common"),
            createCard(name = "Rare", rarity = "rare"),
            createCard(name = "Mythic", rarity = "mythic")
        )

        val rares = cards.filterByRarity("rare")
        assertEquals(1, rares.size)
        assertEquals("Rare", rares[0].name)
    }

    @Test
    fun filterByCmc_returnsExactMatches() {
        val cards = listOf(
            createCard(name = "One", manaValue = 1.0),
            createCard(name = "Two", manaValue = 2.0),
            createCard(name = "Three", manaValue = 3.0),
            createCard(name = "Another Three", manaValue = 3.0)
        )

        val threeDrop = cards.filterByCmc(3.0)
        assertEquals(2, threeDrop.size)
        assertTrue(threeDrop.all { it.manaValue == 3.0 })
    }

    @Test
    fun filterByCmcRange_withMinAndMax() {
        val cards = listOf(
            createCard(name = "Zero", manaValue = 0.0),
            createCard(name = "One", manaValue = 1.0),
            createCard(name = "Two", manaValue = 2.0),
            createCard(name = "Three", manaValue = 3.0),
            createCard(name = "Four", manaValue = 4.0)
        )

        val lowCost = cards.filterByCmcRange(min = 1.0, max = 3.0)
        assertEquals(3, lowCost.size)
        assertTrue(lowCost.all { it.manaValue in 1.0..3.0 })
    }

    @Test
    fun filterByCmcRange_withOnlyMin() {
        val cards = listOf(
            createCard(name = "One", manaValue = 1.0),
            createCard(name = "Five", manaValue = 5.0),
            createCard(name = "Seven", manaValue = 7.0)
        )

        val expensive = cards.filterByCmcRange(min = 5.0)
        assertEquals(2, expensive.size)
        assertTrue(expensive.all { it.manaValue >= 5.0 })
    }

    @Test
    fun filterByCmcRange_withOnlyMax() {
        val cards = listOf(
            createCard(name = "One", manaValue = 1.0),
            createCard(name = "Two", manaValue = 2.0),
            createCard(name = "Five", manaValue = 5.0)
        )

        val cheap = cards.filterByCmcRange(max = 2.0)
        assertEquals(2, cheap.size)
        assertTrue(cheap.all { it.manaValue <= 2.0 })
    }

    @Test
    fun filterCreatures_returnsOnlyCreatures() {
        val cards = listOf(
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Instant"),
            createCard(typeLine = "Creature — Dragon")
        )

        val creatures = cards.filterCreatures()
        assertEquals(2, creatures.size)
        assertTrue(creatures.all { it.isCreature() })
    }

    @Test
    fun filterInstants_returnsOnlyInstants() {
        val cards = listOf(
            createCard(typeLine = "Instant"),
            createCard(typeLine = "Sorcery"),
            createCard(typeLine = "Instant")
        )

        val instants = cards.filterInstants()
        assertEquals(2, instants.size)
        assertTrue(instants.all { it.isInstant() })
    }

    @Test
    fun filterSorceries_returnsOnlySorceries() {
        val cards = listOf(
            createCard(typeLine = "Sorcery"),
            createCard(typeLine = "Instant"),
            createCard(typeLine = "Sorcery")
        )

        val sorceries = cards.filterSorceries()
        assertEquals(2, sorceries.size)
        assertTrue(sorceries.all { it.isSorcery() })
    }

    @Test
    fun filterArtifacts_returnsOnlyArtifacts() {
        val cards = listOf(
            createCard(typeLine = "Artifact — Equipment"),
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Artifact Creature — Golem")
        )

        val artifacts = cards.filterArtifacts()
        assertEquals(2, artifacts.size)
        assertTrue(artifacts.all { it.isArtifact() })
    }

    @Test
    fun filterEnchantments_returnsOnlyEnchantments() {
        val cards = listOf(
            createCard(typeLine = "Enchantment — Aura"),
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Enchantment")
        )

        val enchantments = cards.filterEnchantments()
        assertEquals(2, enchantments.size)
        assertTrue(enchantments.all { it.isEnchantment() })
    }

    @Test
    fun filterPlaneswalkers_returnsOnlyPlaneswalkers() {
        val cards = listOf(
            createCard(typeLine = "Legendary Planeswalker — Jace"),
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Planeswalker — Chandra")
        )

        val planeswalkers = cards.filterPlaneswalkers()
        assertEquals(2, planeswalkers.size)
        assertTrue(planeswalkers.all { it.isPlaneswalker() })
    }

    @Test
    fun filterLands_returnsOnlyLands() {
        val cards = listOf(
            createCard(typeLine = "Land"),
            createCard(typeLine = "Creature — Human"),
            createCard(typeLine = "Basic Land — Forest")
        )

        val lands = cards.filterLands()
        assertEquals(2, lands.size)
        assertTrue(lands.all { it.isLand() })
    }

    @Test
    fun filterColorless_returnsOnlyColorlessCards() {
        val cards = listOf(
            createCard(colors = emptyList()),
            createCard(colors = listOf("R")),
            createCard(colors = null)
        )

        val colorless = cards.filterColorless()
        assertEquals(2, colorless.size)
        assertTrue(colorless.all { it.isColorless() })
    }

    @Test
    fun filterMulticolored_returnsOnlyMulticoloredCards() {
        val cards = listOf(
            createCard(colors = listOf("R", "G")),
            createCard(colors = listOf("R")),
            createCard(colors = listOf("U", "B", "R"))
        )

        val multicolored = cards.filterMulticolored()
        assertEquals(2, multicolored.size)
        assertTrue(multicolored.all { it.isMulticolored() })
    }

    // ==================== List Sorting Tests ====================

    @Test
    fun sortByName_sortsAlphabetically() {
        val cards = listOf(
            createCard(name = "Zebra"),
            createCard(name = "Apple"),
            createCard(name = "Mountain")
        )

        val sorted = cards.sortByName()
        assertEquals("Apple", sorted[0].name)
        assertEquals("Mountain", sorted[1].name)
        assertEquals("Zebra", sorted[2].name)
    }

    @Test
    fun sortByCmc_sortsAscending() {
        val cards = listOf(
            createCard(name = "Five", manaValue = 5.0),
            createCard(name = "One", manaValue = 1.0),
            createCard(name = "Three", manaValue = 3.0)
        )

        val sorted = cards.sortByCmc()
        assertEquals("One", sorted[0].name)
        assertEquals("Three", sorted[1].name)
        assertEquals("Five", sorted[2].name)
    }

    @Test
    fun sortByPrice_sortsAscending() {
        val cards = listOf(
            createCard(name = "Expensive", prices = Prices(usd = "50.00")),
            createCard(name = "Cheap", prices = Prices(usd = "1.00")),
            createCard(name = "Medium", prices = Prices(usd = "10.00"))
        )

        val sorted = cards.sortByPrice()
        assertEquals("Cheap", sorted[0].name)
        assertEquals("Medium", sorted[1].name)
        assertEquals("Expensive", sorted[2].name)
    }

    @Test
    fun sortByPrice_putsNullPricesAtEnd() {
        val cards = listOf(
            createCard(name = "Null Price", prices = null),
            createCard(name = "Has Price", prices = Prices(usd = "5.00")),
            createCard(name = "Unparseable", prices = Prices(usd = "N/A"))
        )

        val sorted = cards.sortByPrice()
        assertEquals("Has Price", sorted[0].name)
        assertTrue(sorted[1].name in listOf("Null Price", "Unparseable"))
        assertTrue(sorted[2].name in listOf("Null Price", "Unparseable"))
    }

    @Test
    fun sortByRarity_sortsInRarityOrder() {
        val cards = listOf(
            createCard(name = "Mythic", rarity = "mythic"),
            createCard(name = "Common", rarity = "common"),
            createCard(name = "Rare", rarity = "rare"),
            createCard(name = "Uncommon", rarity = "uncommon")
        )

        val sorted = cards.sortByRarity()
        assertEquals("Common", sorted[0].name)
        assertEquals("Uncommon", sorted[1].name)
        assertEquals("Rare", sorted[2].name)
        assertEquals("Mythic", sorted[3].name)
    }

    @Test
    fun sortByRarity_handlesSpecialAndBonus() {
        val cards = listOf(
            createCard(name = "Special", rarity = "special"),
            createCard(name = "Common", rarity = "common"),
            createCard(name = "Bonus", rarity = "bonus")
        )

        val sorted = cards.sortByRarity()
        assertEquals("Common", sorted[0].name)
        assertEquals("Special", sorted[1].name)
        assertEquals("Bonus", sorted[2].name)
    }

    @Test
    fun sortByRarity_putsUnknownRaritiesAtEnd() {
        val cards = listOf(
            createCard(name = "Unknown", rarity = "unknown"),
            createCard(name = "Common", rarity = "common"),
            createCard(name = "Null", rarity = null)
        )

        val sorted = cards.sortByRarity()
        assertEquals("Common", sorted[0].name)
        assertTrue(sorted[1].name in listOf("Unknown", "Null"))
        assertTrue(sorted[2].name in listOf("Unknown", "Null"))
    }

    @Test
    fun sortByReleaseDate_sortsOldestFirst() {
        val cards = listOf(
            createCard(name = "Recent", releasedAt = "2023-01-01"),
            createCard(name = "Old", releasedAt = "2020-01-01"),
            createCard(name = "Older", releasedAt = "2021-06-15")
        )

        val sorted = cards.sortByReleaseDate()
        assertEquals("Old", sorted[0].name)
        assertEquals("Older", sorted[1].name)
        assertEquals("Recent", sorted[2].name)
    }

    @Test
    fun sortByCollectorNumber_sortsNumerically() {
        val cards = listOf(
            createCard(name = "Ten", collectorNumber = "10"),
            createCard(name = "One", collectorNumber = "1"),
            createCard(name = "Five", collectorNumber = "5")
        )

        val sorted = cards.sortByCollectorNumber()
        assertEquals("One", sorted[0].name)
        assertEquals("Five", sorted[1].name)
        assertEquals("Ten", sorted[2].name)
    }

    @Test
    fun sortByCollectorNumber_handlesNonNumericCollectorNumbers() {
        val cards = listOf(
            createCard(name = "Numeric", collectorNumber = "5"),
            createCard(name = "Alpha", collectorNumber = "5a"),
            createCard(name = "Beta", collectorNumber = "5b")
        )

        val sorted = cards.sortByCollectorNumber()
        // Just verify it doesn't crash and returns all cards
        assertEquals(3, sorted.size)
    }

    @Test
    fun sortByCollectorNumber_putsNonNumericAtEnd() {
        val cards = listOf(
            createCard(name = "NonNumeric", collectorNumber = "★"),
            createCard(name = "Five", collectorNumber = "5")
        )

        val sorted = cards.sortByCollectorNumber()
        assertEquals("Five", sorted[0].name)
        assertEquals("NonNumeric", sorted[1].name)
    }

    // ==================== Edge Cases and Integration Tests ====================

    @Test
    fun filterAndSort_canBeCombined() {
        val cards = listOf(
            createCard(name = "Expensive Creature", typeLine = "Creature — Dragon", manaValue = 7.0),
            createCard(name = "Cheap Creature", typeLine = "Creature — Human", manaValue = 1.0),
            createCard(name = "Instant", typeLine = "Instant", manaValue = 2.0),
            createCard(name = "Medium Creature", typeLine = "Creature — Beast", manaValue = 4.0)
        )

        val result = cards
            .filterCreatures()
            .filterByCmcRange(max = 5.0)
            .sortByCmc()

        assertEquals(2, result.size)
        assertEquals("Cheap Creature", result[0].name)
        assertEquals("Medium Creature", result[1].name)
    }

    @Test
    fun emptyList_returnsEmptyList() {
        val empty = emptyList<Card>()

        assertTrue(empty.filterCreatures().isEmpty())
        assertTrue(empty.filterByType("Dragon").isEmpty())
        assertTrue(empty.sortByName().isEmpty())
        assertTrue(empty.sortByCmc().isEmpty())
    }

    @Test
    fun multipleFilters_canBeApplied() {
        val cards = listOf(
            createCard(
                name = "Target",
                typeLine = "Creature — Dragon",
                colors = listOf("R"),
                manaValue = 5.0,
                rarity = "rare",
                legalities = Legalities(modern = "legal")
            ),
            createCard(
                name = "Wrong Type",
                typeLine = "Instant",
                colors = listOf("R"),
                manaValue = 5.0,
                rarity = "rare",
                legalities = Legalities(modern = "legal")
            ),
            createCard(
                name = "Wrong Color",
                typeLine = "Creature — Dragon",
                colors = listOf("G"),
                manaValue = 5.0,
                rarity = "rare",
                legalities = Legalities(modern = "legal")
            ),
            createCard(
                name = "Wrong CMC",
                typeLine = "Creature — Dragon",
                colors = listOf("R"),
                manaValue = 7.0,
                rarity = "rare",
                legalities = Legalities(modern = "legal")
            )
        )

        val result = cards
            .filterByType("Dragon")
            .filterByColor("R")
            .filterByCmc(5.0)
            .filterLegalIn("modern")

        assertEquals(1, result.size)
        assertEquals("Target", result[0].name)
    }
}
