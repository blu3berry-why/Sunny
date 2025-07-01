package hu.blu3berry.sunny.features.food.domain.usecase

import hu.blu3berry.sunny.features.food.domain.model.FoodItem

class SaveFoodItemUseCase {
    suspend operator fun invoke(item: FoodItem) {
        // Here you would typically call a repository method to save the food item.
    }
}