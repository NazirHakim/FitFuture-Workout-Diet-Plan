package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.*

@Database(
  entities = [
    UserProfile::class,
    Exercise::class,
    WorkoutLog::class,
    PersonalRecord::class,
    DailyCheckIn::class,
    MealPlanItem::class,
    ChatMessage::class,
    Achievement::class,
    Challenge::class
  ],
  version = 1,
  exportSchema = false
)
@TypeConverters(IronPulseTypeConverters::class)
abstract class IronPulseDatabase : RoomDatabase() {
  abstract fun userDao(): UserDao
  abstract fun workoutDao(): WorkoutDao
  abstract fun checkInDao(): CheckInDao
  abstract fun nutritionDao(): NutritionDao
  abstract fun chatDao(): ChatDao
  abstract fun achievementDao(): AchievementDao

  companion object {
    @Volatile
    private var INSTANCE: IronPulseDatabase? = null

    fun getDatabase(context: Context): IronPulseDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          IronPulseDatabase::class.java,
          "ironpulse_database.db"
        )
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
