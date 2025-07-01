package hu.blu3berry.sunny.features.food.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import hu.blu3berry.sunny.features.food.domain.usecase.SaveFoodItemUseCase
import hu.blu3berry.sunny.features.food.presentation.components.DatePickerField
import hu.blu3berry.sunny.features.food.presentation.components.DropdownSelector

@Composable
fun AddEditFoodItemViewRoot(
    foodItemId: Int? = null,
    viewModel: AddEditFoodItemViewModel = remember { 
        AddEditFoodItemViewModel(
            saveFoodItemUseCase = SaveFoodItemUseCase(),
            foodItemId = foodItemId
        )
    }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()


    AddEditFoodItemView(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun AddEditFoodItemView(
    state: AddEditFoodItemState,
    onAction: (AddEditFoodItemAction) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = state.name,
            onValueChange = { onAction(AddEditFoodItemAction.OnNameChanged(it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownSelector(
            label = "Category",
            options = FoodCategory.entries,
            selected = state.category,
            onSelect = { onAction(AddEditFoodItemAction.OnCategoryChanged(it)) }
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = state.amount.toString(),
                onValueChange = { it.toDoubleOrNull()?.let { amt -> onAction(AddEditFoodItemAction.OnAmountChanged(amt)) } },
                label = { Text("Amount") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            DropdownSelector(
                label = "Unit",
                options = UnitOfMeasure.entries,
                selected = state.unit,
                onSelect = { onAction(AddEditFoodItemAction.OnUnitChanged(it)) },
                modifier = Modifier.weight(1f)
            )
        }

        DatePickerField(
            label = "Expiration Date",
            date = state.expirationDate,
            onDateSelected = { onAction(AddEditFoodItemAction.OnExpirationDateChanged(it)) }
        )

        DropdownSelector(
            label = "Location",
            options = StorageLocation.entries,
            selected = state.location,
            onSelect = { onAction(AddEditFoodItemAction.OnLocationChanged(it)) }
        )

        OutlinedTextField(
            value = state.notes,
            onValueChange = { onAction(AddEditFoodItemAction.OnNotesChanged(it)) },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onNavigateBack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Back")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onAction(AddEditFoodItemAction.OnSaveClicked) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }
    }
}
