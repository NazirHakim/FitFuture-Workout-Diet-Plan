package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.FitnessRepository
import com.example.service.CoachAiService
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

enum class AppScreen {
  ONBOARDING,
  PREDICTION,
  MAIN_DASHBOARD,
  WORKOUT_PLAYER,
  EXERCISE_DETAIL,
  PROGRESS_PHOTO_COMPARE,
  CHALLENGE_DETAIL,
  SETTINGS_PRIVACY,
  GOOGLE_PLAY_DELIVERABLES
}

enum class MainTab {
  DASHBOARD,
  WORKOUTS,
  NUTRITION,
  PROGRESS,
  COACH,
  CHALLENGES
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
  private val repository = FitnessRepository(application)
  private val coachService = CoachAiService()

  val userProfile: StateFlow<UserProfile?> = repository.userProfile
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

  val exercises: StateFlow<List<Exercise>> = repository.allExercises
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val workoutLogs: StateFlow<List<WorkoutLog>> = repository.workoutLogs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val prs: StateFlow<List<PersonalRecord>> = repository.allPRs
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val checkIns: StateFlow<List<DailyCheckIn>> = repository.allCheckIns
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val meals: StateFlow<List<MealPlanItem>> = repository.allMeals
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val achievements: StateFlow<List<Achievement>> = repository.allAchievements
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val challenges: StateFlow<List<Challenge>> = repository.allChallenges
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val articles: List<ArticleCard> = repository.getArticles()

  // Navigation & Current Screen
  private val _currentScreen = MutableStateFlow(AppScreen.MAIN_DASHBOARD)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _selectedTab = MutableStateFlow(MainTab.DASHBOARD)
  val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

  // Active Workout Player State
  private val _activeWorkoutExercises = MutableStateFlow<List<Exercise>>(emptyList())
  val activeWorkoutExercises: StateFlow<List<Exercise>> = _activeWorkoutExercises.asStateFlow()

  private val _currentExerciseIndex = MutableStateFlow(0)
  val currentExerciseIndex: StateFlow<Int> = _currentExerciseIndex.asStateFlow()

  private val _workoutSecondsElapsed = MutableStateFlow(0)
  val workoutSecondsElapsed: StateFlow<Int> = _workoutSecondsElapsed.asStateFlow()

  private val _restSecondsRemaining = MutableStateFlow(0)
  val restSecondsRemaining: StateFlow<Int> = _restSecondsRemaining.asStateFlow()

  private val _isResting = MutableStateFlow(false)
  val isResting: StateFlow<Boolean> = _isResting.asStateFlow()

  private val _currentSetNumber = MutableStateFlow(1)
  val currentSetNumber: StateFlow<Int> = _currentSetNumber.asStateFlow()

  private var timerJob: Job? = null
  private var restJob: Job? = null

  // Selection inspection
  private val _inspectedExercise = MutableStateFlow<Exercise?>(null)
  val inspectedExercise: StateFlow<Exercise?> = _inspectedExercise.asStateFlow()

  private val _inspectedMeal = MutableStateFlow<MealPlanItem?>(null)
  val inspectedMeal: StateFlow<MealPlanItem?> = _inspectedMeal.asStateFlow()

  private val _inspectedArticle = MutableStateFlow<ArticleCard?>(null)
  val inspectedArticle: StateFlow<ArticleCard?> = _inspectedArticle.asStateFlow()

  // Chat input
  private val _isChatLoading = MutableStateFlow(false)
  val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

  init {
    viewModelScope.launch {
      repository.initializeDefaultDataIfEmpty()
    }
  }

  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun selectTab(tab: MainTab) {
    _selectedTab.value = tab
    _currentScreen.value = AppScreen.MAIN_DASHBOARD
  }

  fun inspectExercise(exercise: Exercise?) {
    _inspectedExercise.value = exercise
  }

  fun inspectMeal(meal: MealPlanItem?) {
    _inspectedMeal.value = meal
  }

  fun inspectArticle(article: ArticleCard?) {
    _inspectedArticle.value = article
  }

