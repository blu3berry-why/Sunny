package hu.blu3berry.sunny.core.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN,
        UNAUTHORIZED,
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN,
    }
}
