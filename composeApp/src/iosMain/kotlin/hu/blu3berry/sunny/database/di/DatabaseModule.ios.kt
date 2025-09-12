package hu.blu3berry.sunny.database.di

import hu.blu3berry.sunny.database.getDatabaseBuilder
import org.koin.dsl.module

actual val DatabaseModule = module {
    single { getDatabaseBuilder() }
}