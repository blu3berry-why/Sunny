package hu.blu3berry.sunny.features.food.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate


@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val category: FoodCategory,
    val quantity: Quantity,
    val expirationDate: LocalDate? = null,
    val location: StorageLocation = StorageLocation.PANTRY,
    val barcode: String? = null,
    val notes: String? = null
)
