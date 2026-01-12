package devmugi.mtgcards.scryfall.api.core

import io.ktor.client.plugins.logging.*

/**
 * Configuration options for the Scryfall API client.
 *
 * @property baseUrl The base URL for the Scryfall API (default: https://api.scryfall.com)
 * @property userAgent User-Agent header to send with requests
 * @property connectTimeoutMillis Connection timeout in milliseconds (default: 10 seconds)
 * @property requestTimeoutMillis Request timeout in milliseconds (default: 15 seconds)
 * @property socketTimeoutMillis Socket timeout in milliseconds (default: 30 seconds)
 * @property maxRetries Maximum number of retry attempts for failed requests (default: 2)
 * @property enableLogging Enable HTTP request/response logging (default: false)
 * @property logLevel Logging verbosity level (default: INFO)
 */
data class ScryfallConfig(
    val baseUrl: String = "https://api.scryfall.com",
    val userAgent: String = "Devmugi Scryfall API KMP library " +
        "(https://github.com/devmugi/scryfall-api; contact: anry200@gmail.com)",
    val connectTimeoutMillis: Long = 10_000,
    val requestTimeoutMillis: Long = 15_000,
    val socketTimeoutMillis: Long = 30_000,
    val maxRetries: Int = 2,
    val enableLogging: Boolean = false,
    val logLevel: LogLevel = LogLevel.INFO,
)
