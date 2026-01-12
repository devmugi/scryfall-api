package devmugi.mtgcards.scryfall.api.core

import devmugi.mtgcards.scryfall.api.models.ScryfallError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

/**
 * Base class for Scryfall API endpoints providing shared HTTP client configuration.
 *
 * @param config Configuration options for the API client
 * @param engine Optional custom Ktor HTTP engine
 * @param logger Optional custom logger function for logging HTTP requests/responses.
 *               If provided and logging is enabled in config, this logger will be used.
 *               If not provided, the default platform logger will be used.
 */
open class ScryfallBaseApi(
    private val config: ScryfallConfig = ScryfallConfig(),
    engine: HttpClientEngine? = null,
    private val logger: ((String) -> Unit)? = null,
) {
    // Expose client to subclasses
    protected val client: HttpClient = (engine?.let { HttpClient(it) } ?: HttpClient()).config {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
        install(HttpTimeout) {
            connectTimeoutMillis = config.connectTimeoutMillis
            requestTimeoutMillis = config.requestTimeoutMillis
            socketTimeoutMillis = config.socketTimeoutMillis
        }
        install(UserAgent) {
            agent = config.userAgent
        }

        // Install logging if enabled
        if (config.enableLogging) {
            install(Logging) {
                logger = if (this@ScryfallBaseApi.logger != null) {
                    // Use custom logger provided by user
                    object : Logger {
                        override fun log(message: String) {
                            this@ScryfallBaseApi.logger.invoke(message)
                        }
                    }
                } else {
                    // Use simple default logger that prints to stdout
                    object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                }
                level = config.logLevel
            }
        }

        install(HttpRequestRetry) {
            maxRetries = config.maxRetries
            retryOnExceptionIf { _, cause ->
                when (cause) {
                    is ScryfallApiException.RateLimited,
                    is ScryfallApiException.ServerError,
                    is ScryfallApiException.NetworkError -> true

                    else -> false
                }
            }
            exponentialDelay()
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.isSuccess()) return@validateResponse
                val status = response.status
                val isJson = response.contentType()?.match(ContentType.Application.Json) == true
                val error = try {
                    if (isJson) response.call.body<ScryfallError>() else null
                } catch (@Suppress("SwallowedException") e: SerializationException) {
                    // Expected when response body isn't a valid ScryfallError JSON
                    null
                }
                when (status) {
                    HttpStatusCode.BadRequest -> throw ScryfallApiException.InvalidRequest(
                        error ?: ScryfallError(
                            status = 400,
                            details = "Bad request"
                        )
                    )

                    HttpStatusCode.NotFound -> throw ScryfallApiException.NotFound(
                        error ?: ScryfallError(
                            status = 404,
                            details = "Not found"
                        )
                    )

                    HttpStatusCode.TooManyRequests -> {
                        val retryAfter = response.headers["Retry-After"]?.toLongOrNull()
                        throw ScryfallApiException.RateLimited(
                            error ?: ScryfallError(
                                status = 429,
                                details = "Too Many Requests"
                            ),
                            retryAfter
                        )
                    }

                    else -> {
                        @Suppress("MagicNumber")
                        if (status.value in 500..599) {
                            throw ScryfallApiException.ServerError(
                                error ?: ScryfallError(
                                    status = status.value,
                                    details = "Server error"
                                )
                            )
                        } else {
                            throw ScryfallApiException.HttpError(status.value, error)
                        }
                    }
                }
            }
            handleResponseExceptionWithRequest { cause, _ ->
                when (cause) {
                    is SerializationException -> throw ScryfallApiException.ParseError(cause)
                    is ScryfallApiException -> throw cause
                    else -> throw ScryfallApiException.NetworkError(cause)
                }
            }
        }
        defaultRequest {
            url(config.baseUrl)
        }
    }
}
