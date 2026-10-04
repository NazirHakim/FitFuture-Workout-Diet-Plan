package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Gender { MALE, FEMALE, OTHER }
enum class FitnessGoal { LOSE_WEIGHT, BUILD_MUSCLE, GAIN_WEIGHT, STAY_FIT }
enum class WorkoutLocation { GYM, HOME_EQUIPMENT, HOME_NO_EQUIPMENT }
enum class DietType { VEGETARIAN, NON_VEGETARIAN, VEGAN, EGGETARIAN, KETO, MEDITERRANEAN }
enum class ActivityLevel { SEDENTARY, LIGHTLY_ACTIVE, MODERATELY_ACTIVE, VERY_ACTIVE }

@Entity(tableName = "user_profile")
data class UserProfile(
  @PrimaryKey val id: Int = 1,
  val name: String = "Athlete",
  val email: String = "athlete@ironpulse.fit",
  val age: Int = 26,
  val gender: Gender = Gender.MALE,
  val heightCm: Float = 178f,
  val currentWeightKg: Float = 78f,
  val targetWeightKg: Float = 72f,
  val dietType: DietType = DietType.NON_VEGETARIAN,
  val allergies: String = "None",
  val mealsPerDay: Int = 4,
  val waterTargetMl: Int = 3000,
  val junkFoodFrequency: String = "Rarely (1-2x/month)",
  val activityLevel: ActivityLevel = ActivityLevel.MODERATELY_ACTIVE,
  val sleepHours: Float = 7.5f,
  val injuriesLimitations: String = "None",
  val workoutLocation: WorkoutLocation = WorkoutLocation.GYM,
  val goal: FitnessGoal = FitnessGoal.BUILD_MUSCLE,
  val daysPerWeek: Int = 5,
  val whyStartedText: String = "I want to feel energized, strong, and live my healthiest life.",
  val whyStartedPreset: String = "Build Confidence & Strength",
  val hasUploadedPhoto: Boolean = false,
  val photoFrontUri: String? = null,
  val photoSideUri: String? = null,
  val photoBackUri: String? = null,
  val detailedPlanUnlocked: Boolean = false,
  val isOnboarded: Boolean = true,
  val currentStreak: Int = 5,
  val bestStreak: Int = 14,
  val totalWorkoutsDone: Int = 18,
  val xpPoints: Int = 850,
  val userLevel: Int = 3,
  val useMetricUnits: Boolean = true,
  val notificationsEnabled: Boolean = true,
  val notificationTime: String = "09:00 AM"
)

@Entity(tableName = "exercises")
data class Exercise(
  @PrimaryKey val id: String,
  val name: String,
  val category: String, // Chest, Back, Legs, Shoulders, Arms, Core, Full Body
  val primaryMuscle: String,
  val secondaryMuscles: String,
  val location: WorkoutLocation,
  val defaultSets: Int = 3,
  val defaultReps: String = "10-12",
  val restSeconds: Int = 60,
  val instructions: String,
  val coachingTips: String,
  val commonMistakes: String,
  val caloriesBurnPerMinute: Int = 8,
  val level: String = "Intermediate", // Beginner, Intermediate, Advanced
  val iconEmoji: String = "🏋️"
)

@Entity(tableName = "workout_logs")
data class WorkoutLog(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val workoutTitle: String,
  val dateTimestamp: Long = System.currentTimeMillis(),
  val dateString: String, // YYYY-MM-DD
  val durationSeconds: Int,
  val caloriesBurned: Int,
  val exercisesDoneCount: Int,
  val notes: String = ""
)

@Entity(tableName = "exercise_prs")
data class PersonalRecord(
  @PrimaryKey val exerciseId: String,
  val exerciseName: String,
  val maxWeightKg: Float,
  val repsAchieved: Int,
  val achievedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "daily_checkins")
data class DailyCheckIn(
  @PrimaryKey val dateString: String, // YYYY-MM-DD
  val weightKg: Float,
  val chestCm: Float? = null,
  val waistCm: Float? = null,
  val armsCm: Float? = null,
  val hipsCm: Float? = null,
  val thighsCm: Float? = null,
  val waterDrunkMl: Int = 0,
  val caloriesLogged: Int = 0,
  val workoutDone: Boolean = false,
  val photoUri: String? = null,
  val notes: String = ""
)

@Entity(tableName = "meal_items")
data class MealPlanItem(
  @PrimaryKey val id: String,
  val name: String,
  val mealType: String, // Breakfast, Lunch, Dinner, Snack
  val calories: Int,
  val proteinG: Int,
  val carbsG: Int,
  val fatG: Int,
  val ingredients: String,
  val recipe: String,
  val dietType: DietType = DietType.NON_VEGETARIAN,
  val budgetTier: String = "$$" // $, $$, $$$
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val sender: String, // "USER" or "COACH"
  val message: String,
  val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "achievements")
data class Achievement(
  @PrimaryKey val id: String,
  val title: String,
  val description: String,
  val icon: String,
  val xpReward: Int,
  val isUnlocked: Boolean = false,
  val unlockedDate: String? = null
)

@Entity(tableName = "challenges")
data class Challenge(
  @PrimaryKey val id: String,
  val title: String,
  val subtitle: String,
  val description: String,
  val targetDays: Int,
  val currentProgressDays: Int = 0,
  val xpReward: Int = 200,
  val isCompleted: Boolean = false,
  val badgeIcon: String = "🔥"
)

data class ArticleCard(
  val id: String,
  val type: String, // MUSCLE_OF_DAY, ATHLETE_OF_DAY, DAILY_TIP, NUTRITION
  val title: String,
  val subtitle: String,
  val content: String,
  val actionTag: String,
  val iconEmoji: String
)
