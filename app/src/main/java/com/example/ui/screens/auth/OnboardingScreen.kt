package com.example.ui.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
  onComplete: (UserProfile) -> Unit
) {
  var currentStep by remember { mutableStateOf(0) } // 0: Auth/Sign-in, 1: Metrics, 2: Diet, 3: Activity, 4: Fitness Plan, 5: Why, 6: Optional Photo
  val totalSteps = 6

  // Form states
  var name by remember { mutableStateOf("Alex") }
  var email by remember { mutableStateOf("athlete@ironpulse.fit") }
  var ageText by remember { mutableStateOf("26") }
  var gender by remember { mutableStateOf(Gender.MALE) }
  var heightText by remember { mutableStateOf("178") }
  var weightText by remember { mutableStateOf("80") }
  var targetWeightText by remember { mutableStateOf("74") }

  var dietType by remember { mutableStateOf(DietType.NON_VEGETARIAN) }
  var allergies by remember { mutableStateOf("None") }
  var mealsPerDay by remember { mutableStateOf(4) }
  var waterTargetMl by remember { mutableStateOf(3000) }
  var junkFoodFrequency by remember { mutableStateOf("1-2x / month") }

  var activityLevel by remember { mutableStateOf(ActivityLevel.MODERATELY_ACTIVE) }
  var sleepHours by remember { mutableStateOf(7.5f) }
  var injuries by remember { mutableStateOf("None") }

  var workoutLocation by remember { mutableStateOf(WorkoutLocation.GYM) }
  var fitnessGoal by remember { mutableStateOf(FitnessGoal.BUILD_MUSCLE) }
  var daysPerWeek by remember { mutableStateOf(5) }

  var whyStartedPreset by remember { mutableStateOf("Build Unshakable Strength & Discipline") }
  var whyStartedCustom by remember { mutableStateOf("I want to feel energetic every morning, build confidence, and stay healthy for my loved ones.") }

  var uploadedPhotoMock by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "IRONPULSE SETUP",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        },
        navigationIcon = {
          if (currentStep > 0) {
            IconButton(
              onClick = { currentStep -= 1 },
              modifier = Modifier.testTag("onboarding_back_button")
            ) {
              Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 20.dp)
        .verticalScroll(rememberScrollState())
    ) {
      if (currentStep > 0) {
        // Step Progress Bar
        LinearProgressIndicator(
          progress = { currentStep / totalSteps.toFloat() },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp),
          color = ElectricFlame,
          trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Step $currentStep of $totalSteps",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
      }

      when (currentStep) {
        0 -> {
          // Auth Screen
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
              modifier = Modifier
                .size(72.dp)
                .background(ElectricFlame, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Welcome to IronPulse",
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Your elite AI trainer, progressive overload tracker, and body architect.",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
              value = name,
              onValueChange = { name = it },
              label = { Text("Full Name") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_name_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
              value = email,
              onValueChange = { email = it },
              label = { Text("Email Address") },
              modifier = Modifier
                .fillMaxWidth()
                .testTag("auth_email_input"),
              singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
              onClick = { currentStep = 1 },
              colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_continue_button")
            ) {
              Text("Continue with Email", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
              onClick = { currentStep = 1 },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_google_button")
            ) {
              Icon(Icons.Default.AccountCircle, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Continue with Google")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
              onClick = { currentStep = 1 },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("auth_phone_button")
            ) {
              Icon(Icons.Default.PhoneIphone, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("Sign in with Phone OTP")
            }
          }
        }

        1 -> {
          // Metrics Step
          Text("Basic Body Metrics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Text("Calibrates your basal metabolic rate and target loads", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          Spacer(modifier = Modifier.height(16.dp))

          OutlinedTextField(
            value = ageText,
            onValueChange = { ageText = it },
            label = { Text("Age (years)") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("onboarding_age_input")
          )
          Spacer(modifier = Modifier.height(12.dp))

          Text("Biological Gender", style = MaterialTheme.typography.labelMedium)
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Gender.values().forEach { g ->
              val sel = gender == g
              Button(
                onClick = { gender = g },
                colors = ButtonDefaults.buttonColors(containerColor = if (sel) ElectricFlame else MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.weight(1f)
              ) {
                Text(g.name, fontSize = 12.sp, color = if (sel) Color.White else Color.Gray)
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
              value = heightText,
              onValueChange = { heightText = it },
              label = { Text("Height (cm)") },
              modifier = Modifier
                .weight(1f)
                .testTag("onboarding_height_input")
            )
            OutlinedTextField(
              value = weightText,
              onValueChange = { weightText = it },
              label = { Text("Current (kg)") },
              modifier = Modifier
                .weight(1f)
                .testTag("onboarding_weight_input")
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedTextField(
            value = targetWeightText,
            onValueChange = { targetWeightText = it },
            label = { Text("Target Weight (kg)") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("onboarding_target_weight_input")
          )

          Spacer(modifier = Modifier.height(24.dp))

          Button(
            onClick = { currentStep = 2 },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("onboarding_step1_next")
          ) {
            Text("Next: Eating Habits", fontWeight = FontWeight.Bold)
          }
        }

        2 -> {
          // Diet Step
          Text("Diet & Nutrition Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))

          Text("Dietary Preference", style = MaterialTheme.typography.labelMedium)
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            DietType.values().forEach { diet ->
              val sel = dietType == diet
              Surface(
                color = if (sel) ElectricFlame.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) ElectricFlame else Color.Transparent),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { dietType = diet }
                  .padding(vertical = 2.dp)
              ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                  RadioButton(selected = sel, onClick = { dietType = diet })
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(diet.name.replace("_", " "), fontWeight = FontWeight.SemiBold)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = allergies,
            onValueChange = { allergies = it },
            label = { Text("Allergies or Intolerances") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(12.dp))
          Text("Meals per day: $mealsPerDay", style = MaterialTheme.typography.bodyMedium)
          Slider(
            value = mealsPerDay.toFloat(),
            onValueChange = { mealsPerDay = it.toInt() },
            valueRange = 2f..6f,
            steps = 3
          )

          Spacer(modifier = Modifier.height(20.dp))
          Button(
            onClick = { currentStep = 3 },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
          ) {
            Text("Next: Activity & Sleep", fontWeight = FontWeight.Bold)
          }
        }

        3 -> {
          // Activity & Limitations
          Text("Activity Level & Recovery", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))

          Text("Daily Activity Outside Gym", style = MaterialTheme.typography.labelMedium)
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ActivityLevel.values().forEach { act ->
              val sel = activityLevel == act
              Surface(
                color = if (sel) ElectricFlame.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) ElectricFlame else Color.Transparent),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { activityLevel = act }
              ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                  RadioButton(selected = sel, onClick = { activityLevel = act })
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(act.name.replace("_", " "))
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text("Sleep Hours: ${String.format(java.util.Locale.US, "%.1f", sleepHours)} hrs / night", style = MaterialTheme.typography.bodyMedium)
          Slider(
            value = sleepHours,
            onValueChange = { sleepHours = it },
            valueRange = 4f..10f
          )

          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = injuries,
            onValueChange = { injuries = it },
            label = { Text("Injuries / Joint Limitations (e.g. lower back, shoulder)") },
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = { currentStep = 4 },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
          ) {
            Text("Next: Workout Goals", fontWeight = FontWeight.Bold)
          }
        }

        4 -> {
          // Goal & Location
          Text("Goal & Equipment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(16.dp))

          Text("Primary Goal", style = MaterialTheme.typography.labelMedium)
          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FitnessGoal.values().forEach { g ->
              val sel = fitnessGoal == g
              Surface(
                color = if (sel) ElectricFlame.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) ElectricFlame else Color.Transparent),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { fitnessGoal = g }
              ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                  RadioButton(selected = sel, onClick = { fitnessGoal = g })
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(g.name.replace("_", " "), fontWeight = FontWeight.Bold)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text("Training Location", style = MaterialTheme.typography.labelMedium)
          Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            WorkoutLocation.values().forEach { loc ->
              val sel = workoutLocation == loc
              Button(
                onClick = { workoutLocation = loc },
                colors = ButtonDefaults.buttonColors(containerColor = if (sel) ElectricFlame else MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.weight(1f)
              ) {
                Text(
                  text = when (loc) {
                    WorkoutLocation.GYM -> "Gym"
                    WorkoutLocation.HOME_EQUIPMENT -> "Home DB"
                    WorkoutLocation.HOME_NO_EQUIPMENT -> "Bodyweight"
                  },
                  fontSize = 11.sp,
                  color = if (sel) Color.White else Color.Gray
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          Text("Days available per week: $daysPerWeek days", style = MaterialTheme.typography.bodyMedium)
          Slider(
            value = daysPerWeek.toFloat(),
            onValueChange = { daysPerWeek = it.toInt() },
            valueRange = 2f..6f,
            steps = 3
          )

          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = { currentStep = 5 },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
          ) {
            Text("Next: Your Core Why", fontWeight = FontWeight.Bold)
          }
        }

        5 -> {
          // "Why did you start?" Step
          Text("Why Did You Start?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Text("IronPulse stores this to remind you on the days your motivation wavers.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          Spacer(modifier = Modifier.height(16.dp))

          val presets = listOf(
            "Build Unshakable Strength & Discipline",
            "Lose Weight & Reclaim Energy",
            "Be a Healthy Role Model for Family",
            "Overcome Past Limitations & Self-Doubt",
            "Compete & Reach Peak Athletic Condition"
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            presets.forEach { preset ->
              val sel = whyStartedPreset == preset
              Surface(
                color = if (sel) ElectricFlame.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) ElectricFlame else Color.Transparent),
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable { whyStartedPreset = preset }
              ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                  RadioButton(selected = sel, onClick = { whyStartedPreset = preset })
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(preset, style = MaterialTheme.typography.bodyMedium)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))
          OutlinedTextField(
            value = whyStartedCustom,
            onValueChange = { whyStartedCustom = it },
            label = { Text("Personal Reason (In your own words)") },
            modifier = Modifier
              .fillMaxWidth()
              .height(110.dp)
              .testTag("onboarding_why_input")
          )

          Spacer(modifier = Modifier.height(24.dp))
          Button(
            onClick = { currentStep = 6 },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
          ) {
            Text("Next: Optional Photo Vault", fontWeight = FontWeight.Bold)
          }
        }

        6 -> {
          // Optional Photo Upload Step
          Text("Optional Baseline Photo", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
          Spacer(modifier = Modifier.height(8.dp))

          Surface(
            color = Color(0xFF1B202A),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E384D)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, tint = VoltLime, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("PRIVACY GUARANTEED", style = MaterialTheme.typography.labelMedium, color = VoltLime, fontWeight = FontWeight.Bold)
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                "• Completely optional. The app works 100% without photos.\n• Encrypted locally on your device. Never uploaded to public servers.\n• Never used for advertising or AI training without explicit opt-in.\n• You can delete your photos and account at any time in Settings.",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFCCD4E0),
                lineHeight = 18.sp
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          if (!uploadedPhotoMock) {
            Button(
              onClick = { uploadedPhotoMock = true },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("upload_photo_mock_button")
            ) {
              Icon(Icons.Default.AddAPhoto, contentDescription = null, tint = ElectricFlame)
              Spacer(modifier = Modifier.width(10.dp))
              Text("Add Baseline Photo (Front / Side)", color = Color.White)
            }
          } else {
            Card(
              colors = CardDefaults.cardColors(containerColor = Color(0xFF1E281F)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VoltLime)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                  Text("Photo Encrypted & Added", fontWeight = FontWeight.Bold, color = VoltLime)
                  Text("Detailed Improvement Plan Unlocked!", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(30.dp))

          Button(
            onClick = {
              val profile = UserProfile(
                id = 1,
                name = name.ifBlank { "Alex" },
                email = email.ifBlank { "athlete@ironpulse.fit" },
                age = ageText.toIntOrNull() ?: 26,
                gender = gender,
                heightCm = heightText.toFloatOrNull() ?: 178f,
                currentWeightKg = weightText.toFloatOrNull() ?: 80f,
                targetWeightKg = targetWeightText.toFloatOrNull() ?: 74f,
                dietType = dietType,
                allergies = allergies,
                mealsPerDay = mealsPerDay,
                waterTargetMl = waterTargetMl,
                junkFoodFrequency = junkFoodFrequency,
                activityLevel = activityLevel,
                sleepHours = sleepHours,
                injuriesLimitations = injuries,
                workoutLocation = workoutLocation,
                goal = fitnessGoal,
                daysPerWeek = daysPerWeek,
                whyStartedText = whyStartedCustom,
                whyStartedPreset = whyStartedPreset,
                hasUploadedPhoto = uploadedPhotoMock,
                detailedPlanUnlocked = uploadedPhotoMock,
                isOnboarded = true
              )
              onComplete(profile)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(52.dp)
              .testTag("complete_onboarding_button")
          ) {
            Text(if (uploadedPhotoMock) "Generate My Transformation Plan" else "Skip Photo & Generate Plan", fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
