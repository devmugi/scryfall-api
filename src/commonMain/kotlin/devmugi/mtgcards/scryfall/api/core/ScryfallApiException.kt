package devmugi.mtgcards.scryfall.api.core

import devmugi.mtgcards.scryfall.api.models.ScryfallError

sealed class ScryfallApiException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class InvalidRequest(val error: ScryfallError) : ScryfallApiException(error.details ?: "Invalid request")
    class NotFound(val error: ScryfallError) : ScryfallApiException(error.details ?: "Not found")
    class RateLimited(val error: ScryfallError, val retryAfterSeconds: Long?) :
        ScryfallApiException(error.details ?: "Rate limited")

    class ServerError(val error: ScryfallError) : ScryfallApiException(error.details ?: "Server error")
    class NetworkError(cause: Throwable) : ScryfallApiException("Network error", cause)
    class ParseError(cause: Throwable) : ScryfallApiException("Parse error", cause)
    class HttpError(val status: Int, val error: ScryfallError?) : ScryfallApiException(error?.details ?: "HTTP $status")
}
