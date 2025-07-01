package hu.blu3berry.sunny.core.data

actual object Log {
    actual fun e(
        tag: String,
        message: String,
        throwable: Throwable?,
    ) {
        println("$tag : $message")
    }

    actual fun d(
        tag: String,
        message: String,
    ) {
        println("$tag : $message")
    }

    actual fun i(
        tag: String,
        message: String,
    ) {
        println("$tag : $message")
    }
}