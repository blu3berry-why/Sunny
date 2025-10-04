package hu.blu3berry.sunny.features.food.data.remote

import hu.blu3berry.sunny.features.food.domain.model.FoodItem

/**
 * Interface for remote food data operations.
 * This interface defines methods for interacting with a remote data source for food items.
 */
interface RemoteFoodDataSource {
    /**
     * Upserts (inserts or updates) a food item in the remote data source.
     * @param foodItem The food item to upsert
     * @return The upserted food item, potentially with server-assigned values
     */
    suspend fun upsertFoodItem(foodItem: FoodItem): FoodItem

    /**
     * Retrieves all food items from the remote data source.
     * @return A list of all food items
     */
    suspend fun getAllFoodItems(): List<FoodItem>

    /**
     * Deletes a food item from the remote data source by its ID.
     * @param foodItemId The ID of the food item to delete
     */
    suspend fun deleteFoodItem(foodItemId: Int)
}