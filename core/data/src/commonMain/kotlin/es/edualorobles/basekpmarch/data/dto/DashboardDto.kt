package es.edualorobles.basekpmarch.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    val title: String,
    val message: String
)
