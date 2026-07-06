package es.edualorobles.basekpmarch.presentation

import es.edualorobles.basekpmarch.domain.model.DashboardData

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(val data: DashboardData) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
