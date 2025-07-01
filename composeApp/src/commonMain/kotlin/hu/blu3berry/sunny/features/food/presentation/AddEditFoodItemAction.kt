package hu.blu3berry.sunny.features.food.presentation

import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import kotlinx.datetime.LocalDate

sealed interface AddEditFoodItemAction {
    data class OnNameChanged(val value: String) : AddEditFoodItemAction
    data class OnCategoryChanged(val value: FoodCategory) : AddEditFoodItemAction
    data class OnAmountChanged(val value: Double) : AddEditFoodItemAction
    data class OnUnitChanged(val value: UnitOfMeasure) : AddEditFoodItemAction
    data class OnExpirationDateChanged(val value: LocalDate?) : AddEditFoodItemAction
    data class OnLocationChanged(val value: StorageLocation) : AddEditFoodItemAction
    data class OnNotesChanged(val value: String) : AddEditFoodItemAction
    data object OnSaveClicked : AddEditFoodItemAction
}
