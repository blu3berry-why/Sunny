package hu.blu3berry.sunny

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform