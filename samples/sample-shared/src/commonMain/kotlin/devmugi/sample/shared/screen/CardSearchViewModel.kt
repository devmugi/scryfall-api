package devmugi.sample.shared.screen

import devmugi.mtgcards.scryfall.api.CardsApi
import devmugi.mtgcards.scryfall.api.models.Card
import devmugi.sample.shared.model.CardUi
import devmugi.sample.shared.model.SearchState
import devmugi.sample.shared.model.ViewMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSearchViewModel {
    private val api = CardsApi()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    data class UiState(
        val query: String = "",
        val viewMode: ViewMode = ViewMode.LIST,
        val searchState: SearchState = SearchState.Idle,
    )

    fun onQueryChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onViewModeChange(mode: ViewMode) {
        _uiState.update { it.copy(viewMode = mode) }
    }

    fun onSearch() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) return

        _uiState.update { it.copy(searchState = SearchState.Loading) }

        scope.launch {
            try {
                val result = api.search(q = query)
                val cards = result.data.map { it.toUi() }
                _uiState.update { it.copy(searchState = SearchState.Success(cards)) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(searchState = SearchState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }

    fun onRandom() {
        _uiState.update { it.copy(searchState = SearchState.Loading) }

        scope.launch {
            try {
                val card = api.random()
                _uiState.update { it.copy(searchState = SearchState.Success(listOf(card.toUi()))) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(searchState = SearchState.Error(e.message ?: "Unknown error"))
                }
            }
        }
    }
}

private fun Card.toUi(): CardUi = CardUi(
    id = id,
    name = name,
    typeLine = typeLine,
    oracleText = oracleText,
    smallImageUrl = imageUris?.small,
    normalImageUrl = imageUris?.normal,
)
