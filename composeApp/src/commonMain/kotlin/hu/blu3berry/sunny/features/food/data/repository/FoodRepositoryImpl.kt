package hu.blu3berry.sunny.features.food.data.repository

import hu.blu3berry.sunny.features.food.data.database.FoodItemDao
import hu.blu3berry.sunny.features.food.data.remote.RemoteFoodDataSource
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the FoodRepository interface.
 * This class is in the data layer and knows about the data sources.
 */
class FoodRepositoryImpl(
    private val foodItemDao: FoodItemDao,
    private val remoteFoodDataSource: RemoteFoodDataSource
) : FoodRepository {

    override suspend fun saveFoodItem(foodItem: FoodItem) {
        val updatedFoodItem = remoteFoodDataSource.upsertFoodItem(foodItem)
        foodItemDao.upsertFoodItem(updatedFoodItem)
    }

    override suspend fun deleteFoodItem(foodItem: FoodItem) {
        foodItem.id?.let { id ->
            remoteFoodDataSource.deleteFoodItem(id)
            foodItemDao.deleteFoodItem(foodItem)
        }
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

    override fun getAllFoodItemsAsFlow(): Flow<List<FoodItem>> {
        return foodItemDao.getAllFoodItems()
    }

    suspend fun refreshFoodItems() = coroutineScope {
        async {
            val foodItems = remoteFoodDataSource.getAllFoodItems()
            foodItemDao.upsertAll(foodItems)
        }
    }
}
