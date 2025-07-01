package hu.blu3berry.sunny.features.food.domain.usecase

import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository

/**
 * Use case for updating an existing food item in the repository.
 * This class follows the clean architecture principles by depending on the repository interface.
 */
class UpdateFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    /**
     * Update an existing food item in the repository.
     * @param item The food item to update
     * @throws IllegalArgumentException if the food item doesn't have an ID
     */
    suspend operator fun invoke(item: FoodItem) {
        // Ensure the item has an ID, which indicates it already exists in the database
        requireNotNull(item.id) { "Cannot update a food item without an ID" }
        
        foodRepository.saveFoodItem(item)
    }
}