package fun.animeon.tv.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fun.animeon.tv.data.model.AnimeItem
import fun.animeon.tv.data.repository.AnimeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<AnimeItem> = emptyList(),
    val errorMessage: String? = null
)

class SearchViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(query = newQuery)
        searchJob?.cancel()
        if (newQuery.isBlank()) {
            _uiState.value = _uiState.value.copy(results = emptyList(), isLoading = false)
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.value = _uiState.value.copy(isLoading = true)
            val searchResults = repository.searchAnime(newQuery)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                results = searchResults
            )
        }
    }
}
