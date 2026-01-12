package devmugi.mtgcards.scryfall

import devmugi.mtgcards.scryfall.api.*
import devmugi.mtgcards.scryfall.api.core.ScryfallConfig
import io.ktor.client.engine.*

/**
 * Main entry point for accessing the Scryfall API.
 *
 * This class provides access to all Scryfall API endpoints through specialized API classes.
 *
 * @param config Configuration options for the API client
 * @param engine Optional custom Ktor HTTP engine for testing or advanced use cases
 * @param logger Optional custom logger function for logging HTTP requests/responses.
 *               If provided and logging is enabled in config, this logger will be used.
 *               If not provided, the default platform logger will be used.
 *
 * Example usage:
 * ```kotlin
 * // Basic usage
 * val scryfall = Scryfall()
 * val cards = scryfall.cards.search("Lightning Bolt")
 *
 * // With logging enabled
 * val scryfall = Scryfall(
 *     config = ScryfallConfig(enableLogging = true),
 *     logger = { message -> println("Scryfall: $message") }
 * )
 * ```
 */
class Scryfall(
    val config: ScryfallConfig = ScryfallConfig(),
    val engine: HttpClientEngine? = null,
    private val logger: ((String) -> Unit)? = null,
) {
    val cards: CardsApi by lazy { CardsApi(config, engine, logger) }
    val rulings: RulingsApi by lazy { RulingsApi(config, engine, logger) }
    val sets: SetsApi by lazy { SetsApi(config, engine, logger) }
    val symbology: CardSymbolApi by lazy { CardSymbolApi(config, engine, logger) }
    val catalogs: CatalogsApi by lazy { CatalogsApi(config, engine, logger) }
    val bulk: BulkDataApi by lazy { BulkDataApi(config, engine, logger) }
}
