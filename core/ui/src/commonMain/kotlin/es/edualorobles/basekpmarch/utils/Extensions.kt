package es.edualorobles.basekpmarch.utils

fun Double?.orZero(): Double {
    return this ?: 0.0
}