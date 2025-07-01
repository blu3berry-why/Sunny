package hu.blu3berry.sunny.features.food.domain.model

data class ShoppingListItem(
    val id: Int? = null,
    val name: String,
    val expectedAmount: Quantity,
    val reason: MissingReason
)
