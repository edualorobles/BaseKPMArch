package es.edualorobles.basekpmarch.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Dashboard : Route
}