package hu.blu3berry.sunny.database.di

import hu.blu3berry.sunny.database.getFoodDatabase
import org.koin.dsl.module

actual val DatabaseModule = module {
    single { getFoodDatabase() }
}