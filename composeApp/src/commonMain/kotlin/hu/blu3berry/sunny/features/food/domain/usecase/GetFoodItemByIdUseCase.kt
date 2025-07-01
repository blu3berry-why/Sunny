package hu.blu3berry.sunny.features.food.domain.usecase

import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository

/**
 * Use case for retrieving a food item by its ID from the repository.
 * This class follows the clean architecture principles by depending on the repository interface.
 */
class GetFoodItemByIdUseCase(
    private val foodRepository: FoodRepository
) {
    /**
     * Retrieve a food item by its ID from the repository.
     * @param id The ID of the food item to retrieve
     * @return The food item if found, null otherwise
     */
    suspend operator fun invoke(id: Int): FoodItem? {
        return foodRepository.getFoodItemById(id)
    }
}