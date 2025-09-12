package hu.blu3berry.sunny.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<FoodDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("food.db")
    return Room.databaseBuilder<FoodDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}