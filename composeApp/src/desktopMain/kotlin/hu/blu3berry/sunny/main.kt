package hu.blu3berry.sunny

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import hu.blu3berry.sunny.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Sunny",
        ) {
            App()
        }
    }
}