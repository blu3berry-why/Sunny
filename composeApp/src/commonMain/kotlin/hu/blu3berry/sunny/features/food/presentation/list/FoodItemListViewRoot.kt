package hu.blu3berry.sunny.features.food.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.blu3berry.sunny.core.presentation.UiText
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FoodItemListViewRoot(
    viewModel: FoodItemListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.statusBarsPadding()
    ) {
        FoodItemListView(
            state = state,
            onAction = viewModel::onAction
        )
    }


}

@Composable
fun FoodItemListView(
    state: FoodItemListState,
    onAction: (FoodItemListAction) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { onAction(FoodItemListAction.OnSearchQueryChanged(it)) },
                label = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Food items list
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error.asString(),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                state.foodItems.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.searchQuery.isBlank())
                                UiText.DynamicString("No food items found").asString()
                            else
                                UiText.DynamicString("No results for '${state.searchQuery}'")
                                    .asString(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                else -> {
                    LazyColumn {
                        items(state.foodItems) { foodItem ->
                            FoodItemCard(
                                foodItem = foodItem,
                                onClick = { onAction(FoodItemListAction.OnFoodItemClicked(it)) },
                                onDeleteClick = { onAction(FoodItemListAction.OnDeleteFoodItemClick(it)) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { onAction(FoodItemListAction.OnNewFoodItemClick) },

            ) {
            Icon(Icons.Default.Add, contentDescription = "Add Food Item")
        }
    }
}

@Composable
fun FoodItemCard(
    foodItem: FoodItem,
    onClick: (FoodItem) -> Unit = {},
    onDeleteClick: (FoodItem) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onClick(foodItem) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = foodItem.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row {
                    Text(
                        text = "Quantity: ${foodItem.quantity.amount} ${foodItem.quantity.unit}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Category: ${foodItem.category}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Location: ${foodItem.location}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = { onDeleteClick(foodItem) }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Food Item",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview
@Composable
fun FoodItemCardPreview() {
    FoodItemCard(
        foodItem = FoodItem(
            id = 1,
            name = "Milk",
            category = hu.blu3berry.sunny.features.food.domain.model.FoodCategory.DAIRY,
            quantity = hu.blu3berry.sunny.features.food.domain.model.Quantity(
                2.0,
                hu.blu3berry.sunny.features.food.domain.model.UnitOfMeasure.LITER
            ),
            expirationDate = null,
            location = hu.blu3berry.sunny.features.food.domain.model.StorageLocation.FRIDGE,
            notes = "Some notes",
            userId = null,
        )
    )
}
