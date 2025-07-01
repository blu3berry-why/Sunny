package hu.blu3berry.sunny.features.food.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.model.Quantity
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import hu.blu3berry.sunny.features.food.domain.usecase.SaveFoodItemUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditFoodItemViewModel(
    private val saveFoodItemUseCase: SaveFoodItemUseCase,
    private val foodItemId: Int? = null
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditFoodItemState())
    val state = _state.asStateFlow()


    init {
        if (foodItemId != null) {
            // For demonstration purposes, we'll use the sample data from FoodItemListViewModel
            // In a real app, this would be a call to a repository or use case
            loadFoodItem(foodItemId)
        }
    }

    private fun loadFoodItem(id: Int) {
        // This is a mock implementation for demonstration
        // In a real app, this would fetch the food item from a repository
        val sampleFoodItems = listOf(
            FoodItem(
                id = 1,
                name = "Milk",
                category = FoodCategory.DAIRY,
                quantity = Quantity(1.0, UnitOfMeasure.LITER),
                location = StorageLocation.FRIDGE
            ),
            FoodItem(
                id = 2,
                name = "Bread",
                category = FoodCategory.GRAIN,
                quantity = Quantity(1.0, UnitOfMeasure.PIECE),
                location = StorageLocation.PANTRY
            ),
            FoodItem(
                id = 3,
                name = "Chicken",
                category = FoodCategory.MEAT,
                quantity = Quantity(500.0, UnitOfMeasure.GRAM),
                location = StorageLocation.FREEZER
            ),
            FoodItem(
                id = 4,
                name = "Apple",
                category = FoodCategory.FRUIT,
                quantity = Quantity(5.0, UnitOfMeasure.PIECE),
                location = StorageLocation.FRIDGE
            )
        )

        val foodItem = sampleFoodItems.find { it.id == id }
        foodItem?.let {
            _state.update { state ->
                state.copy(
                    name = foodItem.name,
                    category = foodItem.category,
                    amount = foodItem.quantity.amount,
                    unit = foodItem.quantity.unit,
                    expirationDate = foodItem.expirationDate,
                    location = foodItem.location,
                    notes = foodItem.notes ?: ""
                )
            }
        }
    }

    fun onAction(action: AddEditFoodItemAction) {
        when (action) {
            is AddEditFoodItemAction.OnNameChanged -> _state.update { it.copy(name = action.value) }
            is AddEditFoodItemAction.OnCategoryChanged -> _state.update { it.copy(category = action.value) }
            is AddEditFoodItemAction.OnAmountChanged -> _state.update { it.copy(amount = action.value) }
            is AddEditFoodItemAction.OnUnitChanged -> _state.update { it.copy(unit = action.value) }
            is AddEditFoodItemAction.OnExpirationDateChanged -> _state.update { it.copy(expirationDate = action.value) }
            is AddEditFoodItemAction.OnLocationChanged -> _state.update { it.copy(location = action.value) }
            is AddEditFoodItemAction.OnNotesChanged -> _state.update { it.copy(notes = action.value) }

            is AddEditFoodItemAction.OnSaveClicked -> saveFoodItem()
        }
    }

    private fun saveFoodItem() {
        viewModelScope.launch {
            val item = state.value.toFoodItem()
            saveFoodItemUseCase(item)
            // Emit navigation event to go back
        }
    }
}
