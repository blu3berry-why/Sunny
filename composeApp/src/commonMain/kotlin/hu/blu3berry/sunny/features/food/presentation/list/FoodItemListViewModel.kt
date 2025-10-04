package hu.blu3berry.sunny.features.food.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.blu3berry.sunny.core.presentation.navigation.sendEvent
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository
import hu.blu3berry.sunny.features.food.presentation.list.FoodItemListViewModel.NavigationAction.OnFoodItemClick
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

class FoodItemListViewModel(
    private val repository: FoodRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FoodItemListState())
    val foodItems = repository.getAllFoodItemsAsFlow()
    val state = combine(
        _state,
        foodItems
    ) { state, foodItems ->
        state.copy(
            foodItems = foodItems.filter { foodItem ->
                foodItem.name.contains(state.searchQuery, ignoreCase = true)
            },
            isLoading = false,
            error = null
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        FoodItemListState()
    )

    private val navigationChannel = Channel<NavigationAction>()
    val navigationEventsChannelFlow = navigationChannel.receiveAsFlow()

    interface NavigationAction {
        @Serializable
        data class OnFoodItemClick(val foodItemId: Int) : NavigationAction

        @Serializable
        data object OnNewFoodItemClick : NavigationAction
    }

    init {
        refreshItemsFromRemote()
    }

    fun onAction(action: FoodItemListAction) {
        when (action) {
            is FoodItemListAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }

            is FoodItemListAction.LoadFoodItems -> {}
            is FoodItemListAction.OnFoodItemClicked -> {
                viewModelScope.launch {
                    navigationChannel.sendEvent(OnFoodItemClick(action.foodItem.id!!))
                }
            }

            FoodItemListAction.OnNewFoodItemClick -> {
                viewModelScope.launch {
                    navigationChannel.sendEvent(event = NavigationAction.OnNewFoodItemClick)
                }
            }

            is FoodItemListAction.OnDeleteFoodItemClick -> deleteFoodItem(action.foodItem)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun deleteFoodItem(foodItem: FoodItem) =
        viewModelScope.launch {
            repository.deleteFoodItem(foodItem)
        }

    private fun refreshItemsFromRemote() {
        viewModelScope.launch {
            repository.getAllFoodItems()
        }
    }
}
