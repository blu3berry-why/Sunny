package hu.blu3berry.sunny.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getFoodDatabase() : FoodDatabase {
    val dbFile = NSHomeDirectory() + "/food.db"
    return Room.databaseBuilder<FoodDatabase>(
        name = dbFile,
        factory = { FoodDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}