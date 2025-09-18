package hu.blu3berry.sunny.features.food.data.repository

import androidx.room.coroutines.createFlow
import hu.blu3berry.sunny.core.data.remote.deleteFoodItemFromServer
import hu.blu3berry.sunny.core.data.remote.getAllFoodItemsFromServer
import hu.blu3berry.sunny.core.data.remote.upsertFoodItemToServer
import hu.blu3berry.sunny.features.food.data.database.FoodItemDao
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Implementation of the FoodRepository interface.
 * This class is in the data layer and knows about the data sources.
 */
class FoodRepositoryImpl(
    private val foodItemDao: FoodItemDao
) : FoodRepository {

    override suspend fun saveFoodItem(foodItem: FoodItem) {
        upsertFoodItemToServer(foodItem)
        foodItemDao.upsertFoodItem(foodItem)
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {
        deleteFoodItemFromServer(foodItem.id!!)
        foodItemDao.deleteFoodItem(foodItem)
    }

    override suspend fun getAllFoodItems(): Flow<List<FoodItem>> {
        refreshFoodItems()
        return foodItemDao.getAllFoodItems()
    }

    override suspend fun getFoodItemById(id: Int): FoodItem? {
        refreshFoodItems().await()
        return foodItemDao.getFoodItemById(id)
    }

    override suspend fun getFoodItemByIdFlow(id: Int): Flow<FoodItem?> {
        refreshFoodItems()
        return foodItemDao.getFoodItemByIdFlow(id)
    }

    suspend fun refreshFoodItems() = coroutineScope {
        async {
            val foodItems = getAllFoodItemsFromServer()
            foodItemDao.upsertAll(foodItems)
        }
    }
}
