package hu.blu3berry.sunny.core.data

import hu.blu3berry.sunny.core.domain.DataError
import hu.blu3berry.sunny.core.domain.Result
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

@Suppress("detekt:TooGenericExceptionCaught", "detekt:SwallowedException", "detekt:ReturnCount")
suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Remote> {
    val response =
        try {
            execute()
        } catch (_: SocketTimeoutException) {
            return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        } catch (_: UnresolvedAddressException) {
            return Result.Error(DataError.Remote.NO_INTERNET)
        } catch (e: Exception) {
            Log.e("safeCall ERROR", e.toString())

            /*
             * This is a workaround for the UnresolvedAddressException which is only on the JVM platform
             * */
            if (isErrorUnknownHostException(e)) {
                return Result.Error(DataError.Remote.NO_INTERNET)
            }

            /*
             * This call is important, the coroutine context may be cancelled
             * and if we catch the exception (Cancellation Exception) the context can not be cancelled
             * This call rethrows the exception is it is the cancellation exception
             * */
            coroutineContext.ensureActive()
            return Result.Error(DataError.Remote.UNKNOWN)
        }
    return responseToResult<T>(response)
}

@Suppress("detekt:MagicNumber")
suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> =
    when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (_: NoTransformationFoundException) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        401 -> Result.Error(DataError.Remote.UNAUTHORIZED)
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> {
            Result.Error(DataError.Remote.SERVER)
        }

        else -> {
            Result.Error(DataError.Remote.UNKNOWN)
        }
    }

expect fun isErrorUnknownHostException(e: Exception): Boolean
