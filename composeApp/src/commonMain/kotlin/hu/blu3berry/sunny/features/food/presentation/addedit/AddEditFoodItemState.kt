@file:OptIn(ExperimentalUuidApi::class)

package hu.blu3berry.sunny.features.food.presentation.addedit

import hu.blu3berry.sunny.core.presentation.UiText
import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.model.Quantity
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi

data class AddEditFoodItemState(
    val name: String = "",
    val category: FoodCategory = FoodCategory.OTHER,
    val amount: Double = 1.0,
    val unit: UnitOfMeasure = UnitOfMeasure.PIECE,
    val expirationDate: LocalDate? = null,
    val location: StorageLocation = StorageLocation.PANTRY,
    val notes: String? = "",
    val modalDatePickerVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null
)

fun AddEditFoodItemState.toFoodItem(id : Int? = null): FoodItem {
    return FoodItem(
        id = id,
        userId = null,
        name = name,
        category = category,
        quantity = Quantity(amount, unit),
        expirationDate = expirationDate,
        location = location,
        notes = notes
    )
}

fun UpdateStateByFoodItem(foodItem: FoodItem): AddEditFoodItemState {
    return AddEditFoodItemState(
        name = foodItem.name,
        category = foodItem.category,
        amount = foodItem.quantity.amount,
        unit = foodItem.quantity.unit,
        expirationDate = foodItem.expirationDate,
        location = foodItem.location,
        notes = foodItem.notes
    )
}


