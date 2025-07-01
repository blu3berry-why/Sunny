package hu.blu3berry.sunny.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

fun getFoodDatabase(): FoodDatabase {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "food.db")
        return Room.databaseBuilder<FoodDatabase>(
            name = dbFile.absolutePath,
        )
            .setDriver(BundledSQLiteDriver())
            .build()
}

fun getDatabaseBuilder(): RoomDatabase.Builder<FoodDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")
    return Room.databaseBuilder<FoodDatabase>(
        name = dbFile.absolutePath,
    )
}