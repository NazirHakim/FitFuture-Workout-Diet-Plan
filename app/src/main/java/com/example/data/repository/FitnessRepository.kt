package com.example.data.repository

import android.content.Context
import com.example.data.local.IronPulseDatabase
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FitnessRepository(context: Context) {
  private val db = IronPulseDatabase.getDatabase(context)
  private val userDao = db.userDao()
  private val workoutDao = db.workoutDao()
  private val checkInDao = db.checkInDao()
  private val nutritionDao = db.nutritionDao()
  private val chatDao = db.chatDao()
  private val achievementDao = db.achievementDao()

  val userProfile: Flow<UserProfile?> = userDao.getUserProfile()
  val allExercises: Flow<List<Exercise>> = workoutDao.getAllExercises()
  val workoutLogs: Flow<List<WorkoutLog>> = workoutDao.getAllWorkoutLogs()
  val allPRs: Flow<List<PersonalRecord>> = workoutDao.getAllPRs()
  val allCheckIns: Flow<List<DailyCheckIn>> = checkInDao.getAllCheckIns()
  val allMeals: Flow<List<MealPlanItem>> = nutritionDao.getAllMeals()
  val chatMessages: Flow<List<ChatMessage>> = chatDao.getAllMessages()
  val allAchievements: Flow<List<Achievement>> = achievementDao.getAllAchievements()
  val allChallenges: Flow<List<Challenge>> = achievementDao.getAllChallenges()

  suspend fun initializeDefaultDataIfEmpty() = withContext(Dispatchers.IO) {
    val existingProfile = userDao.getUserProfileOnce()
    if (existingProfile == null) {
      userDao.insertOrUpdateProfile(
        UserProfile(
          id = 1,
          name = "Alex Vance",
          email = "alex.vance@ironpulse.fit",
          age = 27,
          gender = Gender.MALE,
          heightCm = 180f,
          currentWeightKg = 82.5f,
          targetWeightKg = 76.0f,
          dietType = DietType.NON_VEGETARIAN,
          allergies = "None",
          mealsPerDay = 4,
          waterTargetMl = 3200,
          junkFoodFrequency = "1x every two weeks",
          activityLevel = ActivityLevel.MODERATELY_ACTIVE,
          sleepHours = 7.5f,
          injuriesLimitations = "None (ready to train)",
          workoutLocation = WorkoutLocation.GYM,
          goal = FitnessGoal.BUILD_MUSCLE,
          daysPerWeek = 5,
          whyStartedText = "I want to be in the best shape of my life, boost my stamina, and build unshakable mental discipline.",
          whyStartedPreset = "Build Unshakable Strength & Discipline",
          hasUploadedPhoto = false,
          detailedPlanUnlocked = false,
          isOnboarded = true,
          currentStreak = 6,
          bestStreak = 15,
          totalWorkoutsDone = 24,
          xpPoints = 1240,
          userLevel = 4
        )
      )

      // Seed Exercises
      workoutDao.insertExercises(getDefaultExercises())

      // Seed Meals
      nutritionDao.insertMeals(getDefaultMeals())

      // Seed Achievements
      achievementDao.insertAchievements(getDefaultAchievements())

      // Seed Challenges
      achievementDao.insertChallenges(getDefaultChallenges())

      // Seed Initial PRs
      workoutDao.savePR(PersonalRecord("bench_press", "Barbell Bench Press", 95f, 5))
      workoutDao.savePR(PersonalRecord("squat", "Barbell Back Squat", 125f, 6))
      workoutDao.savePR(PersonalRecord("deadlift", "Conventional Deadlift", 150f, 4))
      workoutDao.savePR(PersonalRecord("pull_up", "Strict Pull-Ups", 15f, 12))

      // Seed initial check-ins for charts
      val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
      val calendar = Calendar.getInstance()
      for (i in 6 downTo 0) {
        calendar.time = Date()
        calendar.add(Calendar.DAY_OF_YEAR, -i)
        val dateStr = sdf.format(calendar.time)
        checkInDao.insertOrUpdateCheckIn(
          DailyCheckIn(
            dateString = dateStr,
            weightKg = 83.5f - (6 - i) * 0.15f,
            chestCm = 104f + (6 - i) * 0.1f,
            waistCm = 86f - (6 - i) * 0.1f,
            armsCm = 37.5f + (6 - i) * 0.05f,
            hipsCm = 98f,
            thighsCm = 58.5f,
            waterDrunkMl = if (i == 0) 2250 else 3000,
            caloriesLogged = if (i == 0) 1850 else 2400,
            workoutDone = i != 2 && i != 5,
            notes = if (i == 0) "Feeling great today, crushed the chest session!" else "On track!"
          )
        )
      }

      // Seed initial chat welcome message
      chatDao.insertMessage(
        ChatMessage(
          sender = "COACH",
          message = "Welcome to IronPulse! I'm Coach Alex, your dedicated fitness & nutrition mentor. Whenever you need guidance on your lifts, meal swaps, or motivation when the grind gets tough, I'm right here with you. What are we conquering today?"
        )
      )
    }
  }

  suspend fun updateProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
    userDao.insertOrUpdateProfile(profile)
  }

  suspend fun logWorkout(title: String, durationSec: Int, calories: Int, count: Int, notes: String = ""): Long = withContext(Dispatchers.IO) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = sdf.format(Date())
    val logId = workoutDao.insertWorkoutLog(
      WorkoutLog(
        workoutTitle = title,
        dateTimestamp = System.currentTimeMillis(),
        dateString = today,
        durationSeconds = durationSec,
        caloriesBurned = calories,
        exercisesDoneCount = count,
        notes = notes
      )
    )

    // Update today check-in
    val checkIn = checkInDao.getCheckInForDate(today) ?: DailyCheckIn(
      dateString = today,
      weightKg = 82f,
      workoutDone = true
    )
    checkInDao.insertOrUpdateCheckIn(checkIn.copy(workoutDone = true))

    // Update streak and XP in profile
    val profile = userDao.getUserProfileOnce()
    if (profile != null) {
      val newXp = profile.xpPoints + 150
      val newTotal = profile.totalWorkoutsDone + 1
      val newLevel = 1 + (newXp / 300)
      userDao.insertOrUpdateProfile(
        profile.copy(
          xpPoints = newXp,
          totalWorkoutsDone = newTotal,
          userLevel = newLevel,
          currentStreak = profile.currentStreak + 1
        )
      )
    }
    logId
  }

  suspend fun logWater(amountMl: Int) = withContext(Dispatchers.IO) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = sdf.format(Date())
    val current = checkInDao.getCheckInForDate(today) ?: DailyCheckIn(
      dateString = today,
      weightKg = 82f
    )
    val updated = current.copy(waterDrunkMl = current.waterDrunkMl + amountMl)
    checkInDao.insertOrUpdateCheckIn(updated)
  }

  suspend fun logFoodCalories(calories: Int) = withContext(Dispatchers.IO) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = sdf.format(Date())
    val current = checkInDao.getCheckInForDate(today) ?: DailyCheckIn(
      dateString = today,
      weightKg = 82f
    )
    val updated = current.copy(caloriesLogged = current.caloriesLogged + calories)
    checkInDao.insertOrUpdateCheckIn(updated)
  }

  suspend fun saveCheckIn(checkIn: DailyCheckIn) = withContext(Dispatchers.IO) {
    checkInDao.insertOrUpdateCheckIn(checkIn)
    val profile = userDao.getUserProfileOnce()
    if (profile != null) {
      userDao.insertOrUpdateProfile(profile.copy(currentWeightKg = checkIn.weightKg))
    }
  }

  suspend fun recordPR(exerciseId: String, exerciseName: String, weightKg: Float, reps: Int) = withContext(Dispatchers.IO) {
    workoutDao.savePR(
      PersonalRecord(
        exerciseId = exerciseId,
        exerciseName = exerciseName,
        maxWeightKg = weightKg,
        repsAchieved = reps,
        achievedTimestamp = System.currentTimeMillis()
      )
    )
  }

  suspend fun sendChatMessage(userText: String, coachReply: String) = withContext(Dispatchers.IO) {
    chatDao.insertMessage(ChatMessage(sender = "USER", message = userText))
    chatDao.insertMessage(ChatMessage(sender = "COACH", message = coachReply))
  }

  suspend fun updateChallengeProgress(challengeId: String, incrementDays: Int) = withContext(Dispatchers.IO) {
    val challenges = achievementDao.getAllChallenges()
    // For specific challenge update
  }

  suspend fun deleteAllData() = withContext(Dispatchers.IO) {
    userDao.deleteAllUserData()
    chatDao.clearHistory()
  }

  private fun getDefaultExercises(): List<Exercise> {
    return listOf(
      Exercise(
        id = "bench_press",
        name = "Barbell Flat Bench Press",
        category = "Chest",
        primaryMuscle = "Pectoralis Major",
        secondaryMuscles = "Anterior Deltoids, Triceps Brachii",
        location = WorkoutLocation.GYM,
        defaultSets = 4,
        defaultReps = "8-10",
        restSeconds = 90,
        instructions = "Lie flat on the bench with eyes under the bar. Grip slightly wider than shoulder width. Retract your scapulae, unrack, lower slowly to mid-chest, then drive explosively through your palms.",
        coachingTips = "Drive feet hard into the floor for leg drive. Keep elbows tucked at approximately 45 degrees to protect shoulders.",
        commonMistakes = "Bouncing the bar off ribs, flaring elbows out to 90 degrees, arching lower back excessively off the bench.",
        caloriesBurnPerMinute = 9,
        level = "Intermediate",
        iconEmoji = "🏋️"
      ),
      Exercise(
        id = "incline_db_press",
        name = "Incline Dumbbell Press",
        category = "Chest",
        primaryMuscle = "Upper Pectoralis (Clavicular Head)",
        secondaryMuscles = "Front Deltoids, Triceps",
        location = WorkoutLocation.GYM,
        defaultSets = 3,
        defaultReps = "10-12",
        restSeconds = 60,
        instructions = "Set bench to 30-45 degrees. Press dumbbells straight up over upper chest, squeeze the upper pecs, then lower with control until elbows reach bench height.",
        coachingTips = "Do not set the incline too steep (>45 deg) or the load shifts entirely to front deltoids.",
        commonMistakes = "Collapsing chest at bottom, clanking dumbbells at the top.",
        caloriesBurnPerMinute = 8,
        level = "Beginner",
        iconEmoji = "💪"
      ),
      Exercise(
        id = "push_up",
        name = "Standard Push-Up",
        category = "Chest",
        primaryMuscle = "Pectorals",
        secondaryMuscles = "Core, Triceps, Anterior Deltoids",
        location = WorkoutLocation.HOME_NO_EQUIPMENT,
        defaultSets = 4,
        defaultReps = "15-20",
        restSeconds = 45,
        instructions = "Start in a high plank with hands under shoulders. Brace core, engage glutes. Lower chest until 2 inches from the floor, then push back up keeping a straight bodyline.",
        coachingTips = "Think of screwing your hands outward into the floor to lock in shoulder stability.",
        commonMistakes = "Sagging lower back, looking up straining the neck, flared elbows.",
        caloriesBurnPerMinute = 7,
        level = "Beginner",
        iconEmoji = "⚡"
      ),
      Exercise(
        id = "squat",
        name = "Barbell Back Squat",
        category = "Legs",
        primaryMuscle = "Quadriceps & Gluteus Maximus",
        secondaryMuscles = "Hamstrings, Adductors, Core Stabilizers",
        location = WorkoutLocation.GYM,
        defaultSets = 4,
        defaultReps = "6-8",
        restSeconds = 120,
        instructions = "Rest the bar on upper traps. Unrack and take two steps back. Inhale deep into your belly, hinge hips back and descend until thighs are at or below parallel. Drive up through midfoot.",
        coachingTips = "Spread the floor with your feet to engage your gluteus medius and prevent knee valgus.",
        commonMistakes = "Knees caving inwards, heel lifting off floor, rounded lumbar spine (butt wink).",
        caloriesBurnPerMinute = 12,
        level = "Advanced",
        iconEmoji = "🦵"
      ),
      Exercise(
        id = "goblet_squat",
        name = "Dumbbell Goblet Squat",
        category = "Legs",
        primaryMuscle = "Quadriceps & Core",
        secondaryMuscles = "Glutes, Upper Back",
        location = WorkoutLocation.HOME_EQUIPMENT,
        defaultSets = 3,
        defaultReps = "12-15",
        restSeconds = 60,
        instructions = "Hold one dumbbell vertically against chest with both palms. Squat down between knees, keeping torso tall and upright, then push back up.",
        coachingTips = "Elbows should point down and track inside knees at the bottom of the squat.",
        commonMistakes = "Leaning too far forward, letting dumbbell pull shoulders down.",
        caloriesBurnPerMinute = 8,
        level = "Beginner",
        iconEmoji = "🔥"
      ),
      Exercise(
        id = "deadlift",
        name = "Conventional Barbell Deadlift",
        category = "Back",
        primaryMuscle = "Erector Spinae & Hamstrings",
        secondaryMuscles = "Latissimus Dorsi, Trapezius, Glutes, Forearms",
        location = WorkoutLocation.GYM,
        defaultSets = 3,
        defaultReps = "5-5",
        restSeconds = 150,
        instructions = "Stand with feet hip-width under bar (bar over midfoot). Grip bar outside shins, pull chest tall to set your back flat, wedge hips in, then stand tall by driving the floor away.",
        coachingTips = "Engage lats as if squeezing oranges in your armpits before the bar leaves the floor.",
        commonMistakes = "Rounding thoracic/lumbar spine, hyperextending at top, bar drifting away from shins.",
        caloriesBurnPerMinute = 13,
        level = "Advanced",
        iconEmoji = "⚡"
      ),
      Exercise(
        id = "pull_up",
        name = "Strict Pull-Up",
        category = "Back",
        primaryMuscle = "Latissimus Dorsi",
        secondaryMuscles = "Biceps Brachii, Rhomboids, Lower Traps",
        location = WorkoutLocation.HOME_EQUIPMENT,
        defaultSets = 4,
        defaultReps = "8-12",
        restSeconds = 75,
        instructions = "Grip bar overhand slightly wider than shoulder width. Depress scapulae down, initiate pull by driving elbows toward your back pockets until chin clears bar.",
        coachingTips = "Avoid swinging or kipping. Full dead-hang at bottom for maximum lat elongation.",
        commonMistakes = "Kipping legs, incomplete range of motion, shrugging shoulders up.",
        caloriesBurnPerMinute = 10,
        level = "Intermediate",
        iconEmoji = "🏆"
      ),
      Exercise(
        id = "db_shoulder_press",
        name = "Seated Dumbbell Shoulder Press",
        category = "Shoulders",
        primaryMuscle = "Anterior & Lateral Deltoids",
        secondaryMuscles = "Triceps, Upper Traps",
        location = WorkoutLocation.GYM,
        defaultSets = 3,
        defaultReps = "10-12",
        restSeconds = 60,
        instructions = "Sit upright with back supported. Hold dumbbells at ear height with palms facing forward or slightly angled. Press upward smoothly until arms extend overhead.",
        coachingTips = "Keep ribcage pinned down. Don't arch lower back to turn it into an incline chest press.",
        commonMistakes = "Locking elbows violently, dropping weights below shoulders too fast.",
        caloriesBurnPerMinute = 7,
        level = "Intermediate",
        iconEmoji = "🎯"
      ),
      Exercise(
        id = "plank",
        name = "Core Stability Plank",
        category = "Core",
        primaryMuscle = "Rectus Abdominis & Transverse Abdominis",
        secondaryMuscles = "Glutes, Shoulders",
        location = WorkoutLocation.HOME_NO_EQUIPMENT,
        defaultSets = 3,
        defaultReps = "45-60s",
        restSeconds = 45,
        instructions = "Rest on forearms and toes with elbows directly below shoulders. Squeeze abs, tuck pelvis posterior, squeeze glutes and quads. Hold perfectly rigid.",
        coachingTips = "Pull forearms towards toes actively (RKC plank technique) for 3x higher muscle activation.",
        commonMistakes = "Sagging belly/hips, hiking hips like a tent, holding breath.",
        caloriesBurnPerMinute = 6,
        level = "Beginner",
        iconEmoji = "🛡️"
      ),
      Exercise(
        id = "hiit_burpees",
        name = "Cardio Burst Burpees",
        category = "Full Body",
        primaryMuscle = "Cardiovascular Endurance",
        secondaryMuscles = "Pecs, Quads, Delts, Calves",
        location = WorkoutLocation.HOME_NO_EQUIPMENT,
        defaultSets = 4,
        defaultReps = "15 reps",
        restSeconds = 45,
        instructions = "Drop hands to floor from standing, kick feet back into a push-up position, drop chest, push back up, snap feet to hands and jump vertically clapping overhead.",
        coachingTips = "Land softly on balls of feet. Maintain controlled rhythmic breathing.",
        commonMistakes = "Landing flat-footed with jarring knees, skipping the chest-to-floor phase.",
        caloriesBurnPerMinute = 14,
        level = "Intermediate",
        iconEmoji = "⚡"
      )
    )
  }

  private fun getDefaultMeals(): List<MealPlanItem> {
    return listOf(
      MealPlanItem(
        id = "meal_b1",
        name = "High-Protein Oatmeal with Blueberries & Whey",
        mealType = "Breakfast",
        calories = 460,
        proteinG = 38,
        carbsG = 52,
        fatG = 9,
        ingredients = "Rolled oats (60g), 1 scoop Vanilla Whey Protein, unsweetened almond milk (200ml), fresh blueberries (50g), chia seeds (10g).",
        recipe = "Cook oats in almond milk over medium heat for 4 minutes. Remove from heat, stir in protein powder thoroughly, top with blueberries and chia seeds.",
        dietType = DietType.NON_VEGETARIAN,
        budgetTier = "$"
      ),
      MealPlanItem(
        id = "meal_b2",
        name = "Avocado & Scrambled Egg Toast",
        mealType = "Breakfast",
        calories = 490,
        proteinG = 28,
        carbsG = 35,
        fatG = 22,
        ingredients = "2 whole eggs + 2 egg whites, whole grain sprouted toast (2 slices), 1/2 ripe avocado, chili flakes, sea salt.",
        recipe = "Toast bread. Mash avocado with salt and chili. Scramble eggs softly in olive oil spray. Layer egg over avocado toast.",
        dietType = DietType.EGGETARIAN,
        budgetTier = "$$"
      ),
      MealPlanItem(
        id = "meal_l1",
        name = "Grilled Lemon Herb Chicken & Quinoa Bowl",
        mealType = "Lunch",
        calories = 620,
        proteinG = 52,
        carbsG = 65,
        fatG = 14,
        ingredients = "Chicken breast (200g), cooked quinoa (150g), steamed broccoli (100g), roasted bell peppers, olive oil (1 tsp), fresh lemon juice.",
        recipe = "Season chicken with oregano, garlic, salt, and lemon. Grill on skillet for 6 min per side until 165°F. Serve over fluffy warm quinoa and steamed greens.",
        dietType = DietType.NON_VEGETARIAN,
        budgetTier = "$$"
      ),
      MealPlanItem(
        id = "meal_l2",
        name = "Mediterranean Chickpea & Tofu Power Bowl",
        mealType = "Lunch",
        calories = 580,
        proteinG = 34,
        carbsG = 70,
        fatG = 16,
        ingredients = "Firm tofu pan-seared (150g), chickpeas (120g), cucumber, cherry tomatoes, kalamata olives, tahini dressing (1 tbsp).",
        recipe = "Press and cube tofu, pan sear until crispy golden. Toss with seasoned chickpeas, fresh chopped veggies, and drizzle rich tahini lemon sauce.",
        dietType = DietType.VEGAN,
        budgetTier = "$"
      ),
      MealPlanItem(
        id = "meal_d1",
        name = "Atlantic Salmon with Roasted Sweet Potatoes & Asparagus",
        mealType = "Dinner",
        calories = 680,
        proteinG = 46,
        carbsG = 48,
        fatG = 26,
        ingredients = "Wild salmon fillet (180g), sweet potato cubes (180g), asparagus spears (120g), garlic butter (10g), rosemary.",
        recipe = "Roast sweet potatoes at 400°F for 20 min. Add seasoned salmon and asparagus to baking sheet for final 12 min until flaky and tender.",
        dietType = DietType.NON_VEGETARIAN,
        budgetTier = "$$$"
      ),
      MealPlanItem(
        id = "meal_d2",
        name = "Hearty Black Bean & Lentil Muscle Chili",
        mealType = "Dinner",
        calories = 560,
        proteinG = 35,
        carbsG = 82,
        fatG = 8,
        ingredients = "Brown lentils (100g), black beans (100g), crushed fire-roasted tomatoes, onions, cumin, smoked paprika, cilantro.",
        recipe = "Simmer aromatics, lentils, and beans with rich chili spices for 30 minutes until thick and savory. Garnish with lime and fresh cilantro.",
        dietType = DietType.VEGETARIAN,
        budgetTier = "$"
      ),
      MealPlanItem(
        id = "meal_s1",
        name = "Greek Yogurt Parfait with Walnuts & Honey",
        mealType = "Snack",
        calories = 270,
        proteinG = 24,
        carbsG = 22,
        fatG = 8,
        ingredients = "0% Fat Plain Greek Yogurt (200g), crushed walnuts (15g), organic honey (1 tsp), cinnamon.",
        recipe = "Spoon chilled Greek yogurt into bowl, swirl honey, sprinkle crushed walnuts and a dash of warming cinnamon.",
        dietType = DietType.VEGETARIAN,
        budgetTier = "$"
      )
    )
  }

  private fun getDefaultAchievements(): List<Achievement> {
    return listOf(
      Achievement("first_step", "First Blood", "Complete your first workout in IronPulse", "🎯", 100, true, "2026-09-28"),
      Achievement("streak_7", "Consistency King", "Maintain a 7-day workout streak", "🔥", 250, false),
      Achievement("streak_30", "Iron Discipline", "Crush 30 days of continuous workouts", "👑", 600, false),
      Achievement("century_club", "100 Club", "Complete 100 logged workouts", "💯", 1000, false),
      Achievement("pr_crusher", "PR Crusher", "Set a new Personal Record on a major lift", "⚡", 150, true, "2026-10-01"),
      Achievement("hydration_hero", "Hydration Master", "Hit your daily water goal 5 days in a row", "💧", 120, true, "2026-10-02"),
      Achievement("clean_fuel", "Clean Fuel", "Hit your protein macro target for 7 consecutive days", "🥑", 200, false)
    )
  }

  private fun getDefaultChallenges(): List<Challenge> {
    return listOf(
      Challenge(
        id = "bring_sally_up",
        title = "Bring Sally Up Squat Challenge",
        subtitle = "3 min 30 sec test of quad & glute endurance",
        description = "Play 'Flower' by Moby. Lower into a squat on 'Bring Sally Down' and stand up on 'Bring Sally Up'. Can you survive the full 30 reps without dropping?",
        targetDays = 1,
        currentProgressDays = 0,
        xpReward = 300,
        badgeIcon = "🏋️"
      ),
      Challenge(
        id = "bring_sally_down",
        title = "Bring Sally Down Push-Up Crucible",
        subtitle = "Ultimate chest and tricep isometric test",
        description = "Hold isometric push-up hover on 'Sally Down', press up on 'Sally Up'. Pure grit and chest hypertrophy.",
        targetDays = 1,
        currentProgressDays = 0,
        xpReward = 350,
        badgeIcon = "⚡"
      ),
      Challenge(
        id = "plank_7_days",
        title = "7-Day Core Fortification",
        subtitle = "Progressive isometric abdominal stability",
        description = "Hold a strict RKC plank for 2 minutes daily for 7 days to build a bulletproof trunk and protect your spine.",
        targetDays = 7,
        currentProgressDays = 4,
        xpReward = 250,
        badgeIcon = "🛡️"
      ),
      Challenge(
        id = "habit_21",
        title = "21-Day Habit Transformer",
        subtitle = "Re-wire your neural fitness habit loops",
        description = "Log a workout or active recovery session every single day for 21 days straight.",
        targetDays = 21,
        currentProgressDays = 6,
        xpReward = 750,
        badgeIcon = "🔥"
      ),
      Challenge(
        id = "no_sugar_week",
        title = "Zero Added Sugar Sprint",
        subtitle = "7 days of natural, unprocessed clean energy",
        description = "Cut refined sugars, sodas, and ultra-processed snacks for 7 full days to restore insulin sensitivity.",
        targetDays = 7,
        currentProgressDays = 3,
        xpReward = 300,
        badgeIcon = "🍏"
      ),
      Challenge(
        id = "daily_10k_steps",
        title = "10,000 Daily Steps Marathon",
        subtitle = "Continuous NEAT (Non-Exercise Activity) burn",
        description = "Hit 10,000 steps daily for 14 days straight to elevate daily caloric expenditure and cardio health.",
        targetDays = 14,
        currentProgressDays = 5,
        xpReward = 400,
        badgeIcon = "👟"
      )
    )
  }

  fun getArticles(): List<ArticleCard> {
    return listOf(
      ArticleCard(
        id = "art_1",
        type = "MUSCLE_OF_DAY",
        title = "Muscle of the Day: Latissimus Dorsi",
        subtitle = "The Wings of the Human Back",
        content = "The Lats are the broadest muscle of the human back, originating along the spine and inserting into the bicep groove of the humerus. For maximum lat development, focus on pulling your elbows towards your hips (not pulling with your hands) during vertical and horizontal pulls.",
        actionTag = "Back Training",
        iconEmoji = "🦅"
      ),
      ArticleCard(
        id = "art_2",
        type = "ATHLETE_OF_DAY",
        title = "Athlete of the Day: Arnold Schwarzenegger",
        subtitle = "7x Mr. Olympia & Golden Era Legend",
        content = "\"The mind is the limit. As long as the mind can envision the fact that you can do something, you can do it.\" Arnold's secret was intense mind-muscle connection, visualizing his biceps peaking like mountain ranges during every single rep.",
        actionTag = "Mindset & Form",
        iconEmoji = "🏆"
      ),
      ArticleCard(
        id = "art_3",
        type = "DAILY_TIP",
        title = "The Power of Sleep & Muscle Synthesis",
        subtitle = "Where gains are actually built",
        content = "Over 70% of daily Human Growth Hormone (HGH) pulse occurs during Stage 3 deep Slow-Wave Sleep. Training breaks down muscle tissue; uninterrupted 7-9 hours of sleep with adequate protein intake is where actual myofibrillar hypertrophy occurs.",
        actionTag = "Recovery",
        iconEmoji = "💤"
      ),
      ArticleCard(
        id = "art_4",
        type = "NUTRITION",
        title = "Optimal Daily Protein Distribution",
        subtitle = "Maximizing Muscle Protein Synthesis (MPS)",
        content = "Research indicates that consuming 0.4g to 0.5g of high-quality protein per kilogram of bodyweight per meal across 3 to 5 meals per day maximizes the anabolic response, allowing the leucine trigger (~2.5g leucine) to be hit multiple times daily.",
        actionTag = "Nutrition Science",
        iconEmoji = "🥑"
      )
    )
  }
}
