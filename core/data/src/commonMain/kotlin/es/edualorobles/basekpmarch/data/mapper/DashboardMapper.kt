package es.edualorobles.basekpmarch.data.mapper

import es.edualorobles.basekpmarch.data.dto.DashboardDto
import es.edualorobles.basekpmarch.domain.model.DashboardData

fun DashboardDto.toDomain(): DashboardData = DashboardData(
    title = title,
    message = message
)
