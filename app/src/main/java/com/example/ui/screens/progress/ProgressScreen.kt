package com.example.ui.screens.progress

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyCheckIn
import com.example.data.model.PersonalRecord
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(
  userProfile: UserProfile?,
  checkIns: List<DailyCheckIn>,
  prs: List<PersonalRecord>,
  onAddCheckIn: () -> Unit,
  onOpenPhotoCompare: () -> Unit
) {
  var selectedMetricTab by remember { mutableStateOf(0) } // 0: Weight, 1: Measurements, 2: Calendar & PRs
  val currentWeight = userProfile?.currentWeightKg ?: 80f
  val targetWeight = userProfile?.targetWeightKg ?: 74f

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "ANALYTICS & BODY METRICS",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricFlame,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Progress & Consistency",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      Button(
        onClick = onAddCheckIn,
        colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
        modifier = Modifier.testTag("add_checkin_button")
      ) {
        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Check-In", fontSize = 12.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Metric Tabs
    PrimaryTabRow(
      selectedTabIndex = selectedMetricTab,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = ElectricFlame
    ) {
      Tab(
        selected = selectedMetricTab == 0,
        onClick = { selectedMetricTab = 0 },
        text = { Text("Weight Trend", fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedMetricTab == 1,
        onClick = { selectedMetricTab = 1 },
        text = { Text("Girth Metrics", fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedMetricTab == 2,
        onClick = { selectedMetricTab = 2 },
        text = { Text("Calendar & PRs", fontWeight = FontWeight.Bold) }
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(14.dp),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      // Photo Comparison Vault banner
      item {
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2430)),
          shape = RoundedCornerShape(16.dp),
          border = BorderStroke(1.dp, VoltLime.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenPhotoCompare() }
            .testTag("photo_compare_banner")
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .background(VoltLime.copy(alpha = 0.2f), CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Compare, contentDescription = null, tint = VoltLime)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text("Day 1 vs Current Photo Slider", fontWeight = FontWeight.Bold, color = Color.White)
              Text("Encrypted Private Vault • Side-by-side morph", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
          }
        }
      }

      when (selectedMetricTab) {
        0 -> {
          // Weight Trend View
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
                  Column {
                    Text("CURRENT BODY WEIGHT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("${currentWeight} kg", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = Color.White)
                  }
                  Column(horizontalAlignment = Alignment.End) {
                    Text("TARGET", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("${targetWeight} kg", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VoltLime)
                  }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Canvas Trend Line Graph
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF131720), RoundedCornerShape(12.dp))
                    .padding(8.dp)
                ) {
                  Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    val sorted = checkIns.sortedBy { it.dateString }
                    if (sorted.isNotEmpty()) {
                      val minW = sorted.minOf { it.weightKg } - 1f
                      val maxW = sorted.maxOf { it.weightKg } + 1f
                      val range = (maxW - minW).coerceAtLeast(1f)

                      val path = Path()
                      sorted.forEachIndexed { index, item ->
                        val x = (index / (sorted.size - 1).toFloat().coerceAtLeast(1f)) * (w - 20.dp.toPx()) + 10.dp.toPx()
                        val normalizedY = (item.weightKg - minW) / range
                        val y = h - (normalizedY * (h - 30.dp.toPx()) + 15.dp.toPx())

                        if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        drawCircle(color = ElectricFlame, radius = 4.dp.toPx(), center = Offset(x, y))
                      }

                      drawPath(
                        path = path,
                        color = ElectricFlame,
                        style = Stroke(width = 3.dp.toPx())
                      )
                    }
                  }
                }
              }
            }
          }
        }

        1 -> {
          // Circumference Measurements View
          item {
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Text("CIRCUMFERENCE TRACKER (CM)", style = MaterialTheme.typography.labelSmall, color = VoltLime, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                MeasurementRow(label = "Chest", current = "104.5 cm", change = "+1.5 cm", isGainPositive = true)
                MeasurementRow(label = "Waist", current = "85.2 cm", change = "-2.1 cm", isGainPositive = false)
                MeasurementRow(label = "Arms (Flexed)", current = "37.8 cm", change = "+0.8 cm", isGainPositive = true)
                MeasurementRow(label = "Hips", current = "98.0 cm", change = "-1.0 cm", isGainPositive = false)
                MeasurementRow(label = "Thighs", current = "58.5 cm", change = "+0.5 cm", isGainPositive = true)
              }
            }
          }
        }

        2 -> {
          // Monthly Calendar & Consistency Grid
          item {
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text("MONTHLY WORKOUT CONSISTENCY", style = MaterialTheme.typography.labelSmall, color = ElectricFlame, fontWeight = FontWeight.Bold)
                  Text("85% On-Track", style = MaterialTheme.typography.labelSmall, color = VoltLime, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Calendar Legend
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                  LegendPill("Completed", VoltLime)
                  LegendPill("Rest Day", Color.Gray)
                  LegendPill("Missed", MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Monthly 28-day Grid Simulation
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                  for (row in 0..3) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                      for (col in 1..7) {
                        val dayNum = row * 7 + col
                        val statusColor = when {
                          dayNum % 7 == 3 || dayNum % 7 == 0 -> Color(0xFF333A48) // Rest day
                          dayNum == 12 || dayNum == 19 -> MaterialTheme.colorScheme.error // Missed
                          dayNum <= 24 -> VoltLime // Completed
                          else -> Color(0xFF1E222B) // Upcoming
                        }

                        Box(
                          modifier = Modifier
                            .size(36.dp)
                            .background(statusColor.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                            .border(1.dp, statusColor, RoundedCornerShape(8.dp)),
                          contentAlignment = Alignment.Center
                        ) {
                          Text(text = "$dayNum", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                      }
                    }
                  }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reschedule Missed Workout suggestion
                Surface(
                  color = Color(0xFF221A1A),
                  shape = RoundedCornerShape(10.dp),
                  border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(Icons.Default.EventRepeat, contentDescription = null, tint = ElectricFlame)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text("Missed Leg Day on Oct 2?", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.White)
                      Text("Tap to reschedule as active superset for tomorrow.", style = MaterialTheme.typography.bodySmall, color = Color.LightGray, fontSize = 11.sp)
                    }
                  }
                }
              }
            }
          }

          // Personal Records (PRs)
          item {
            Text("HALL OF PRs (PERSONAL RECORDS)", style = MaterialTheme.typography.labelMedium, color = VoltLime, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
          }

          items(prs) { pr ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("⚡", fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(pr.exerciseName, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("${pr.repsAchieved} Rep Max", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                  }
                }

                Text(
                  text = "${pr.maxWeightKg} kg",
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.ExtraBold,
                  color = VoltLime
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun MeasurementRow(label: String, current: String, change: String, isGainPositive: Boolean) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.White)
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(current, fontWeight = FontWeight.Bold, color = Color.White)
      Spacer(modifier = Modifier.width(10.dp))
      Surface(
        color = if (change.startsWith("+") == isGainPositive) VoltLime.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.2f),
        shape = RoundedCornerShape(6.dp)
      ) {
        Text(
          text = change,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = if (change.startsWith("+") == isGainPositive) VoltLime else CyberCyan,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

@Composable
fun LegendPill(title: String, color: Color) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
    Spacer(modifier = Modifier.width(4.dp))
    Text(title, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
  }
}
