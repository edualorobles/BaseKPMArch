package es.edualorobles.basekpmarch.core.ui.utils

import java.util.Locale

fun formatTime(seconds: Long, locale: Locale = Locale.ROOT): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format(locale, "%02d:%02d:%02d", hours, minutes, secs)
}