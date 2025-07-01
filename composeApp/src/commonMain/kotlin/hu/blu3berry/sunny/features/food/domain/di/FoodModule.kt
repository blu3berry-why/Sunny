package hu.blu3berry.sunny.features.food.domain.di

import androidx.lifecycle.viewmodel.compose.viewModel
import hu.blu3berry.sunny.features.food.data.repository.FoodRepositoryImpl
import hu.blu3berry.sunny.features.food.domain.repository.FoodRepository
import hu.blu3berry.sunny.features.food.domain.usecase.GetFoodItemByIdUseCase
import hu.blu3berry.sunny.features.food.domain.usecase.SaveFoodItemUseCase
import hu.blu3berry.sunny.features.food.domain.usecase.UpdateFoodItemUseCase
import hu.blu3berry.sunny.features.food.presentation.AddEditFoodItemViewModel
import hu.blu3berry.sunny.features.food.presentation.AddFoodItemViewModel
import hu.blu3berry.sunny.features.food.presentation.EditFoodItemViewModel
import hu.blu3berry.sunny.features.food.presentation.FoodItemListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val FoodSharedModule = module {
    // Data layer
    single { get<hu.blu3berry.sunny.database.FoodDatabase>().foodItemDao() }
    singleOf(::FoodRepositoryImpl) bind FoodRepository::class

    // Domain layer
    factoryOf(::SaveFoodItemUseCase)
    factoryOf(::GetFoodItemByIdUseCase)
    factoryOf(::UpdateFoodItemUseCase)

    // Presentation layer
    viewModelOf(::FoodItemListViewModel)
    viewModel { (id: Int) ->
        EditFoodItemViewModel(
            id = id,
            getFoodItemUseCase = get(),
            updateFoodItemUseCase = get(),
        )
    }
    viewModelOf(::AddFoodItemViewModel)
}
