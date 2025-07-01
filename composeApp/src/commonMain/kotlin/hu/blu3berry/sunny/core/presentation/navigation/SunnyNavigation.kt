package hu.blu3berry.sunny.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import hu.blu3berry.sunny.features.food.presentation.AddEditFoodItemViewRoot
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewModel
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewRoot
import kotlinx.coroutines.flow.consumeAsFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SunnyNavigation() {
    val navController = rememberNavController()
   FoodItemListViewRoot()
    NavHost(
        navController = navController,
        startDestination = Route.FoodList
    ){
        composable<Route.FoodList> {
            val foodListViewModel = koinViewModel<FoodItemListViewModel>()
            foodListViewModel.navigationEventsChannelFlow.asEvents {
                when(it){
                    is FoodItemListViewModel.NavigationAction.OnFoodItemClick -> {
                        navController.navigate(Route.EditFoodItem(it.foodItemId))
                    }
                }
            }

        }
    }
}
