package hu.blu3berry.sunny.database.di

import hu.blu3berry.sunny.database.getDatabaseBuilder
import hu.blu3berry.sunny.database.getFoodDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val DatabaseModule= module {
    // This module is empty because the database is initialized in the iOS-specific code.
    // If you need to provide any dependencies related to the database, you can do so here.
    single { getDatabaseBuilder(androidContext()) }

}