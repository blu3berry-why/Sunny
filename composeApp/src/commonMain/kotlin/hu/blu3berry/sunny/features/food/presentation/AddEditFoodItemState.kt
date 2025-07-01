package hu.blu3berry.sunny.features.food.presentation

import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.model.Quantity
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import kotlinx.datetime.LocalDate

data class AddEditFoodItemState(
    val name: String = "",
    val category: FoodCategory = FoodCategory.OTHER,
    val amount: Double = 1.0,
    val unit: UnitOfMeasure = UnitOfMeasure.PIECE,
    val expirationDate: LocalDate? = null,
    val location: StorageLocation = StorageLocation.PANTRY,
    val notes: String = ""
)

fun AddEditFoodItemState.toFoodItem(): FoodItem {
    return FoodItem(
        name = name,
        category = category,
        quantity = Quantity(amount, unit),
        expirationDate = expirationDate,
        location = location,
        notes = notes
    )
}
