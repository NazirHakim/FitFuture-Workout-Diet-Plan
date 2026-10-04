package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  fun getUserProfile(): Flow<UserProfile?>

  @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
  suspend fun getUserProfileOnce(): UserProfile?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateProfile(profile: UserProfile)

  @Query("DELETE FROM user_profile")
  suspend fun deleteAllUserData()
}

@Dao
interface WorkoutDao {
  @Query("SELECT * FROM exercises")
  fun getAllExercises(): Flow<List<Exercise>>

  @Query("SELECT * FROM exercises WHERE category = :category")
  fun getExercisesByCategory(category: String): Flow<List<Exercise>>

  @Query("SELECT * FROM exercises WHERE id = :id LIMIT 1")
  suspend fun getExerciseById(id: String): Exercise?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertExercises(exercises: List<Exercise>)

  @Query("SELECT * FROM workout_logs ORDER BY dateTimestamp DESC")
  fun getAllWorkoutLogs(): Flow<List<WorkoutLog>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertWorkoutLog(log: WorkoutLog): Long

  @Query("SELECT * FROM exercise_prs")
  fun getAllPRs(): Flow<List<PersonalRecord>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun savePR(pr: PersonalRecord)
}

@Dao
interface CheckInDao {
  @Query("SELECT * FROM daily_checkins ORDER BY dateString DESC")
  fun getAllCheckIns(): Flow<List<DailyCheckIn>>

  @Query("SELECT * FROM daily_checkins WHERE dateString = :dateString LIMIT 1")
  suspend fun getCheckInForDate(dateString: String): DailyCheckIn?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdateCheckIn(checkIn: DailyCheckIn)
}

@Dao
interface NutritionDao {
  @Query("SELECT * FROM meal_items")
  fun getAllMeals(): Flow<List<MealPlanItem>>

  @Query("SELECT * FROM meal_items WHERE mealType = :mealType")
  fun getMealsByType(mealType: String): Flow<List<MealPlanItem>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMeals(meals: List<MealPlanItem>)
}

@Dao
interface ChatDao {
  @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
  fun getAllMessages(): Flow<List<ChatMessage>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMessage(message: ChatMessage): Long

  @Query("DELETE FROM chat_messages")
  suspend fun clearHistory()
}

@Dao
interface AchievementDao {
  @Query("SELECT * FROM achievements")
  fun getAllAchievements(): Flow<List<Achievement>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAchievements(achievements: List<Achievement>)

  @Query("UPDATE achievements SET isUnlocked = 1, unlockedDate = :dateStr WHERE id = :id")
  suspend fun unlockAchievement(id: String, dateStr: String)

  @Query("SELECT * FROM challenges")
  fun getAllChallenges(): Flow<List<Challenge>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChallenges(challenges: List<Challenge>)

  @Update
  suspend fun updateChallenge(challenge: Challenge)
}
