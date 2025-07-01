package hu.blu3berry.sunny.features.food.presentation

import hu.blu3berry.sunny.features.food.domain.model.FoodItem

sealed interface FoodItemListAction {
    data class OnSearchQueryChanged(val query: String) : FoodItemListAction
    data object LoadFoodItems : FoodItemListAction
    object OnNewFoodItemClick : FoodItemListAction

    data class OnFoodItemClicked(val foodItem: FoodItem) : FoodItemListAction
}
