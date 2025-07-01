package hu.blu3berry.sunny.database.di

import hu.blu3berry.sunny.database.FoodDatabase
import hu.blu3berry.sunny.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

expect val DatabaseModule: Module

val DatabaseSharedModule = module {
   single { getRoomDatabase(get()) } bind FoodDatabase::class
}