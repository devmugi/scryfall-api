@file:OptIn(ExperimentalJsExport::class)
@file:Suppress("unused")

package devmugi.mtgcards.scryfall.js

import devmugi.mtgcards.scryfall.api.core.ScryfallApiException
import devmugi.mtgcards.scryfall.api.models.ScryfallError
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

// HTTP status code constants
private object HttpStatus {
    const val BAD_REQUEST = 400
    const val NOT_FOUND = 404
    const val RATE_LIMITED = 429
    const val SERVER_ERROR = 500
}

/**
 * Error type constants for JS consumers.
 *
 * Usage:
 * ```javascript
 * import { ErrorType } from '@devmugi/scryfall-api';
 *
 * try {
 *     const card = await api.byScryfallId("invalid-id");
 * } catch (error) {
 *     if (error.type === ErrorType.NOT_FOUND) {
 *         console.log("Card not found");
 *     }
 * }
 * ```
 */
@JsExport
object ErrorType {
    /** Invalid request parameters (400) */
    const val INVALID_REQUEST = "invalid_request"

    /** Resource not found (404) */
    const val NOT_FOUND = "not_found"

    /** Rate limited by Scryfall API (429) */
    const val RATE_LIMITED = "rate_limited"

    /** Server error from Scryfall (5xx) */
    const val SERVER_ERROR = "server_error"

    /** Network connectivity error */
    const val NETWORK_ERROR = "network_error"

    /** JSON parsing error */
    const val PARSE_ERROR = "parse_error"

    /** Other HTTP error */
    const val HTTP_ERROR = "http_error"

    /** Unknown error type */
    const val UNKNOWN = "unknown"
}

/**
 * JavaScript-friendly error representation for Scryfall API errors.
 *
 * This class wraps Kotlin exceptions into a format that's easy to use from JavaScript.
 *
 * Usage:
 * ```javascript
 * import { JsScryfallError, ErrorType } from '@devmugi/scryfall-api';
 *
 * try {
 *     const card = await api.byScryfallId("invalid-id");
 * } catch (error) {
 *     const scryfallError = JsScryfallError.fromError(error);
 *     console.log(`Error type: ${scryfallError.type}`);
 *     console.log(`Message: ${scryfallError.message}`);
 *     if (scryfallError.status) {
 *         console.log(`HTTP Status: ${scryfallError.status}`);
 *     }
 * }
 * ```
 */
@JsExport
data class JsScryfallError(
    /** The error type (see ErrorType constants) */
    val type: String,

    /** Human-readable error message */
    val message: String,

    /** HTTP status code (if applicable) */
    val status: Int?,

    /** Scryfall error code (if available) */
    val code: String?,

    /** Detailed error description from Scryfall */
    val details: String?,

    /** Retry-after seconds for rate limiting (if applicable) */
    val retryAfterSeconds: Int?,

    /** Any warnings returned by the API */
    val warnings: Array<String>?
) {
    companion object {
        /**
         * Creates a JsScryfallError from a Kotlin exception.
         *
         * @param error The caught exception
         * @return A JsScryfallError with appropriate type and details
         */
        fun fromException(error: Throwable): JsScryfallError =
            when (error) {
                is ScryfallApiException.InvalidRequest -> fromApiError(error, ErrorType.INVALID_REQUEST)
                is ScryfallApiException.NotFound -> fromApiError(error, ErrorType.NOT_FOUND)
                is ScryfallApiException.RateLimited -> fromRateLimited(error)
                is ScryfallApiException.ServerError -> fromApiError(error, ErrorType.SERVER_ERROR)
                is ScryfallApiException.NetworkError -> fromSimpleError(error, ErrorType.NETWORK_ERROR)
                is ScryfallApiException.ParseError -> fromSimpleError(error, ErrorType.PARSE_ERROR)
                is ScryfallApiException.HttpError -> fromHttpError(error)
                else -> fromUnknownError(error)
            }

        private fun fromApiError(error: ScryfallApiException, errorType: String): JsScryfallError {
            val scryfallError = extractScryfallError(error)
            val defaultStatus = when (errorType) {
                ErrorType.INVALID_REQUEST -> HttpStatus.BAD_REQUEST
                ErrorType.NOT_FOUND -> HttpStatus.NOT_FOUND
                ErrorType.SERVER_ERROR -> HttpStatus.SERVER_ERROR
                else -> null
            }
            return JsScryfallError(
                type = errorType,
                message = error.message ?: errorType.replace("_", " ").replaceFirstChar { it.uppercase() },
                status = scryfallError?.status ?: defaultStatus,
                code = scryfallError?.code,
                details = scryfallError?.details,
                retryAfterSeconds = null,
                warnings = scryfallError?.warnings?.toTypedArray()
            )
        }

        private fun fromRateLimited(error: ScryfallApiException.RateLimited): JsScryfallError =
            JsScryfallError(
                type = ErrorType.RATE_LIMITED,
                message = error.message ?: "Rate limited",
                status = error.error.status ?: HttpStatus.RATE_LIMITED,
                code = error.error.code,
                details = error.error.details,
                retryAfterSeconds = error.retryAfterSeconds?.toInt(),
                warnings = error.error.warnings?.toTypedArray()
            )

        private fun fromHttpError(error: ScryfallApiException.HttpError): JsScryfallError =
            JsScryfallError(
                type = ErrorType.HTTP_ERROR,
                message = error.message ?: "HTTP error",
                status = error.status,
                code = error.error?.code,
                details = error.error?.details,
                retryAfterSeconds = null,
                warnings = error.error?.warnings?.toTypedArray()
            )

        private fun fromSimpleError(error: Throwable, errorType: String): JsScryfallError =
            JsScryfallError(
                type = errorType,
                message = error.message ?: errorType.replace("_", " ").replaceFirstChar { it.uppercase() },
                status = null,
                code = null,
                details = error.cause?.message,
                retryAfterSeconds = null,
                warnings = null
            )

        private fun fromUnknownError(error: Throwable): JsScryfallError =
            JsScryfallError(
                type = ErrorType.UNKNOWN,
                message = error.message ?: "Unknown error",
                status = null,
                code = null,
                details = null,
                retryAfterSeconds = null,
                warnings = null
            )

        private fun extractScryfallError(error: ScryfallApiException): ScryfallError? =
            when (error) {
                is ScryfallApiException.InvalidRequest -> error.error
                is ScryfallApiException.NotFound -> error.error
                is ScryfallApiException.ServerError -> error.error
                is ScryfallApiException.RateLimited -> error.error
                is ScryfallApiException.HttpError -> error.error
                else -> null
            }
    }
}
