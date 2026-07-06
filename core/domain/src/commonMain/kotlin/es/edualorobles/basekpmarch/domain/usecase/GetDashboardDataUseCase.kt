package es.edualorobles.basekpmarch.domain.usecase

import es.edualorobles.basekpmarch.domain.model.DashboardData
import es.edualorobles.basekpmarch.domain.repository.DashboardRepository

class GetDashboardDataUseCase(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): DashboardData = repository.getDashboardData()
}
