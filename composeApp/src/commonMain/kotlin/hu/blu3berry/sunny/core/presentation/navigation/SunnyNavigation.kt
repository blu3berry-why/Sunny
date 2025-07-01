package hu.blu3berry.sunny.core.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import hu.blu3berry.sunny.features.food.presentation.AddEditFoodItemViewRoot
import hu.blu3berry.sunny.features.food.presentation.AddFoodItemViewModel
import hu.blu3berry.sunny.features.food.presentation.EditFoodItemViewModel
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewModel
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewRoot
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SunnyNavigation(modifier : Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.FoodList,
        modifier = modifier.fillMaxSize()
    ) {
        composable<Route.FoodList> {
            val foodListViewModel = koinViewModel<FoodItemListViewModel>()
            foodListViewModel.navigationEventsChannelFlow.OnEvent {
                when (it) {
                    is FoodItemListViewModel.NavigationAction.OnFoodItemClick -> {
                        navController.navigate(Route.EditFoodItem(it.foodItemId))
                    }

                    is FoodItemListViewModel.NavigationAction.OnNewFoodItemClick -> {
                        navController.navigate(Route.AddFoodItem)
                    }
                }
            }
            FoodItemListViewRoot(
                viewModel = foodListViewModel
            )
        }

        composable<Route.AddFoodItem> {
            val addFoodItemViewModel = koinViewModel<AddFoodItemViewModel>()
            addFoodItemViewModel.navigationEventsChannelFlow.OnEvent {
                // both actions do the same thing, so we can handle them together
                navController.navigate(Route.FoodList)
            }
            AddEditFoodItemViewRoot(
                viewModel = addFoodItemViewModel
            )
        }

        composable<Route.EditFoodItem> {
            val id = it.toRoute<Route.EditFoodItem>().id
            val editFoodItemViewModel = koinViewModel<EditFoodItemViewModel>(parameters = { parametersOf(id) })
            editFoodItemViewModel.navigationEventsChannelFlow.OnEvent {
                // both actions do the same thing, so we can handle them together
                navController.navigate(Route.FoodList)
            }
            AddEditFoodItemViewRoot(
                viewModel = editFoodItemViewModel
            )
        }

    }
}

