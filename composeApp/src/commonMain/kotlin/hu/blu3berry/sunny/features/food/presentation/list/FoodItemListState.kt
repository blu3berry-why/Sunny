package hu.blu3berry.sunny.features.food.presentation.list

import hu.blu3berry.sunny.core.presentation.UiText
import hu.blu3berry.sunny.features.food.domain.model.FoodItem

data class FoodItemListState(
    val foodItems: List<FoodItem> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: UiText? = null
)
