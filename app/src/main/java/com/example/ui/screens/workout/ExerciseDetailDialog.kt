package com.example.ui.screens.workout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Exercise
import com.example.ui.components.InteractiveMuscleMap
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@Composable
fun ExerciseDetailDialog(
  exercise: Exercise,
  onDismiss: () -> Unit
) {
  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.9f)
        .testTag("exercise_detail_dialog"),
      shape = RoundedCornerShape(20.dp),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(exercise.iconEmoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
              )
              Text(
                text = "${exercise.category} • ${exercise.level}",
                style = MaterialTheme.typography.bodySmall,
                color = ElectricFlame
              )
            }
          }

          IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_exercise_dialog")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Muscle Map (front/back silhouette)
        InteractiveMuscleMap(
          primaryMuscle = exercise.primaryMuscle,
          secondaryMuscles = exercise.secondaryMuscles
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sets / Reps / Rest Card
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("SETS", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
              Text("${exercise.defaultSets}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("REPS", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
              Text(exercise.defaultReps, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("REST", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
              Text("${exercise.restSeconds}s", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ElectricFlame)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Execution Guide
        Text("EXECUTION STEPS", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        Text(exercise.instructions, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD6DBE2), lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Coaching Tips
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E281F)),
          border = androidx.compose.foundation.BorderStroke(1.dp, VoltLime.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text("💡 COACHING CUE", style = MaterialTheme.typography.labelSmall, color = VoltLime, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(exercise.coachingTips, style = MaterialTheme.typography.bodyMedium, color = Color.White)
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Common Mistakes
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF281E1E)),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("COMMON MISTAKES TO AVOID", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(exercise.commonMistakes, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFFD4D4))
          }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
        ) {
          Text("Got It", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}
