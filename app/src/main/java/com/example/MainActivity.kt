package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.MainTab
import com.example.ui.MainViewModel
import com.example.ui.components.*
import com.example.ui.screens.auth.OnboardingScreen
import com.example.ui.screens.challenges.ChallengesScreen
import com.example.ui.screens.coach.CoachChatScreen
import com.example.ui.screens.dashboard.DashboardScreen
import com.example.ui.screens.nutrition.NutritionScreen
import com.example.ui.screens.profile.DeliverablesScreen
import com.example.ui.screens.profile.ProfileSettingsScreen
import com.example.ui.screens.progress.PhotoCompareScreen
import com.example.ui.screens.progress.ProgressScreen
import com.example.ui.screens.workout.ExerciseDetailDialog
import com.example.ui.screens.workout.WorkoutListScreen
import com.example.ui.screens.workout.WorkoutPlayerScreen
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.IronPulseTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      IronPulseTheme(darkTheme = true) {
        val viewModel: MainViewModel = viewModel()
        IronPulseApp(viewModel)
      }
    }
  }
}

@Composable
fun IronPulseApp(viewModel: MainViewModel) {
  val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
  val exercises by viewModel.exercises.collectAsStateWithLifecycle()
  val workoutLogs by viewModel.workoutLogs.collectAsStateWithLifecycle()
  val prs by viewModel.prs.collectAsStateWithLifecycle()
  val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
  val meals by viewModel.meals.collectAsStateWithLifecycle()
  val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
  val achievements by viewModel.achievements.collectAsStateWithLifecycle()
  val challenges by viewModel.challenges.collectAsStateWithLifecycle()
  val articles = viewModel.articles

  val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
  val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

  // Player state
  val activeExercises by viewModel.activeWorkoutExercises.collectAsStateWithLifecycle()
  val currentExerciseIdx by viewModel.currentExerciseIndex.collectAsStateWithLifecycle()
  val currentSetNum by viewModel.currentSetNumber.collectAsStateWithLifecycle()
  val secondsElapsed by viewModel.workoutSecondsElapsed.collectAsStateWithLifecycle()
  val isResting by viewModel.isResting.collectAsStateWithLifecycle()
  val restRemaining by viewModel.restSecondsRemaining.collectAsStateWithLifecycle()
  val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

  // Inspected Dialogs
  val inspectedExercise by viewModel.inspectedExercise.collectAsStateWithLifecycle()
  val inspectedMeal by viewModel.inspectedMeal.collectAsStateWithLifecycle()
  val inspectedArticle by viewModel.inspectedArticle.collectAsStateWithLifecycle()

  var showAddCheckInDialog by remember { mutableStateOf(false) }

  // Handle Back gestures
  if (currentScreen != AppScreen.MAIN_DASHBOARD) {
    BackHandler {
      if (currentScreen == AppScreen.WORKOUT_PLAYER) {
        viewModel.finishWorkoutSession()
      } else {
        viewModel.navigateTo(AppScreen.MAIN_DASHBOARD)
      }
    }
  }

  // Today check-in if present
  val todayCheckIn = checkIns.firstOrNull()

  when (currentScreen) {
    AppScreen.ONBOARDING -> {
      OnboardingScreen(
        onComplete = { profile ->
          viewModel.completeOnboarding(profile)
        }
      )
    }

    AppScreen.PREDICTION -> {
      Scaffold(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.background)
      ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
          BodyTransformationPrediction(
            userProfile = userProfile,
            onContinue = { viewModel.navigateTo(AppScreen.MAIN_DASHBOARD) }
          )
        }
      }
    }

    AppScreen.WORKOUT_PLAYER -> {
      WorkoutPlayerScreen(
        exercises = activeExercises,
        currentExerciseIndex = currentExerciseIdx,
        currentSetNumber = currentSetNum,
        secondsElapsed = secondsElapsed,
        isResting = isResting,
        restSecondsRemaining = restRemaining,
        onCompleteSet = { weight, reps ->
          viewModel.completeCurrentSet(weight, reps)
        },
        onSkipExercise = { viewModel.skipCurrentExercise() },
        onSkipRest = { viewModel.skipRestTimer() },
        onFinishWorkout = { viewModel.finishWorkoutSession() }
      )
    }

    AppScreen.PROGRESS_PHOTO_COMPARE -> {
      PhotoCompareScreen(
        onBack = { viewModel.navigateTo(AppScreen.MAIN_DASHBOARD) }
      )
    }

    AppScreen.GOOGLE_PLAY_DELIVERABLES -> {
      DeliverablesScreen(
        onBack = { viewModel.navigateTo(AppScreen.MAIN_DASHBOARD) }
      )
    }

    AppScreen.SETTINGS_PRIVACY -> {
      ProfileSettingsScreen(
        userProfile = userProfile,
        onDeleteAccount = { viewModel.deleteAccountAndAllData() },
        onUnitToggle = { useMetric -> viewModel.updateUnits(useMetric) },
        onNotificationToggle = { enabled, time -> viewModel.updateNotifications(enabled, time) },
        onOpenDeliverables = { viewModel.navigateTo(AppScreen.GOOGLE_PLAY_DELIVERABLES) }
      )
    }

    else -> {
      // Main Application with Bottom Navigation Bar
      Scaffold(
        bottomBar = {
          NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.White,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
          ) {
            NavigationBarItem(
              icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
              label = { Text("Home", fontSize = 10.sp) },
              selected = selectedTab == MainTab.DASHBOARD,
              onClick = { viewModel.selectTab(MainTab.DASHBOARD) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_dashboard")
            )

            NavigationBarItem(
              icon = { Icon(Icons.Default.FitnessCenter, contentDescription = "Workouts") },
              label = { Text("Workouts", fontSize = 10.sp) },
              selected = selectedTab == MainTab.WORKOUTS,
              onClick = { viewModel.selectTab(MainTab.WORKOUTS) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_workouts")
            )

            NavigationBarItem(
              icon = { Icon(Icons.Default.Restaurant, contentDescription = "Diet") },
              label = { Text("Diet", fontSize = 10.sp) },
              selected = selectedTab == MainTab.NUTRITION,
              onClick = { viewModel.selectTab(MainTab.NUTRITION) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_nutrition")
            )

            NavigationBarItem(
              icon = { Icon(Icons.Default.Timeline, contentDescription = "Progress") },
              label = { Text("Progress", fontSize = 10.sp) },
              selected = selectedTab == MainTab.PROGRESS,
              onClick = { viewModel.selectTab(MainTab.PROGRESS) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_progress")
            )

            NavigationBarItem(
              icon = { Icon(Icons.Default.Psychology, contentDescription = "Coach Alex") },
              label = { Text("Coach", fontSize = 10.sp) },
              selected = selectedTab == MainTab.COACH,
              onClick = { viewModel.selectTab(MainTab.COACH) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_coach")
            )

            NavigationBarItem(
              icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Challenges") },
              label = { Text("Trophies", fontSize = 10.sp) },
              selected = selectedTab == MainTab.CHALLENGES,
              onClick = { viewModel.selectTab(MainTab.CHALLENGES) },
              colors = NavigationBarItemDefaults.colors(
                selectedIconColor = ElectricFlame,
                indicatorColor = ElectricFlame.copy(alpha = 0.2f)
              ),
              modifier = Modifier.testTag("nav_tab_challenges")
            )
          }
        }
      ) { innerPadding ->
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          when (selectedTab) {
            MainTab.DASHBOARD -> {
              DashboardScreen(
                userProfile = userProfile,
                todayCheckIn = todayCheckIn,
                exercises = exercises,
                articles = articles,
                onStartWorkout = { exList ->
                  viewModel.startWorkoutSession("Today's Chest Blast", exList)
                },
                onLogMeal = { viewModel.selectTab(MainTab.NUTRITION) },
                onAddPhoto = { showAddCheckInDialog = true },
                onChatWithCoach = { viewModel.selectTab(MainTab.COACH) },
                onWaterAdd = { ml -> viewModel.addWater(ml) },
                onArticleClick = { art -> viewModel.inspectArticle(art) },
                onOpenSettings = { viewModel.navigateTo(AppScreen.SETTINGS_PRIVACY) }
              )
            }

            MainTab.WORKOUTS -> {
              WorkoutListScreen(
                exercises = exercises,
                onExerciseClick = { ex -> viewModel.inspectExercise(ex) },
                onStartRoutine = { title, exList ->
                  viewModel.startWorkoutSession(title, exList)
                }
              )
            }

            MainTab.NUTRITION -> {
              NutritionScreen(
                userProfile = userProfile,
                todayCheckIn = todayCheckIn,
                meals = meals,
                onMealClick = { meal -> viewModel.inspectMeal(meal) },
                onWaterAdd = { ml -> viewModel.addWater(ml) },
                onLogFood = { cal -> viewModel.logMealCalories(cal) }
              )
            }

            MainTab.PROGRESS -> {
              ProgressScreen(
                userProfile = userProfile,
                checkIns = checkIns,
                prs = prs,
                onAddCheckIn = { showAddCheckInDialog = true },
                onOpenPhotoCompare = { viewModel.navigateTo(AppScreen.PROGRESS_PHOTO_COMPARE) }
              )
            }

            MainTab.COACH -> {
              CoachChatScreen(
                userProfile = userProfile,
                messages = chatMessages,
                isLoading = isChatLoading,
                onSendMessage = { msg -> viewModel.sendMessageToCoach(msg) }
              )
            }

            MainTab.CHALLENGES -> {
              ChallengesScreen(
                userProfile = userProfile,
                challenges = challenges,
                achievements = achievements
              )
            }
          }
        }
      }
    }
  }

  // Dialog Overlays
  inspectedExercise?.let { ex ->
    ExerciseDetailDialog(exercise = ex, onDismiss = { viewModel.inspectExercise(null) })
  }

  inspectedMeal?.let { meal ->
    MealDetailDialog(meal = meal, onDismiss = { viewModel.inspectMeal(null) })
  }

  inspectedArticle?.let { article ->
    ArticleDetailDialog(article = article, onDismiss = { viewModel.inspectArticle(null) })
  }

  if (showAddCheckInDialog) {
    AddCheckInDialog(
      currentWeight = userProfile?.currentWeightKg ?: 80f,
      onDismiss = { showAddCheckInDialog = false },
      onSave = { w, c, waist, a, photo, notes ->
        viewModel.submitDailyCheckIn(w, c, waist, a, photo, notes)
        showAddCheckInDialog = false
      }
    )
  }
}
