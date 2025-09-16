package hu.blu3berry.sunny.features.food.domain.usecase

import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository

/**
 * Use case for saving a food item to the repository.
 * This class follows the clean architecture principles by depending on the repository interface.
 */
class SaveFoodItemUseCase(
    private val foodRepository: FoodRepository
) {
    /**
     * Save a food item to the repository.
     * @param item The food item to save
     */
    suspend operator fun invoke(item: FoodItem){
        foodRepository.saveFoodItem(item)
    }
}
