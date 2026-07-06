package es.edualorobles.basekpmarch.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.edualorobles.basekpmarch.domain.usecase.GetDashboardDataUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getDashboardData: GetDashboardDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            _uiState.value = runCatching { getDashboardData() }
                .fold(
                    onSuccess = { DashboardUiState.Content(it) },
                    onFailure = { DashboardUiState.Error(it.message ?: "Unknown error") }
                )
        }
    }
}
