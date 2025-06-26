package hu.blu3berry.sunny.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlin.jvm.java

fun getFoodDatabase(context: Context) : FoodDatabase {
    val dbFile = context.getDatabasePath("food.db")
    return Room.databaseBuilder<FoodDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).build()
}