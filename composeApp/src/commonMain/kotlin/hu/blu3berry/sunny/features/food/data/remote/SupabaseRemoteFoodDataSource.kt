package hu.blu3berry.sunny.features.food.data.remote

import hu.blu3berry.sunny.features.food.data.mapper.toDomain
import hu.blu3berry.sunny.features.food.data.mapper.toDto
import hu.blu3berry.sunny.features.food.data.model.FoodItemDto
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest

/**
 * Supabase implementation of the RemoteFoodDataSource interface.
 * This class handles remote food data operations using Supabase.
 *
 * @property client The SupabaseClient instance for making API calls
 */
class SupabaseRemoteFoodDataSource(
    private val client: SupabaseClient
) : RemoteFoodDataSource {

    override suspend fun upsertFoodItem(foodItem: FoodItem): FoodItem {
        val dto = foodItem.toDto()
        // upsert and return the inserted/updated row
        val response = client.from("food_items")
            .upsert(dto) {
                select()
            }.decodeSingle<FoodItemDto>() // decodeSingle returns the first row

        // convert back to domain and return (including server-assigned id)
        return response.toDomain()
    }

    override suspend fun getAllFoodItems(): List<FoodItem> {
        return try {
            client.from("food_items")
                .select()
                .decodeList<FoodItemDto>()
                .map { it.toDomain() }
        } catch (e: Exception) {
            throw Exception("Failed to fetch food items", e)
        }
    }

    override suspend fun deleteFoodItem(foodItemId: Int) {
        client.postgrest
            .from("food_items")
            .delete {
                filter {
                    eq("id", foodItemId)
                }
            }
    }
}