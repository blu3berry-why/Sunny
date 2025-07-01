package hu.blu3berry.sunny.features.food.data.repository

import hu.blu3berry.sunny.features.food.data.database.FoodItemDao
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the FoodRepository interface.
 * This class is in the data layer and knows about the data sources.
 */
class FoodRepositoryImpl(
    private val foodItemDao: FoodItemDao
) : FoodRepository {

    override suspend fun saveFoodItem(foodItem: FoodItem) {
        foodItemDao.upsertFoodItem(foodItem)
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {
        foodItemDao.deleteFoodItem(foodItem)
    }

    override fun getAllFoodItems(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoodItems()
    }

    override suspend fun getFoodItemById(id: Int): FoodItem? {
        return foodItemDao.getFoodItemById(id)
    }

    override fun getFoodItemByIdFlow(id: Int): Flow<FoodItem?> {
        return foodItemDao.getFoodItemByIdFlow(id)
    }
}
