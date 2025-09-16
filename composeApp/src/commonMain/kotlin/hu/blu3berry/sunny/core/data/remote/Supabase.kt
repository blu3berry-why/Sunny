package hu.blu3berry.sunny.core.data.remote

import hu.blu3berry.sunny.features.food.data.mapper.toDomain
import hu.blu3berry.sunny.features.food.data.mapper.toDto
import hu.blu3berry.sunny.features.food.data.remote.FoodItemDto
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.serializer.KotlinXSerializer
import kotlinx.serialization.json.Json
import kotlin.getValue

expect val SUPABASE_URL: String

expect val SUPABASE_KEY: String

object Supabase {
    // set these from BuildConfig / secure storage

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_KEY,
        ){
            defaultSerializer = KotlinXSerializer(
                Json {
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                }
            )

            // IMPORTANT: install Postgrest so `client.from(...)` is available at runtime
            install(Postgrest)
        }
    }
}

suspend fun upsertFoodItemToServer(foodItem: FoodItem)
        : FoodItem {
    val dto = foodItem.toDto()
    val supabase = Supabase.client
    // upsert and return the inserted/updated row
    val response = supabase.from("food_items")
        .upsert(dto) {
            // optionally specify onConflict if you use unique constraint
            // onConflict = "id"
            select()
        }.decodeSingle<FoodItemDto>() // decodeSingle returns the first row

    // convert back to domain and return (including server-assigned id)
    return response.toDomain()
}

suspend fun getAllFoodItemsFromServer(): List<FoodItem> {
    return Supabase.client
        .from("food_items")
        .select()                     // filter by current user's UUID
        .decodeList<FoodItemDto>()    // decode to DTO list
        .map { it.toDomain() }        // convert to domain model
}


suspend fun deleteFoodItemFromServer(foodItemId: Int) {
    val response = Supabase.client.postgrest
        .from("food_items")
        .delete {
            filter{
                eq("id", foodItemId)
            }
        }
}

