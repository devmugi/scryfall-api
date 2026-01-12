package devmugi.sample.shared.model

sealed interface SearchState {
    data object Idle : SearchState
    data object Loading : SearchState
    data class Success(val cards: List<CardUi>) : SearchState
    data class Error(val message: String) : SearchState
}