  // --- Workout Player Logic ---
  fun startWorkoutSession(workoutTitle: String, exercisesToPerform: List<Exercise>) {
    _activeWorkoutExercises.value = exercisesToPerform
    _currentExerciseIndex.value = 0
    _workoutSecondsElapsed.value = 0
    _currentSetNumber.value = 1
    _isResting.value = false
    _restSecondsRemaining.value = 0
    _currentScreen.value = AppScreen.WORKOUT_PLAYER

    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (true) {
        delay(1000)
        _workoutSecondsElapsed.value += 1
      }
    }
  }

  fun completeCurrentSet(weightKg: Float, reps: Int) {
    val currentEx = _activeWorkoutExercises.value.getOrNull(_currentExerciseIndex.value)
    if (currentEx != null) {
      viewModelScope.launch {
        // Check if personal record
        val existingPr = prs.value.find { it.exerciseId == currentEx.id }
        if (existingPr == null || weightKg > existingPr.maxWeightKg) {
          repository.recordPR(currentEx.id, currentEx.name, weightKg, reps)
        }
      }

      if (_currentSetNumber.value < currentEx.defaultSets) {
        _currentSetNumber.value += 1
        startRestTimer(currentEx.restSeconds)
      } else {
        // Advance to next exercise or finish
        if (_currentExerciseIndex.value < _activeWorkoutExercises.value.size - 1) {
          _currentExerciseIndex.value += 1
          _currentSetNumber.value = 1
          startRestTimer(currentEx.restSeconds)
        } else {
          finishWorkoutSession()
        }
      }
    }
  }

  fun skipCurrentExercise() {
    if (_currentExerciseIndex.value < _activeWorkoutExercises.value.size - 1) {
      _currentExerciseIndex.value += 1
      _currentSetNumber.value = 1
      _isResting.value = false
      restJob?.cancel()
    } else {
      finishWorkoutSession()
    }
  }

  private fun startRestTimer(seconds: Int) {
    _isResting.value = true
    _restSecondsRemaining.value = seconds
    restJob?.cancel()
    restJob = viewModelScope.launch {
      while (_restSecondsRemaining.value > 0) {
        delay(1000)
        _restSecondsRemaining.value -= 1
      }
      _isResting.value = false
    }
  }

  fun skipRestTimer() {
    restJob?.cancel()
    _isResting.value = false
    _restSecondsRemaining.value = 0
  }

  fun finishWorkoutSession() {
    timerJob?.cancel()
    restJob?.cancel()
    val totalTime = _workoutSecondsElapsed.value
    val count = _activeWorkoutExercises.value.size
    val calories = (totalTime / 60) * 9 // ~9 kcal/min
    viewModelScope.launch {
      repository.logWorkout("Custom Workout Session", totalTime, calories, count)
    }
    _currentScreen.value = AppScreen.MAIN_DASHBOARD
  }

  // --- Water & Food Logging ---
  fun addWater(ml: Int = 250) {
    viewModelScope.launch {
      repository.logWater(ml)
    }
  }

  fun logMealCalories(calories: Int) {
    viewModelScope.launch {
      repository.logFoodCalories(calories)
    }
  }

  // --- AI Chat ---
  fun sendMessageToCoach(text: String) {
    if (text.isBlank()) return
    viewModelScope.launch {
      _isChatLoading.value = true
      val profile = userProfile.value
      val reply = coachService.getCoachResponse(text, profile)
      repository.sendChatMessage(text, reply)
      _isChatLoading.value = false
    }
  }

  // --- Check-In & Progress ---
  fun submitDailyCheckIn(
    weightKg: Float,
    chest: Float?,
    waist: Float?,
    arms: Float?,
    photoUri: String?,
    notes: String
  ) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = sdf.format(Date())
    viewModelScope.launch {
      val checkIn = DailyCheckIn(
        dateString = today,
        weightKg = weightKg,
        chestCm = chest,
        waistCm = waist,
        armsCm = arms,
        photoUri = photoUri,
        notes = notes,
        workoutDone = true
      )
      repository.saveCheckIn(checkIn)
    }
  }

  // --- Onboarding Completion ---
  fun completeOnboarding(profile: UserProfile) {
    viewModelScope.launch {
      repository.updateProfile(profile.copy(isOnboarded = true))
      _currentScreen.value = AppScreen.PREDICTION
    }
  }

  fun deleteAccountAndAllData() {
    viewModelScope.launch {
      repository.deleteAllData()
      _currentScreen.value = AppScreen.ONBOARDING
    }
  }

  fun updateUnits(useMetric: Boolean) {
    val current = userProfile.value ?: return
    viewModelScope.launch {
      repository.updateProfile(current.copy(useMetricUnits = useMetric))
    }
  }

  fun updateNotifications(enabled: Boolean, time: String) {
    val current = userProfile.value ?: return
    viewModelScope.launch {
      repository.updateProfile(current.copy(notificationsEnabled = enabled, notificationTime = time))
    }
  }
}
