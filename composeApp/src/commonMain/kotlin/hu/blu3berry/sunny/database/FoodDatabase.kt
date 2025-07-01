package hu.blu3berry.sunny.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import hu.blu3berry.sunny.database.converters.FoodTypeConverters
import hu.blu3berry.sunny.features.food.data.database.FoodItemDao
import hu.blu3berry.sunny.features.food.domain.model.FoodItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [FoodItem::class],
    version = 2,
)
@TypeConverters(FoodTypeConverters::class)
@ConstructedBy(FoodDatabaseConstructor::class)
abstract class FoodDatabase : RoomDatabase(){
    abstract fun foodItemDao(): FoodItemDao
}


@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object FoodDatabaseConstructor : RoomDatabaseConstructor<FoodDatabase> {
    override fun initialize(): FoodDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<FoodDatabase>
): FoodDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}