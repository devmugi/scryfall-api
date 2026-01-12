package devmugi.sample.shared.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import devmugi.sample.shared.model.SearchState
import devmugi.sample.shared.ui.CardResults
import devmugi.sample.shared.ui.SearchBar
import devmugi.sample.shared.ui.ViewModeToggle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSearchScreen(
    viewModel: CardSearchViewModel = remember { CardSearchViewModel() },
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scryfall Search") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = viewModel::onQueryChange,
                onSearch = viewModel::onSearch,
                onRandom = viewModel::onRandom,
                enabled = state.searchState !is SearchState.Loading,
            )

            ViewModeToggle(
                mode = state.viewMode,
                onModeChange = viewModel::onViewModeChange,
            )

            when (val searchState = state.searchState) {
                is SearchState.Idle -> {
                    EmptyMessage(
                        message = "Search for cards or tap shuffle for a random card",
                    )
                }

                is SearchState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is SearchState.Success -> {
                    if (searchState.cards.isEmpty()) {
                        EmptyMessage(message = "No cards found")
                    } else {
                        CardResults(
                            cards = searchState.cards,
                            viewMode = state.viewMode,
                        )
                    }
                }

                is SearchState.Error -> {
                    EmptyMessage(
                        message = "Error: ${searchState.message}",
                        isError = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyMessage(
    message: String,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isError) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
        )
    }
}
