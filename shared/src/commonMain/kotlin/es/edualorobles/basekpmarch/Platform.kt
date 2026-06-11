package es.edualorobles.basekpmarch

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform