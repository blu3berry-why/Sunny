package hu.blu3berry.sunny.features.food.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodItemDao {
    @Upsert
    suspend fun upsertFoodItem(foodItem: FoodItem)

    @Delete
    suspend fun deleteFoodItem(foodItem: FoodItem)

    @Query("SELECT * FROM food_items")
    fun getAllFoodItems(): Flow<List<FoodItem>>
}
