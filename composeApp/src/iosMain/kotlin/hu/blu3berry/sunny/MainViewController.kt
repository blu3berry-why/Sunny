package hu.blu3berry.sunny

import androidx.compose.ui.window.ComposeUIViewController
import hu.blu3berry.sunny.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() },
) { App() }