package es.edualorobles.basekpmarch.domain.repository

import es.edualorobles.basekpmarch.domain.model.DashboardData

/**
 * Contract owned by `core:domain`. `core:data` provides the implementation; `feature:dashboard`
 * never sees it directly — only through [es.edualorobles.basekpmarch.domain.usecase.GetDashboardDataUseCase].
 */
interface DashboardRepository {
    suspend fun getDashboardData(): DashboardData
}
