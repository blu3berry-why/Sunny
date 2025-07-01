package hu.blu3berry.sunny.core.presentation

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class DynamicString(
        val string: String,
    ) : UiText

    data class StringResourceId(
        val id: StringResource,
        // No need to overwrite the equals and the hash methods, we wont compare string resources
        val args: Array<Any> = arrayOf(),
    ) : UiText

    @Composable
    fun asString(): String =
        when (this) {
            is DynamicString -> string
            is StringResourceId -> stringResource(resource = id, formatArgs = args)
        }
}
