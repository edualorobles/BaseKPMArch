package es.edualorobles.basekpmarch.core.ui.utils

fun Double?.orZero(): Double {
    return this ?: 0.0
}