package es.edualorobles.basekpmarch.data.repository

import es.edualorobles.basekpmarch.data.dto.DashboardDto
import es.edualorobles.basekpmarch.data.mapper.toDomain
import es.edualorobles.basekpmarch.domain.model.DashboardData
import es.edualorobles.basekpmarch.domain.repository.DashboardRepository

class DashboardRepositoryImpl : DashboardRepository {

    // TODO: replace with a real network call once a backend exists, e.g.:
    //   httpClient.get("$baseUrl/dashboard").body<DashboardDto>()
    override suspend fun getDashboardData(): DashboardData {
        return DashboardDto(
            title = "Dashboard",
            message = "Welcome to BaseKPMArch"
        ).toDomain()
    }
}
