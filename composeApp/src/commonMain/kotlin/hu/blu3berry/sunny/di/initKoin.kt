package hu.blu3berry.sunny.di

import hu.blu3berry.sunny.database.di.DatabaseModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            DatabaseModule
        )
    }
}