package hu.blu3berry.sunny.core.data

actual fun isErrorUnknownHostException(e: Exception): Boolean {
    return e is java.net.UnknownHostException || e is java.net.NoRouteToHostException
}