package com.animeon.tv.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeon.tv.data.api.StreamResolver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerUiState(
    val isLoading: Boolean = true,
    val resolvedStreamUrl: String? = null,
    val isPlaying: Boolean = true,
    val errorMessage: String? = null
)

class PlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadStream(rawEpisodeLink: String) {
        viewModelScope.launch {
            _uiState.value = PlayerUiState(isLoading = true)
            try {
                val streamUrl = StreamResolver.resolveStreamUrl(rawEpisodeLink)
                _uiState.value = PlayerUiState(
                    isLoading = false,
                    resolvedStreamUrl = streamUrl
                )
            } catch (e: Exception) {
                _uiState.value = PlayerUiState(
                    isLoading = false,
                    errorMessage = e.localizedMessage ?: "Не удалось загрузить видеопоток"
                )
            }
        }
    }
}
