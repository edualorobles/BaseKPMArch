package es.edualorobles.basekpmarch.utils

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    fun Long.pad(): String = if (this < 10) "0$this" else "$this"

    return "${hours.pad()}:${minutes.pad()}:${secs.pad()}"
}