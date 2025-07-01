package hu.blu3berry.sunny.features.food.presentation.components

import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import androidx.compose.material3.Text
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DatePickerField(
    label: String,
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {

    val formattedDate = date?.toString() ?: "Select date"
    
    Button(onClick = {
        // Use platform-specific date picker or fallback
        // For now, mock with today:
        onDateSelected(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }) {
        Text("$label: $formattedDate")
    }
}
