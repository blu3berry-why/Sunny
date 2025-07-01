package hu.blu3berry.sunny.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlin.jvm.java

fun getFoodDatabase(context: Context) : FoodDatabase {
    val dbFile = context.getDatabasePath("food.db")
    return Room.databaseBuilder<FoodDatabase>(
        context = context.applicationContext,
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).build()
}

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<FoodDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("food.db")
    return Room.databaseBuilder<FoodDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}