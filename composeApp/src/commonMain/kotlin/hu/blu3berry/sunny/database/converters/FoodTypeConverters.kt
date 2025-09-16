package hu.blu3berry.sunny.database.converters

import androidx.room.TypeConverter
import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.Quantity
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Type converters for Room database to convert between custom types and database types.
 */
class FoodTypeConverters {
    // FoodCategory converters
    @TypeConverter
    fun fromFoodCategory(value: FoodCategory): String {
        return value.name
    }

    @TypeConverter
    fun toFoodCategory(value: String): FoodCategory {
        return FoodCategory.valueOf(value)
    }

    // StorageLocation converters
    @TypeConverter
    fun fromStorageLocation(value: StorageLocation): String {
        return value.name
    }

    @TypeConverter
    fun toStorageLocation(value: String): StorageLocation {
        return StorageLocation.valueOf(value)
    }

    // UnitOfMeasure converters
    @TypeConverter
    fun fromUnitOfMeasure(value: UnitOfMeasure): String {
        return value.name
    }

    @TypeConverter
    fun toUnitOfMeasure(value: String): UnitOfMeasure {
        return UnitOfMeasure.valueOf(value)
    }

    // Quantity converters
    @TypeConverter
    fun fromQuantity(quantity: Quantity): String {
        return "${quantity.amount}:${quantity.unit.name}"
    }

    @TypeConverter
    fun toQuantity(value: String): Quantity {
        val parts = value.split(":")
        return Quantity(
            amount = parts[0].toDouble(),
            unit = UnitOfMeasure.valueOf(parts[1])
        )
    }

    // LocalDate converters
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    // Uuid converters
    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun fromUuid(uuid: Uuid?): String? = uuid?.toString()

    @OptIn(ExperimentalUuidApi::class)
    @TypeConverter
    fun toUuid(uuid: String?): Uuid? = uuid?.let { Uuid.parse(it) }


}