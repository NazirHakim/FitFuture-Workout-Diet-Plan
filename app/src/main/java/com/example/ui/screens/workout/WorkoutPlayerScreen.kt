package com.example.ui.screens.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.ui.components.InteractiveMuscleMap
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
  exercises: List<Exercise>,
  currentExerciseIndex: Int,
  currentSetNumber: Int,
  secondsElapsed: Int,
  isResting: Boolean,
  restSecondsRemaining: Int,
  onCompleteSet: (weightKg: Float, reps: Int) -> Unit,
  onSkipExercise: () -> Unit,
  onSkipRest: () -> Unit,
  onFinishWorkout: () -> Unit
) {
  val exercise = exercises.getOrNull(currentExerciseIndex) ?: return

  var weightInput by remember(currentExerciseIndex, currentSetNumber) { mutableStateOf("60") }
  var repsInput by remember(currentExerciseIndex, currentSetNumber) { mutableStateOf("10") }
  var showFormMap by remember { mutableStateOf(false) }

  val minutes = secondsElapsed / 60
  val seconds = secondsElapsed % 60
  val formattedTime = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)
  val caloriesBurned = (secondsElapsed / 60) * exercise.caloriesBurnPerMinute

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "ACTIVE WORKOUT PLAYER",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricFlame,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
              Text(
                text = "${currentExerciseIndex + 1} of ${exercises.size} Exercises",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
              )
            }

            Surface(
              color = Color(0xFF202632),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(Icons.Default.Timer, contentDescription = null, tint = VoltLime, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(formattedTime, fontWeight = FontWeight.Bold, color = VoltLime, fontSize = 14.sp)
              }
            }
          }
        },
        actions = {
          IconButton(onClick = onFinishWorkout, modifier = Modifier.testTag("end_workout_btn")) {
            Icon(Icons.Default.Check, contentDescription = "Finish Workout", tint = ElectricFlame)
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
        .padding(horizontal = 16.dp)
        .verticalScroll(rememberScrollState())
    ) {
      if (isResting) {
        // Rest Timer Overlay Card
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF132228)),
          shape = RoundedCornerShape(20.dp),
          border = androidx.compose.foundation.BorderStroke(2.dp, CyberCyan),
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
        ) {
          Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text("REST & HYDRATE", style = MaterialTheme.typography.labelMedium, color = CyberCyan, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "$restSecondsRemaining s",
              style = MaterialTheme.typography.displayMedium,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            Text("Breathe deeply • Prepare for Set ${currentSetNumber}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Button(
              onClick = onSkipRest,
              colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.testTag("skip_rest_btn")
            ) {
              Text("Skip Rest", color = Color.Black, fontWeight = FontWeight.Bold)
            }
          }
        }
      }

      // Exercise Header Card
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(exercise.iconEmoji, fontSize = 32.sp)
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = exercise.name,
                  style = MaterialTheme.typography.titleLarge,
                  fontWeight = FontWeight.ExtraBold,
                  color = Color.White
                )
                Text(
                  text = "Target: ${exercise.primaryMuscle}",
                  style = MaterialTheme.typography.bodySmall,
                  color = ElectricFlame
                )
              }
            }

            IconButton(onClick = { showFormMap = !showFormMap }) {
              Icon(
                if (showFormMap) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "Toggle Muscle Map",
                tint = VoltLime
              )
            }
          }

          AnimatedVisibility(visible = showFormMap) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
              InteractiveMuscleMap(primaryMuscle = exercise.primaryMuscle, secondaryMuscles = exercise.secondaryMuscles)
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Live Coaching Cue banner
          Surface(
            color = Color(0xFF1D2821),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, VoltLime.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("🗣️", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "CUE: ${exercise.coachingTips}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Set Logger Box
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricFlame.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "SET $currentSetNumber OF ${exercise.defaultSets}",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.ExtraBold,
              color = ElectricFlame
            )

            Surface(
              color = MaterialTheme.colorScheme.surface,
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = "Target: ${exercise.defaultReps} reps",
                style = MaterialTheme.typography.labelSmall,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
          ) {
            OutlinedTextField(
              value = weightInput,
              onValueChange = { weightInput = it },
              label = { Text("Weight (kg)") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .weight(1f)
                .testTag("set_weight_input")
            )

            OutlinedTextField(
              value = repsInput,
              onValueChange = { repsInput = it },
              label = { Text("Reps Completed") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              modifier = Modifier
                .weight(1f)
                .testTag("set_reps_input")
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Complete Set Button
          Button(
            onClick = {
              val w = weightInput.toFloatOrNull() ?: 60f
              val r = repsInput.toIntOrNull() ?: 10
              onCompleteSet(w, r)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .testTag("complete_set_button")
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Log Set & Start Rest", fontWeight = FontWeight.Bold, fontSize = 15.sp)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Bottom Metrics & Skip Actions
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text("EST. ENERGY EXPENDITURE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
          Text("$caloriesBurned kcal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VoltLime)
        }

        OutlinedButton(
          onClick = onSkipExercise,
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.testTag("skip_exercise_btn")
        ) {
          Icon(Icons.Default.SkipNext, contentDescription = null)
          Spacer(modifier = Modifier.width(6.dp))
          Text("Skip Exercise")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
