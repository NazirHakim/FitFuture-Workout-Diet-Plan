package com.example.ui.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ArticleCard
import com.example.data.model.DailyCheckIn
import com.example.data.model.Exercise
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@Composable
fun DashboardScreen(
  userProfile: UserProfile?,
  todayCheckIn: DailyCheckIn?,
  exercises: List<Exercise>,
  articles: List<ArticleCard>,
  onStartWorkout: (List<Exercise>) -> Unit,
  onLogMeal: () -> Unit,
  onAddPhoto: () -> Unit,
  onChatWithCoach: () -> Unit,
  onWaterAdd: (Int) -> Unit,
  onArticleClick: (ArticleCard) -> Unit,
  onOpenSettings: () -> Unit
) {
  val scrollState = rememberScrollState()
  val userName = userProfile?.name ?: "Athlete"
  val streak = userProfile?.currentStreak ?: 5
  val xp = userProfile?.xpPoints ?: 1240
  val level = userProfile?.userLevel ?: 4
  val whyStarted = userProfile?.whyStartedText ?: "To build unshakeable strength and health for a lifetime."

  val waterDrunk = todayCheckIn?.waterDrunkMl ?: 2250
  val waterTarget = userProfile?.waterTargetMl ?: 3000
  val caloriesBurned = 580
  val caloriesConsumed = todayCheckIn?.caloriesLogged ?: 1850

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .verticalScroll(scrollState)
      .padding(bottom = 80.dp)
  ) {
    // Top Athletic Header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(180.dp)
    ) {
      // Background Image with gradient overlay
      Image(
        painter = painterResource(id = R.drawable.hero_fitness),
        contentDescription = "Fitness Hero Banner",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black.copy(alpha = 0.85f),
                MaterialTheme.colorScheme.background
              )
            )
          )
      )

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Bottom
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "WELCOME BACK,",
              style = MaterialTheme.typography.labelMedium,
              color = ElectricFlame,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = userName,
              style = MaterialTheme.typography.headlineSmall,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
          }

          // Streak & Level Badges
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(
              color = Color(0xFF231610),
              shape = RoundedCornerShape(20.dp),
              border = BorderStroke(1.dp, ElectricFlame)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("🔥", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$streak d",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = ElectricFlame
                )
              }
            }

            Surface(
              color = Color(0xFF1E281F),
              shape = RoundedCornerShape(20.dp),
              border = BorderStroke(1.dp, VoltLime)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text("⚡", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Lvl $level",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = VoltLime
                )
              }
            }

            IconButton(
              onClick = onOpenSettings,
              modifier = Modifier.size(32.dp).testTag("open_settings_button")
            ) {
              Icon(Icons.Default.Settings, contentDescription = "Settings & Compliance", tint = Color.White)
            }
          }
        }
      }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
      // "YOUR WHY" Reminder Card
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Stars, contentDescription = null, tint = VoltLime, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "YOUR 'WHY' REMINDER",
              style = MaterialTheme.typography.labelMedium,
              color = VoltLime,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
          }
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "\"$whyStarted\"",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White
          )
        }
      }

      // Quick Action Buttons Grid
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        QuickActionButton(
          title = "Start Lift",
          icon = Icons.Default.PlayArrow,
          color = ElectricFlame,
          modifier = Modifier
            .weight(1f)
            .testTag("quick_start_workout"),
          onClick = { onStartWorkout(exercises.take(5)) }
        )
        QuickActionButton(
          title = "Log Meal",
          icon = Icons.Default.Restaurant,
          color = VoltLime,
          modifier = Modifier
            .weight(1f)
            .testTag("quick_log_meal"),
          onClick = onLogMeal
        )
        QuickActionButton(
          title = "Check In",
          icon = Icons.Default.CameraAlt,
          color = CyberCyan,
          modifier = Modifier
            .weight(1f)
            .testTag("quick_add_photo"),
          onClick = onAddPhoto
        )
        QuickActionButton(
          title = "Coach AI",
          icon = Icons.Default.Psychology,
          color = Color(0xFFD0BCFF),
          modifier = Modifier
            .weight(1f)
            .testTag("quick_chat_coach"),
          onClick = onChatWithCoach
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Today's Scheduled Workout Card
      Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B202A)),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ElectricFlame.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "TODAY'S WORKOUT",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricFlame,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
              )
              Text(
                text = "Chest & Triceps Hypertrophy",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }
            Surface(
              color = ElectricFlame.copy(alpha = 0.2f),
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "45 Min • 5 Exercises",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricFlame,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Barbell Flat Bench, Incline DB Press, Push-Ups, Dips & Cable Pushdowns",
            style = MaterialTheme.typography.bodySmall,
            color = Color.LightGray
          )

          Spacer(modifier = Modifier.height(14.dp))
          Button(
            onClick = { onStartWorkout(exercises.take(5)) },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(46.dp)
              .testTag("launch_workout_button")
          ) {
            Icon(Icons.Default.PlayCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Begin Workout Player", fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Nutrition & Hydration Summary
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        // Calorie Card
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = ElectricFlame, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Calories", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "$caloriesConsumed kcal",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            LinearProgressIndicator(
              progress = { (caloriesConsumed / 2400f).coerceIn(0f, 1f) },
              color = ElectricFlame,
              trackColor = MaterialTheme.colorScheme.surface,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(6.dp)
            )
            Text(
              text = "Target: 2,400 kcal",
              style = MaterialTheme.typography.bodySmall,
              color = Color.Gray,
              fontSize = 10.sp,
              modifier = Modifier.padding(top = 4.dp)
            )
          }
        }

        // Hydration Card
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Default.WaterDrop, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("Water", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "${(waterDrunk / 1000f)} / ${(waterTarget / 1000f)} L",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.ExtraBold,
              color = Color.White
            )
            LinearProgressIndicator(
              progress = { (waterDrunk / waterTarget.toFloat()).coerceIn(0f, 1f) },
              color = CyberCyan,
              trackColor = MaterialTheme.colorScheme.surface,
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(6.dp)
            )
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${(waterDrunk * 100 / waterTarget)}% of goal",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                fontSize = 10.sp
              )
              Text(
                text = "+250ml",
                color = CyberCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                modifier = Modifier
                  .clickable { onWaterAdd(250) }
                  .testTag("quick_add_water_250")
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Daily Knowledge & Athlete Carousel
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "DAILY KNOWLEDGE & MOTIVATION",
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(articles) { article ->
          Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier
              .width(280.dp)
              .height(160.dp)
              .clickable { onArticleClick(article) }
              .testTag("article_card_${article.id}")
          ) {
            Column(
              modifier = Modifier
                .padding(14.dp)
                .fillMaxHeight(),
              verticalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(article.iconEmoji, fontSize = 20.sp)
                  Spacer(modifier = Modifier.width(8.dp))
                  Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text(
                      text = article.actionTag,
                      fontSize = 10.sp,
                      color = ElectricFlame,
                      fontWeight = FontWeight.Bold,
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                  }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = article.title,
                  style = MaterialTheme.typography.titleSmall,
                  fontWeight = FontWeight.Bold,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = article.content,
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.LightGray,
                  maxLines = 3,
                  overflow = TextOverflow.Ellipsis
                )
              }
              Text(
                text = "Read Breakdown →",
                style = MaterialTheme.typography.labelSmall,
                color = ElectricFlame,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun QuickActionButton(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  Surface(
    color = MaterialTheme.colorScheme.surfaceVariant,
    shape = RoundedCornerShape(12.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    modifier = modifier.clickable { onClick() }
  ) {
    Column(
      modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .background(color.copy(alpha = 0.2f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = Color.White
      )
    }
  }
}
