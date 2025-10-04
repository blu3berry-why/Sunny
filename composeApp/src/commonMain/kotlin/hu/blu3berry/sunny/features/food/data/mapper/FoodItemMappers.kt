package hu.blu3berry.sunny.features.food.data.mapper

import hu.blu3berry.sunny.features.food.data.model.FoodItemDto
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import kotlin.uuid.ExperimentalUuidApi

/*
 TODO: Be prepared to handle IllegalArgumentException when using valueOf if server data contains unexpected enum strings — validate or fallback if necessary.
 */

// from domain to dto

@OptIn(ExperimentalUuidApi::class)
fun FoodItem.toDto(): FoodItemDto {
    return FoodItemDto(
        id = this.id,
        user_id = this.userId?.toString() ?: "",
        name = this.name,
        category = this.category.name,
        quantity_amount = this.quantity.amount,
        quantity_unit = this.quantity.unit.name,
        expiration_date = this.expirationDate,
        location = this.location.name,
        barcode = this.barcode,
        notes = this.notes
    )
}

// from dto to domain (note: dto.id is Int?)
@OptIn(ExperimentalUuidApi::class)
fun FoodItemDto.toDomain(): hu.blu3berry.sunny.features.food.domain.model.FoodItem {
    val qty = hu.blu3berry.sunny.features.food.domain.model.Quantity(
        amount = this.quantity_amount,
        unit = hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure.valueOf(this.quantity_unit)
    )
    return hu.blu3berry.sunny.features.food.domain.model.FoodItem(
        id = this.id,
        userId = user_id,
        name = this.name,
        category = hu.blu3berry.sunny.features.food.domain.model.FoodCategory.valueOf(this.category),
        quantity = qty,
        expirationDate = this.expiration_date,
        location = hu.blu3berry.sunny.features.food.domain.model.StorageLocation.valueOf(this.location),
        barcode = this.barcode,
        notes = this.notes
    )
}
