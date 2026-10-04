package com.example.ui.screens.nutrition

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.DailyCheckIn
import com.example.data.model.MealPlanItem
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreen(
  userProfile: UserProfile?,
  todayCheckIn: DailyCheckIn?,
  meals: List<MealPlanItem>,
  onMealClick: (MealPlanItem) -> Unit,
  onWaterAdd: (Int) -> Unit,
  onLogFood: (Int) -> Unit
) {
  var showQuickLogDialog by remember { mutableStateOf(false) }
  var caloriesInput by remember { mutableStateOf("350") }

  val targetCalories = when (userProfile?.goal) {
    com.example.data.model.FitnessGoal.LOSE_WEIGHT -> 2100
    com.example.data.model.FitnessGoal.BUILD_MUSCLE -> 2650
    com.example.data.model.FitnessGoal.GAIN_WEIGHT -> 2900
    else -> 2400
  }

  val targetProtein = ((userProfile?.currentWeightKg ?: 80f) * 2.0f).toInt() // 2g/kg
  val targetFats = ((targetCalories * 0.25f) / 9f).toInt()
  val targetCarbs = ((targetCalories - (targetProtein * 4 + targetFats * 9)) / 4f).toInt()

  val caloriesLogged = todayCheckIn?.caloriesLogged ?: 1850
  val waterDrunk = todayCheckIn?.waterDrunkMl ?: 2250
  val waterTarget = userProfile?.waterTargetMl ?: 3000

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "FUEL & RECOVERY",
          style = MaterialTheme.typography.labelSmall,
          color = VoltLime,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Diet & Macro Engine",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      Button(
        onClick = { showQuickLogDialog = true },
        colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = Modifier.testTag("log_food_dialog_btn")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Log Food", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Safety & Guardrail Disclaimer Banner
    Surface(
      color = Color(0xFF1B202A),
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(1.dp, Color(0xFF333E54)),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = VoltLime, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Not medical advice. Consult a doctor or dietitian. IronPulse enforces safety guardrails: no extreme deficits (<1,200 kcal) and warnings for pregnancy or medical conditions.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(0xFFCCD4E0),
          fontSize = 11.sp,
          lineHeight = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Calories & Macros Card
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text("DAILY ENERGY BALANCE", style = MaterialTheme.typography.labelSmall, color = ElectricFlame, fontWeight = FontWeight.Bold)
              Text("$caloriesLogged / $targetCalories kcal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }

            LinearProgressIndicator(
              progress = { (caloriesLogged / targetCalories.toFloat()).coerceIn(0f, 1f) },
              color = ElectricFlame,
              trackColor = MaterialTheme.colorScheme.surface,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(8.dp)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              MacroColumn(name = "Protein", currentG = 145, targetG = targetProtein, color = ElectricFlame)
              MacroColumn(name = "Carbs", currentG = 210, targetG = targetCarbs, color = VoltLime)
              MacroColumn(name = "Fats", currentG = 52, targetG = targetFats, color = CyberCyan)
            }
          }
        }
      }

      // Water Hydration Tracker Card
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF14202B)),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WaterDrop, contentDescription = null, tint = CyberCyan)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Hydration Tracker", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
              }
              Text("${waterDrunk} / ${waterTarget} ml", fontWeight = FontWeight.Bold, color = CyberCyan)
            }

            LinearProgressIndicator(
              progress = { (waterDrunk / waterTarget.toFloat()).coerceIn(0f, 1f) },
              color = CyberCyan,
              trackColor = Color(0xFF0E161F),
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .height(8.dp)
            )

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              OutlinedButton(
                onClick = { onWaterAdd(250) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("water_add_250")
              ) {
                Text("+250 ml")
              }
              OutlinedButton(
                onClick = { onWaterAdd(500) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                  .weight(1f)
                  .testTag("water_add_500")
              ) {
                Text("+500 ml")
              }
            }
          }
        }
      }

      // Personalized Meal Plan Cards
      item {
        Text(
          text = "TODAY'S PERSONALIZED MEAL PROTOCOL",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      items(meals) { meal ->
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(14.dp),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onMealClick(meal) }
            .testTag("meal_item_${meal.id}")
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Surface(
                color = ElectricFlame.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = meal.mealType.uppercase(),
                  style = MaterialTheme.typography.labelSmall,
                  fontWeight = FontWeight.Bold,
                  color = ElectricFlame,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }

              Text(
                text = "${meal.calories} kcal • Tier: ${meal.budgetTier}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray
              )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
              text = meal.name,
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
              text = "P: ${meal.proteinG}g  •  C: ${meal.carbsG}g  •  F: ${meal.fatG}g",
              style = MaterialTheme.typography.bodySmall,
              color = VoltLime,
              fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Tap to view ingredients & recipe",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
              )

              Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.clickable { onMealClick(meal) }
              ) {
                Row(
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(Icons.Default.SwapHoriz, contentDescription = "Swap", tint = CyberCyan, modifier = Modifier.size(14.dp))
                  Spacer(modifier = Modifier.width(4.dp))
                  Text("Swap Meal", fontSize = 11.sp, color = CyberCyan, fontWeight = FontWeight.Bold)
                }
              }
            }
          }
        }
      }
    }
  }

  // Quick Food Log Dialog
  if (showQuickLogDialog) {
    AlertDialog(
      onDismissRequest = { showQuickLogDialog = false },
      title = { Text("Log Food Intake") },
      text = {
        Column {
          Text("Quickly add calories to your daily total:", style = MaterialTheme.typography.bodySmall)
          Spacer(modifier = Modifier.height(12.dp))
          OutlinedTextField(
            value = caloriesInput,
            onValueChange = { caloriesInput = it },
            label = { Text("Calories (kcal)") },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("food_calories_input")
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val c = caloriesInput.toIntOrNull() ?: 350
            onLogFood(c)
            showQuickLogDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
          modifier = Modifier.testTag("confirm_food_log_btn")
        ) {
          Text("Log Calories")
        }
      },
      dismissButton = {
        TextButton(onClick = { showQuickLogDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }
}

@Composable
fun MacroColumn(name: String, currentG: Int, targetG: Int, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(name, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    Spacer(modifier = Modifier.height(2.dp))
    Text("${currentG}g / ${targetG}g", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = color)
  }
}
