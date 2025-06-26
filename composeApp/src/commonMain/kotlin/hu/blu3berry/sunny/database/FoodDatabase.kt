package hu.blu3berry.sunny.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hu.blu3berry.sunny.database.converters.FoodTypeConverters
import hu.blu3berry.sunny.features.food.data.database.FoodItemDao
import hu.blu3berry.sunny.features.food.domain.model.FoodItem

@Database(
    entities = [FoodItem::class],
    version = 2,
)
@TypeConverters(FoodTypeConverters::class)
abstract class FoodDatabase : RoomDatabase(){
    abstract fun foodItemDao(): FoodItemDao
}
