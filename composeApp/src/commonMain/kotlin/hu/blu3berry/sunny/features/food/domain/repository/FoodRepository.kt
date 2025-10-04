package hu.blu3berry.sunny.features.food.domain.repository

import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for food items following clean architecture principles.
 * This interface is in the domain layer and doesn't know about the data sources.
 */
interface FoodRepository {
    /**
     * Save a food item to the data source.
     * @param foodItem The food item to save
     */
    suspend fun saveFoodItem(foodItem: FoodItem)

    /**
     * Delete a food item from the data source.
     * @param foodItem The food item to delete
     */
    suspend fun deleteFoodItem(foodItem: FoodItem)

    /**
     * Get all food items from the data source.
     * @return A Flow of the list of food items
     */
    suspend fun getAllFoodItems(): Flow<List<FoodItem>>

    /**
     * Get a food item by its ID.
     * @param id The ID of the food item to retrieve
     * @return The food item if found, null otherwise
     */
    suspend fun getFoodItemById(id: Int): FoodItem?

    /**
     * Get a food item by its ID as a Flow.
     * @param id The ID of the food item to retrieve
     * @return A Flow of the food item if found, null otherwise
     */
    suspend fun getFoodItemByIdFlow(id: Int): Flow<FoodItem?>

    fun getAllFoodItemsAsFlow(): Flow<List<FoodItem>>
}
