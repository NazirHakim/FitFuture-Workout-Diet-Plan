package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.*

class IronPulseTypeConverters {
  @TypeConverter
  fun fromGender(value: Gender?): String = value?.name ?: Gender.MALE.name

  @TypeConverter
  fun toGender(value: String?): Gender = runCatching { Gender.valueOf(value ?: "") }.getOrDefault(Gender.MALE)

  @TypeConverter
  fun fromGoal(value: FitnessGoal?): String = value?.name ?: FitnessGoal.BUILD_MUSCLE.name

  @TypeConverter
  fun toGoal(value: String?): FitnessGoal = runCatching { FitnessGoal.valueOf(value ?: "") }.getOrDefault(FitnessGoal.BUILD_MUSCLE)

  @TypeConverter
  fun fromLocation(value: WorkoutLocation?): String = value?.name ?: WorkoutLocation.GYM.name

  @TypeConverter
  fun toLocation(value: String?): WorkoutLocation = runCatching { WorkoutLocation.valueOf(value ?: "") }.getOrDefault(WorkoutLocation.GYM)

  @TypeConverter
  fun fromDiet(value: DietType?): String = value?.name ?: DietType.NON_VEGETARIAN.name

  @TypeConverter
  fun toDiet(value: String?): DietType = runCatching { DietType.valueOf(value ?: "") }.getOrDefault(DietType.NON_VEGETARIAN)

  @TypeConverter
  fun fromActivity(value: ActivityLevel?): String = value?.name ?: ActivityLevel.MODERATELY_ACTIVE.name

  @TypeConverter
  fun toActivity(value: String?): ActivityLevel = runCatching { ActivityLevel.valueOf(value ?: "") }.getOrDefault(ActivityLevel.MODERATELY_ACTIVE)
}
