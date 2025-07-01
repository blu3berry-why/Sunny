package hu.blu3berry.sunny.core.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object FoodList : Route

    @Serializable
    data object AddFoodItem : Route

    @Serializable
    data class EditFoodItem(val id: Int) : Route

}