package hu.blu3berry.sunny.features.food.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.blu3berry.sunny.core.presentation.UiText
import hu.blu3berry.sunny.core.presentation.navigation.sendEvent
import hu.blu3berry.sunny.database.FoodDatabase
import hu.blu3berry.sunny.features.food.domain.usecase.GetFoodItemByIdUseCase
import hu.blu3berry.sunny.features.food.domain.usecase.SaveFoodItemUseCase
import hu.blu3berry.sunny.features.food.domain.usecase.UpdateFoodItemUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.text.category

sealed class AddEditFoodItemViewModel(
) : ViewModel() {

    protected val navigationChannel = Channel<NavigationAction>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    sealed interface NavigationAction {
        data object OnFoodItemSaved : NavigationAction
        data object OnBackPressed : NavigationAction
    }

    protected val _state = MutableStateFlow(AddEditFoodItemState())
    val state = _state.asStateFlow()
    fun onAction(action: AddEditFoodItemAction) {
        when (action) {
            is AddEditFoodItemAction.OnNameChanged -> _state.update { it.copy(name = action.value) }
            is AddEditFoodItemAction.OnCategoryChanged -> _state.update { it.copy(category = action.value) }
            is AddEditFoodItemAction.OnAmountChanged -> _state.update { it.copy(amount = action.value) }
            is AddEditFoodItemAction.OnUnitChanged -> _state.update { it.copy(unit = action.value) }
            is AddEditFoodItemAction.OnExpirationDateChanged -> _state.update {
                it.copy(
                    expirationDate = action.value,
                )
            }

            is AddEditFoodItemAction.OnLocationChanged -> _state.update { it.copy(location = action.value) }
            is AddEditFoodItemAction.OnNotesChanged -> _state.update { it.copy(notes = action.value) }

            AddEditFoodItemAction.OnDatePickerModalToggled -> _state.update { it.copy(
                modalDatePickerVisible = !it.modalDatePickerVisible
            )
            }
            is AddEditFoodItemAction.OnSaveClicked -> {
                viewModelScope.launch {
                    navigationChannel.sendEvent(NavigationAction.OnFoodItemSaved)
                }
                saveFoodItem()
            }

            AddEditFoodItemAction.OnBackPressed -> viewModelScope.launch {
                navigationChannel.sendEvent(NavigationAction.OnBackPressed)
            }

        }
    }

    abstract fun saveFoodItem()
}

class EditFoodItemViewModel(
    private val id: Int,
    private val getFoodItemUseCase: GetFoodItemByIdUseCase,
    private val updateFoodItemUseCase: UpdateFoodItemUseCase,
) : AddEditFoodItemViewModel() {

    init {
        loadFoodItem(id)
    }

    fun loadFoodItem(id: Int) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val foodItem = getFoodItemUseCase(id)
            foodItem?.let {
                _state.update { currentState ->
                    currentState.copy(
                        name = it.name,
                        category = it.category,
                        amount = it.quantity.amount,
                        unit = it.quantity.unit,
                        expirationDate = it.expirationDate,
                        location = it.location,
                        notes = it.notes,
                        isLoading = false
                    )
                }
            } ?: _state.update {
                it.copy(
                    isLoading = false,
                    error = UiText.DynamicString("Food Item Not Found!")
                )
            }
        }
    }

    override fun saveFoodItem() {
        viewModelScope.launch {
            updateFoodItemUseCase(state.value.toFoodItem(id))
        }
    }
}

class AddFoodItemViewModel(
    private val saveFoodItemUseCase: SaveFoodItemUseCase,
) : AddEditFoodItemViewModel() {

    override fun saveFoodItem() {
        viewModelScope.launch {
            saveFoodItemUseCase(state.value.toFoodItem())
        }
    }
}
