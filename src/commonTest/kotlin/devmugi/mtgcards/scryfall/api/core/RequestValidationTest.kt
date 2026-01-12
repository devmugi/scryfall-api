package devmugi.mtgcards.scryfall.api.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class RequestValidationTest {

    // ==================== validateQuery Tests ====================

    @Test
    fun validateQuery_withValidQuery_doesNotThrow() {
        validateQuery("t:creature")
        validateQuery("Lightning Bolt")
        validateQuery("a" + "b".repeat(998)) // 999 characters (within 1000 limit)
    }

    @Test
    fun validateQuery_withBlankQuery_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateQuery("")
        }
        assertEquals("Query cannot be blank", exception.message)
    }

    @Test
    fun validateQuery_withWhitespaceOnlyQuery_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateQuery("   ")
        }
        assertEquals("Query cannot be blank", exception.message)
    }

    @Test
    fun validateQuery_withTooLongQuery_throwsIllegalArgumentException() {
        val tooLongQuery = "a".repeat(1001) // 1001 characters
        val exception = assertFailsWith<IllegalArgumentException> {
            validateQuery(tooLongQuery)
        }
        assertEquals("Query too long (max 1000 characters)", exception.message)
    }

    // ==================== validateCardName Tests ====================

    @Test
    fun validateCardName_withValidName_doesNotThrow() {
        validateCardName("Lightning Bolt")
        validateCardName("Black Lotus")
        validateCardName("a" + "b".repeat(498)) // 499 characters (within 500 limit)
    }

    @Test
    fun validateCardName_withBlankName_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCardName("")
        }
        assertEquals("Card name cannot be blank", exception.message)
    }

    @Test
    fun validateCardName_withTooLongName_throwsIllegalArgumentException() {
        val tooLongName = "a".repeat(501) // 501 characters
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCardName(tooLongName)
        }
        assertEquals("Card name too long (max 500 characters)", exception.message)
    }

    // ==================== validatePage Tests ====================

    @Test
    fun validatePage_withValidPage_doesNotThrow() {
        validatePage(1)
        validatePage(100)
        validatePage(10000) // max allowed
    }

    @Test
    fun validatePage_withZeroPage_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validatePage(0)
        }
        assertEquals("Page must be greater than 0", exception.message)
    }

    @Test
    fun validatePage_withNegativePage_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validatePage(-1)
        }
        assertEquals("Page must be greater than 0", exception.message)
    }

    @Test
    fun validatePage_withTooLargePage_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validatePage(10001)
        }
        assertEquals("Page number too large (max 10000)", exception.message)
    }

    // ==================== validateSetCode Tests ====================

    @Test
    fun validateSetCode_withValidCode_doesNotThrow() {
        validateSetCode("ZNR")
        validateSetCode("M21")
        validateSetCode("DOM")
        validateSetCode("PKHM") // 4 characters
        validateSetCode("PLGS2") // 5 characters (max)
    }

    @Test
    fun validateSetCode_withBlankCode_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateSetCode("")
        }
        assertEquals("Set code cannot be blank", exception.message)
    }

    @Test
    fun validateSetCode_withTooShortCode_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateSetCode("AB")
        }
        assertEquals("Set code must be 3-5 characters", exception.message)
    }

    @Test
    fun validateSetCode_withTooLongCode_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateSetCode("ABCDEF")
        }
        assertEquals("Set code must be 3-5 characters", exception.message)
    }

    @Test
    fun validateSetCode_withNonAlphanumericCode_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateSetCode("A-B")
        }
        assertEquals("Set code must contain only letters and digits", exception.message)
    }

    // ==================== validateScryfallId Tests ====================

    @Test
    fun validateScryfallId_withValidUuid_doesNotThrow() {
        validateScryfallId("12345678-1234-1234-1234-123456789abc")
        validateScryfallId("00000000-0000-0000-0000-000000000000")
        validateScryfallId("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF")
    }

    @Test
    fun validateScryfallId_withBlankId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateScryfallId("")
        }
        assertEquals("ID cannot be blank", exception.message)
    }

    @Test
    fun validateScryfallId_withInvalidUuidFormat_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateScryfallId("not-a-uuid")
        }
        assertEquals("ID must be a valid UUID format", exception.message)
    }

    @Test
    fun validateScryfallId_withIncorrectLength_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateScryfallId("12345678-1234-1234-1234-123456789ab")
        }
        assertEquals("ID must be a valid UUID format", exception.message)
    }

    @Test
    fun validateScryfallId_withMissingHyphens_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateScryfallId("12345678123412341234123456789abc")
        }
        assertEquals("ID must be a valid UUID format", exception.message)
    }

    // ==================== validateCollectorNumber Tests ====================

    @Test
    fun validateCollectorNumber_withValidNumber_doesNotThrow() {
        validateCollectorNumber("123")
        validateCollectorNumber("1a")
        validateCollectorNumber("123★")
        validateCollectorNumber("1234567890") // 10 characters (max)
    }

    @Test
    fun validateCollectorNumber_withBlankNumber_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCollectorNumber("")
        }
        assertEquals("Collector number cannot be blank", exception.message)
    }

    @Test
    fun validateCollectorNumber_withTooLongNumber_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCollectorNumber("12345678901") // 11 characters
        }
        assertEquals("Collector number too long (max 10 characters)", exception.message)
    }

    // ==================== validateIdentifiers Tests ====================

    @Test
    fun validateIdentifiers_withValidList_doesNotThrow() {
        validateIdentifiers(listOf("id1"))
        validateIdentifiers(listOf("id1", "id2", "id3"))
        validateIdentifiers(List(75) { "id$it" }) // 75 items (max)
    }

    @Test
    fun validateIdentifiers_withEmptyList_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateIdentifiers(emptyList<String>())
        }
        assertEquals("Identifiers list cannot be empty", exception.message)
    }

    @Test
    fun validateIdentifiers_withTooManyIdentifiers_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateIdentifiers(List(76) { "id$it" }) // 76 items
        }
        assertEquals("Too many identifiers (max 75 per request)", exception.message)
    }

    // ==================== validateLanguageCode Tests ====================

    @Test
    fun validateLanguageCode_withValidCode_doesNotThrow() {
        validateLanguageCode("en")
        validateLanguageCode("es")
        validateLanguageCode("ja")
        validateLanguageCode("fr")
    }

    @Test
    fun validateLanguageCode_withBlankCode_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateLanguageCode("")
        }
        assertEquals("Language code cannot be blank", exception.message)
    }

    @Test
    fun validateLanguageCode_withWrongLength_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateLanguageCode("eng")
        }
        assertEquals("Language code must be 2 characters (ISO 639-1 format)", exception.message)
    }

    @Test
    fun validateLanguageCode_withUppercase_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateLanguageCode("EN")
        }
        assertEquals("Language code must be lowercase letters", exception.message)
    }

    @Test
    fun validateLanguageCode_withNonLetters_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateLanguageCode("e1")
        }
        assertEquals("Language code must be lowercase letters", exception.message)
    }

    // ==================== ID Validation Tests ====================

    @Test
    fun validateTcgplayerId_withValidId_doesNotThrow() {
        validateTcgplayerId(1)
        validateTcgplayerId(12345)
        validateTcgplayerId(Int.MAX_VALUE)
    }

    @Test
    fun validateTcgplayerId_withZeroId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateTcgplayerId(0)
        }
        assertEquals("TCGPlayer ID must be positive", exception.message)
    }

    @Test
    fun validateTcgplayerId_withNegativeId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateTcgplayerId(-1)
        }
        assertEquals("TCGPlayer ID must be positive", exception.message)
    }

    @Test
    fun validateMultiverseId_withValidId_doesNotThrow() {
        validateMultiverseId(1)
        validateMultiverseId(12345)
        validateMultiverseId(Int.MAX_VALUE)
    }

    @Test
    fun validateMultiverseId_withZeroId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateMultiverseId(0)
        }
        assertEquals("Multiverse ID must be positive", exception.message)
    }

    @Test
    fun validateMultiverseId_withNegativeId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateMultiverseId(-1)
        }
        assertEquals("Multiverse ID must be positive", exception.message)
    }

    @Test
    fun validateArenaId_withValidId_doesNotThrow() {
        validateArenaId(1)
        validateArenaId(12345)
        validateArenaId(Int.MAX_VALUE)
    }

    @Test
    fun validateArenaId_withZeroId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateArenaId(0)
        }
        assertEquals("Arena ID must be positive", exception.message)
    }

    @Test
    fun validateArenaId_withNegativeId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateArenaId(-1)
        }
        assertEquals("Arena ID must be positive", exception.message)
    }

    @Test
    fun validateMtgoId_withValidId_doesNotThrow() {
        validateMtgoId(1)
        validateMtgoId(12345)
        validateMtgoId(Int.MAX_VALUE)
    }

    @Test
    fun validateMtgoId_withZeroId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateMtgoId(0)
        }
        assertEquals("MTGO ID must be positive", exception.message)
    }

    @Test
    fun validateMtgoId_withNegativeId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateMtgoId(-1)
        }
        assertEquals("MTGO ID must be positive", exception.message)
    }

    @Test
    fun validateCardmarketId_withValidId_doesNotThrow() {
        validateCardmarketId(1)
        validateCardmarketId(12345)
        validateCardmarketId(Int.MAX_VALUE)
    }

    @Test
    fun validateCardmarketId_withZeroId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCardmarketId(0)
        }
        assertEquals("Cardmarket ID must be positive", exception.message)
    }

    @Test
    fun validateCardmarketId_withNegativeId_throwsIllegalArgumentException() {
        val exception = assertFailsWith<IllegalArgumentException> {
            validateCardmarketId(-1)
        }
        assertEquals("Cardmarket ID must be positive", exception.message)
    }
}
