package hu.blu3berry.sunny.features.food.data.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class FoodItemDto(
    val id: Int? = null,
    val user_id: String, // UUID as string
    val name: String,
    val category: String,
    val quantity_amount: Double,
    val quantity_unit: String,
    @Contextual
    val expiration_date: LocalDate? = null,
    val location: String,
    val barcode: String? = null,
    val notes: String? = null,
    val created_at: String? = null,
    val updated_at: String? = null
)