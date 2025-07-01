package hu.blu3berry.sunny.features.food.domain.di

import androidx.lifecycle.viewmodel.compose.viewModel
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module



val FoodSharedModule = module {
    viewModelOf(::FoodItemListViewModel)
}