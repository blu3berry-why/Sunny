package hu.blu3berry.sunny

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import hu.blu3berry.sunny.core.presentation.navigation.SunnyNavigation
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        SunnyNavigation()
    }
}
