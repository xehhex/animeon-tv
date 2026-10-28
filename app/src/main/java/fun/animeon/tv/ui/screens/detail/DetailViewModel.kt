package fun.animeon.tv.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fun.animeon.tv.data.model.AnimeDetail
import fun.animeon.tv.data.model.EpisodeInfo
import fun.animeon.tv.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val isLoading: Boolean = true,
    val anime: AnimeDetail? = null,
    val selectedTranslation: String? = null,
    val availableTranslations: List<String> = emptyList(),
    val episodes: Map<String, EpisodeInfo> = emptyMap(),
    val errorMessage: String? = null
)

class DetailViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadAnime(animeUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            val result = repository.getAnimeDetail(animeUrl)
            result.onSuccess { detail ->
                val translations = detail.translations?.keys?.toList() ?: emptyList()
                val initialTranslation = translations.firstOrNull()
                val episodes = initialTranslation?.let { detail.translations?.get(it)?.episodes } ?: emptyMap()

                _uiState.value = DetailUiState(
                    isLoading = false,
                    anime = detail,
                    selectedTranslation = initialTranslation,
                    availableTranslations = translations,
                    episodes = episodes
                )
            }.onFailure { err ->
                _uiState.value = DetailUiState(
                    isLoading = false,
                    errorMessage = err.localizedMessage ?: "Не удалось загрузить аниме"
                )
            }
        }
    }

    fun selectTranslation(translation: String) {
        val currentDetail = _uiState.value.anime ?: return
        val episodes = currentDetail.translations?.get(translation)?.episodes ?: emptyMap()
        _uiState.value = _uiState.value.copy(
            selectedTranslation = translation,
            episodes = episodes
        )
    }
}
