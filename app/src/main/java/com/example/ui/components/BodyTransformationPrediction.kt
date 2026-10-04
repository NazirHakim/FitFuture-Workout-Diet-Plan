package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FitnessGoal
import com.example.data.model.UserProfile
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@Composable
fun BodyTransformationPrediction(
  userProfile: UserProfile?,
  onContinue: () -> Unit = {}
) {
  var selectedMonth by remember { mutableStateOf(3) } // 1, 3, 6, 12
  var followPlanScenario by remember { mutableStateOf(true) } // follow plan vs don't follow plan

  val currentWeight = userProfile?.currentWeightKg ?: 80f
  val targetWeight = userProfile?.targetWeightKg ?: 74f
  val goal = userProfile?.goal ?: FitnessGoal.BUILD_MUSCLE

  // Calculations based on months & scenario
  val factor = when (selectedMonth) {
    1 -> 0.25f
    3 -> 0.60f
    6 -> 0.90f
    12 -> 1.00f
    else -> 0.60f
  }

  val projectedWeight: Float
  val projectedBodyFat: String
  val projectedMuscleGain: String

  if (followPlanScenario) {
    val delta = (targetWeight - currentWeight) * factor
    projectedWeight = currentWeight + delta
    projectedBodyFat = when (goal) {
      FitnessGoal.LOSE_WEIGHT -> "${(24f - 8f * factor).toInt()} - ${(26f - 7f * factor).toInt()}%"
      FitnessGoal.BUILD_MUSCLE -> "${(16f - 2f * factor).toInt()} - ${(17f - 2f * factor).toInt()}%"
      else -> "14 - 16%"
    }
    projectedMuscleGain = when (goal) {
      FitnessGoal.BUILD_MUSCLE -> "+${String.format(java.util.Locale.US, "%.1f", 3.8f * factor)} kg lean mass"
      FitnessGoal.LOSE_WEIGHT -> "+${String.format(java.util.Locale.US, "%.1f", 1.2f * factor)} kg lean mass"
      else -> "+1.5 kg"
    }
  } else {
    // If not following plan: regression / stalling
    projectedWeight = currentWeight + (2.5f * factor)
    projectedBodyFat = "24 - 28% (increasing)"
    projectedMuscleGain = "-0.5 kg (loss of tone)"
  }

  val animatedWeight by animateFloatAsState(targetValue = projectedWeight, label = "weight_anim")

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "AI TRANSFORMATION TRAJECTORY",
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.primary,
      letterSpacing = 1.2.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "Visualizing your physiological adaptation over time",
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Time horizon selector (1M, 3M, 6M, 12M)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
        .padding(4.dp),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      listOf(1 to "1 Mo", 3 to "3 Mos", 6 to "6 Mos", 12 to "1 Year").forEach { (month, label) ->
        val isSelected = selectedMonth == month
        Button(
          onClick = { selectedMonth = month },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
          ),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
          modifier = Modifier.testTag("month_button_$month")
        ) {
          Text(text = label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Scenario toggle: "If I follow my plan" vs "If I don't"
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
        .padding(4.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(
        onClick = { followPlanScenario = true },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (followPlanScenario) ElectricFlame else Color.Transparent,
          contentColor = if (followPlanScenario) Color.White else Color.Gray
        ),
        modifier = Modifier
          .weight(1f)
          .testTag("scenario_follow_plan")
      ) {
        Icon(Icons.Default.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("Follow Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }

      Button(
        onClick = { followPlanScenario = false },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (!followPlanScenario) MaterialTheme.colorScheme.error else Color.Transparent,
          contentColor = if (!followPlanScenario) Color.White else Color.Gray
        ),
        modifier = Modifier
          .weight(1f)
          .testTag("scenario_dont_follow")
      ) {
        Icon(Icons.Default.TrendingDown, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("No Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Morph Avatar / Body Silhouette Visualizer
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(190.dp)
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              MaterialTheme.colorScheme.surfaceVariant,
              MaterialTheme.colorScheme.surface
            )
          ),
          shape = RoundedCornerShape(20.dp)
        )
        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(20.dp)),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val torsoWidth = if (followPlanScenario) {
          lerp(w * 0.42f, w * 0.34f, factor) // leaner, V-taper
        } else {
          lerp(w * 0.42f, w * 0.50f, factor) // wider waist
        }

        val shoulderWidth = if (followPlanScenario) {
          lerp(w * 0.50f, w * 0.58f, factor) // broad athletic shoulders
        } else {
          lerp(w * 0.50f, w * 0.48f, factor)
        }

        val silhouetteColor = if (followPlanScenario) ElectricFlame.copy(alpha = 0.85f) else Color(0xFF6B4343)

        // Head
        drawCircle(
          color = silhouetteColor,
          radius = 24.dp.toPx(),
          center = Offset(w * 0.5f, h * 0.22f)
        )

        // Shoulders
        drawRoundRect(
          color = silhouetteColor,
          topLeft = Offset(w * 0.5f - shoulderWidth / 2f, h * 0.36f),
          size = Size(shoulderWidth, 22.dp.toPx()),
          cornerRadius = CornerRadius(12f, 12f)
        )

        // Torso / Chest & Core
        drawRoundRect(
          color = silhouetteColor,
          topLeft = Offset(w * 0.5f - torsoWidth / 2f, h * 0.48f),
          size = Size(torsoWidth, 36.dp.toPx()),
          cornerRadius = CornerRadius(14f, 14f)
        )

        // Legs
        val legW = torsoWidth * 0.38f
        drawRoundRect(
          color = silhouetteColor,
          topLeft = Offset(w * 0.5f - torsoWidth * 0.45f, h * 0.68f),
          size = Size(legW, 46.dp.toPx()),
          cornerRadius = CornerRadius(10f, 10f)
        )
        drawRoundRect(
          color = silhouetteColor,
          topLeft = Offset(w * 0.5f + torsoWidth * 0.45f - legW, h * 0.68f),
          size = Size(legW, 46.dp.toPx()),
          cornerRadius = CornerRadius(10f, 10f)
        )
      }

      // Overlay stats pill
      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 12.dp)
          .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(12.dp))
          .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "${String.format(java.util.Locale.US, "%.1f", animatedWeight)} kg",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.ExtraBold,
          color = if (followPlanScenario) VoltLime else MaterialTheme.colorScheme.error
        )
        Text(
          text = if (followPlanScenario) "On track for goal" else "Progression halted",
          style = MaterialTheme.typography.labelSmall,
          color = Color.LightGray
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Projected Stats Grid
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text("Est. Body Fat", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
          Spacer(modifier = Modifier.height(4.dp))
          Text(projectedBodyFat, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
      }

      Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text("Muscle Adaptation", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
          Spacer(modifier = Modifier.height(4.dp))
          Text(projectedMuscleGain, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (followPlanScenario) VoltLime else Color.White)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // MANDATORY Disclaimer per user specifications!
    Surface(
      color = Color(0xFF1E222B),
      shape = RoundedCornerShape(12.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF333D4F)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          Icons.Default.Info,
          contentDescription = "Medical & Estimation Disclaimer",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "MANDATORY DISCLAIMER: This is an estimated, motivational prediction. Real results vary by genetics, diet, consistency and health.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFC7CBD1),
          fontSize = 11.sp,
          lineHeight = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Button(
      onClick = onContinue,
      colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("prediction_continue_button")
    ) {
      Text("Enter My IronPulse Plan", fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
  }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
  return start + (stop - start) * fraction
}
