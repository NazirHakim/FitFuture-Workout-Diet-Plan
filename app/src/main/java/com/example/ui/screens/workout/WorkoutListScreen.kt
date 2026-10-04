package com.example.ui.screens.workout

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Exercise
import com.example.data.model.WorkoutLocation
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
  exercises: List<Exercise>,
  onExerciseClick: (Exercise) -> Unit,
  onStartRoutine: (String, List<Exercise>) -> Unit
) {
  var selectedCategory by remember { mutableStateOf("All") }
  var searchQuery by remember { mutableStateOf("") }
  var selectedTab by remember { mutableStateOf(0) } // 0: Weekly Splits, 1: Exercise Library, 2: Warm-up & Stretch

  val categories = listOf("All", "Chest", "Back", "Legs", "Shoulders", "Core", "Full Body")

  val filteredExercises = exercises.filter { ex ->
    (selectedCategory == "All" || ex.category.equals(selectedCategory, ignoreCase = true)) &&
    (searchQuery.isBlank() || ex.name.contains(searchQuery, ignoreCase = true) || ex.primaryMuscle.contains(searchQuery, ignoreCase = true))
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    // Screen Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "WORKOUT PROTOCOLS",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricFlame,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Training & Exercises",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      IconButton(
        onClick = { onStartRoutine("Quick Blast", exercises.shuffled().take(4)) },
        modifier = Modifier.testTag("quick_start_btn")
      ) {
        Icon(Icons.Default.FlashOn, contentDescription = "Quick Start", tint = VoltLime)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Tab Selector
    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = ElectricFlame
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = { Text("Weekly Splits", fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = { Text("Exercise Library", fontWeight = FontWeight.Bold) }
      )
      Tab(
        selected = selectedTab == 2,
        onClick = { selectedTab = 2 },
        text = { Text("Mobility & Stretches", fontWeight = FontWeight.Bold) }
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    when (selectedTab) {
      0 -> {
        // Weekly Splits View
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 90.dp)
        ) {
          item {
            RoutineSplitCard(
              dayNumber = "DAY 1",
              title = "Push Hypertrophy (Chest, Shoulders & Triceps)",
              duration = "50 min",
              level = "Intermediate",
              equipment = "Gym Barbell & DBs",
              exercisesCount = 5,
              onStart = {
                val list = exercises.filter { it.category == "Chest" || it.category == "Shoulders" }
                onStartRoutine("Day 1: Push Hypertrophy", if (list.isNotEmpty()) list else exercises.take(4))
              }
            )
          }

          item {
            RoutineSplitCard(
              dayNumber = "DAY 2",
              title = "Pull Power (Back, Rear Delts & Biceps)",
              duration = "45 min",
              level = "Intermediate",
              equipment = "Gym / Pull-up Bar",
              exercisesCount = 4,
              onStart = {
                val list = exercises.filter { it.category == "Back" }
                onStartRoutine("Day 2: Pull Power", if (list.isNotEmpty()) list else exercises.take(4))
              }
            )
          }

          item {
            RoutineSplitCard(
              dayNumber = "DAY 3",
              title = "Lower Body Foundation (Quads, Glutes & Calves)",
              duration = "55 min",
              level = "Advanced",
              equipment = "Squat Rack & Free Weights",
              exercisesCount = 5,
              onStart = {
                val list = exercises.filter { it.category == "Legs" || it.category == "Core" }
                onStartRoutine("Day 3: Lower Body", if (list.isNotEmpty()) list else exercises.take(4))
              }
            )
          }

          item {
            RoutineSplitCard(
              dayNumber = "DAY 4",
              title = "Home Bodyweight Core & HIIT Crucible",
              duration = "30 min",
              level = "All Levels",
              equipment = "Zero Equipment",
              exercisesCount = 4,
              onStart = {
                val list = exercises.filter { it.location == WorkoutLocation.HOME_NO_EQUIPMENT }
                onStartRoutine("Day 4: Home HIIT & Core", if (list.isNotEmpty()) list else exercises.take(4))
              }
            )
          }
        }
      }

      1 -> {
        // Exercise Library
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("Search by exercise or muscle group...") },
          leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { searchQuery = "" }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("exercise_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Category Filter Chips
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { selectedCategory = cat },
              label = { Text(cat) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = ElectricFlame,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("chip_$cat")
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(10.dp),
          contentPadding = PaddingValues(bottom = 90.dp)
        ) {
          items(filteredExercises) { exercise ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(14.dp),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onExerciseClick(exercise) }
                .testTag("exercise_item_${exercise.id}")
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFF2B3342), RoundedCornerShape(10.dp)),
                  contentAlignment = Alignment.Center
                ) {
                  Text(exercise.iconEmoji, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = exercise.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                  )
                  Spacer(modifier = Modifier.height(2.dp))
                  Text(
                    text = "${exercise.category} • Target: ${exercise.primaryMuscle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = ElectricFlame,
                    fontSize = 11.sp
                  )
                  Text(
                    text = "${exercise.defaultSets} Sets × ${exercise.defaultReps} • Rest ${exercise.restSeconds}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray,
                    fontSize = 11.sp
                  )
                }

                Icon(
                  Icons.Default.ChevronRight,
                  contentDescription = "View Details",
                  tint = Color.Gray
                )
              }
            }
          }
        }
      }

      2 -> {
        // Mobility & Stretching Library
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.spacedBy(12.dp),
          contentPadding = PaddingValues(bottom = 90.dp)
        ) {
          item {
            Card(
              colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2329)),
              shape = RoundedCornerShape(14.dp),
              border = BorderStroke(1.dp, VoltLime.copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("🧘", fontSize = 22.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text("Pre-Workout Dynamic Warm-Up", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VoltLime)
                    Text("Elevates core body temp & lubricates synovial joints", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                  }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "1. Arm Circles & Chest Openers (60 sec)\n2. World's Greatest Stretch / Hip Opener (10 reps/side)\n3. Bodyweight Squats & Ankle Mobilization (15 reps)\n4. Band Pull-Aparts for Scapular Activation (20 reps)",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color(0xFFD6DBE2),
                  lineHeight = 20.sp
                )
              }
            }
          }

          item {
            Card(
              colors = CardDefaults.cardColors(containerColor = Color(0xFF221B29)),
              shape = RoundedCornerShape(14.dp),
              border = BorderStroke(1.dp, Color(0xFFB57EDC).copy(alpha = 0.5f)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text("🌊", fontSize = 22.sp)
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text("Post-Workout Parasympathetic Cool-Down", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFFD0BCFF))
                    Text("Down-regulates nervous system & prevents muscle shortening", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                  }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                  text = "1. Standing Hamstring & Calf Fold (45 sec hold)\n2. Prone Cobra & Abdominal Stretch (30 sec)\n3. Doorway Pectoral Stretch (30 sec per side)\n4. Box Breathing (4 sec in, 4 sec hold, 4 sec out) × 5 cycles",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color(0xFFD6DBE2),
                  lineHeight = 20.sp
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
fun RoutineSplitCard(
  dayNumber: String,
  title: String,
  duration: String,
  level: String,
  equipment: String,
  exercisesCount: Int,
  onStart: () -> Unit
) {
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
        Surface(
          color = ElectricFlame.copy(alpha = 0.2f),
          shape = RoundedCornerShape(6.dp)
        ) {
          Text(
            text = dayNumber,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = ElectricFlame,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }

        Text(
          text = "$duration • $level",
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray,
          fontSize = 12.sp
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = "Equipment: $equipment • $exercisesCount Movements",
        style = MaterialTheme.typography.bodySmall,
        color = Color.LightGray
      )

      Spacer(modifier = Modifier.height(14.dp))

      Button(
        onClick = onStart,
        colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("start_split_button_${dayNumber.lowercase()}")
      ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null)
        Spacer(modifier = Modifier.width(6.dp))
        Text("Start Protocol", fontWeight = FontWeight.Bold)
      }
    }
  }
}
