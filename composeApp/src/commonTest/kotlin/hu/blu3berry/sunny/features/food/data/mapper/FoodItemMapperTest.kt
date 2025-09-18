package hu.blu3berry.sunny.features.food.data.mapper

import hu.blu3berry.sunny.features.food.data.remote.FoodItemDto
import hu.blu3berry.sunny.features.food.domain.model.FoodCategory
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import hu.blu3berry.sunny.features.food.domain.model.Quantity
import hu.blu3berry.sunny.features.food.domain.model.StorageLocation
import hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


class FoodItemMapperTest {


    @Test
    fun `domain to dto mapping works correctly`() {
        val domain = FoodItem(
            id = 123,
            userId = "123e4567-e89b-12d3-a456-426614174000",
            name = "Alma",
            category = FoodCategory.FRUIT,
            quantity = Quantity(5.0, UnitOfMeasure.PIECE),
            expirationDate = LocalDate.parse("2025-12-31"),
            location = StorageLocation.FRIDGE,
            barcode = "1234567890123",
            notes = "Bio alma"
        )

        val dto = domain.toDto()

        assertEquals(domain.id, dto.id)
        assertEquals(domain.userId, dto.user_id)
        assertEquals(domain.name, dto.name)
        assertEquals(domain.category.name, dto.category)
        assertEquals(domain.quantity.amount, dto.quantity_amount)
        assertEquals(domain.quantity.unit.name, dto.quantity_unit)
        assertEquals(domain.expirationDate, dto.expiration_date)
        assertEquals(domain.location.name, dto.location)
        assertEquals(domain.barcode, dto.barcode)
        assertEquals(domain.notes, dto.notes)
    }

    @Test
    fun `dto to domain mapping works correctly`() {
        val dto = FoodItemDto(
            id = 456,
            user_id = "123e4567-e89b-12d3-a456-426614174111",
            name = "Banán",
            category = "FRUIT",
            quantity_amount = 3.0,
            quantity_unit = "PIECE",
            expiration_date = LocalDate.parse("2025-10-01"),
            location = "FRIDGE",
            barcode = "9876543210987",
            notes = "Érett banán"
        )

        val domain = dto.toDomain()

        assertEquals(dto.id, domain.id)
        assertNotNull(domain.userId)
        assertEquals(dto.user_id, domain.userId)
        assertEquals(dto.name, domain.name)
        assertEquals(FoodCategory.valueOf(dto.category), domain.category)
        assertEquals(dto.quantity_amount, domain.quantity.amount)
        assertEquals(UnitOfMeasure.valueOf(dto.quantity_unit), domain.quantity.unit)
        assertEquals(dto.expiration_date, domain.expirationDate)
        assertEquals(StorageLocation.valueOf(dto.location), domain.location)
        assertEquals(dto.barcode, domain.barcode)
        assertEquals(dto.notes, domain.notes)
    }
}

