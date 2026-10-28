package fun.animeon.tv.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fun.animeon.tv.data.model.AnimeItem
import fun.animeon.tv.data.model.ScheduleItem
import fun.animeon.tv.data.repository.AnimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val popularList: List<AnimeItem> = emptyList(),
    val scheduleList: List<ScheduleItem> = emptyList(),
    val heroItem: AnimeItem? = null,
    val errorMessage: String? = null
)

class HomeViewModel(
    private val repository: AnimeRepository = AnimeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val popular = repository.getPopularAnime()
                val schedule = repository.getSchedule()
                val hero = popular.firstOrNull { !it.materialData?.posterUrl.isNullOrEmpty() } ?: popular.firstOrNull()

                _uiState.value = HomeUiState(
                    isLoading = false,
                    popularList = popular,
                    scheduleList = schedule,
                    heroItem = hero
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Ошибка загрузки данных"
                )
            }
        }
    }
}
