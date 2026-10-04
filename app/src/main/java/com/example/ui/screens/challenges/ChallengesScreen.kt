package com.example.ui.screens.challenges

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Achievement
import com.example.data.model.Challenge
import com.example.data.model.UserProfile
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesScreen(
  userProfile: UserProfile?,
  challenges: List<Challenge>,
  achievements: List<Achievement>
) {
  var selectedTab by remember { mutableStateOf(0) } // 0: Challenges, 1: Trophy Room, 2: Leaderboard
  val xp = userProfile?.xpPoints ?: 1240
  val level = userProfile?.userLevel ?: 4

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
          text = "GAMIFICATION & GRIT",
          style = MaterialTheme.typography.labelSmall,
          color = VoltLime,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Challenges & Trophies",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      Surface(
        color = Color(0xFF1E281F),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, VoltLime)
      ) {
        Column(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text("$xp XP", fontWeight = FontWeight.ExtraBold, color = VoltLime, fontSize = 14.sp)
          Text("Level $level Athlete", fontSize = 10.sp, color = Color.LightGray)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    PrimaryTabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.background,
      contentColor = ElectricFlame
    ) {
      Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Active Challenges", fontWeight = FontWeight.Bold) })
      Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Trophy Room", fontWeight = FontWeight.Bold) })
      Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Leaderboard", fontWeight = FontWeight.Bold) })
    }

    Spacer(modifier = Modifier.height(14.dp))

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(bottom = 90.dp)
    ) {
      when (selectedTab) {
        0 -> {
          // Challenges List
          items(challenges) { challenge ->
            Card(
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
              shape = RoundedCornerShape(16.dp),
              border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
              modifier = Modifier.fillMaxWidth().testTag("challenge_${challenge.id}")
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(challenge.badgeIcon, fontSize = 26.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(challenge.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                      Text(challenge.subtitle, style = MaterialTheme.typography.bodySmall, color = ElectricFlame)
                    }
                  }

                  Surface(
                    color = VoltLime.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                  ) {
                    Text("+${challenge.xpReward} XP", color = VoltLime, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(challenge.description, style = MaterialTheme.typography.bodySmall, color = Color.LightGray)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "Progress: ${challenge.currentProgressDays} / ${challenge.targetDays} Days",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                  )
                  Text(
                    text = "${(challenge.currentProgressDays * 100 / challenge.targetDays.coerceAtLeast(1))}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = VoltLime,
                    fontWeight = FontWeight.Bold
                  )
                }

                LinearProgressIndicator(
                  progress = { (challenge.currentProgressDays / challenge.targetDays.toFloat()).coerceIn(0f, 1f) },
                  color = VoltLime,
                  trackColor = MaterialTheme.colorScheme.surface,
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .height(6.dp)
                )
              }
            }
          }
        }

        1 -> {
          // Achievements Trophy Room
          items(achievements) { badge ->
            Card(
              colors = CardDefaults.cardColors(
                containerColor = if (badge.isUnlocked) Color(0xFF1B2329) else MaterialTheme.colorScheme.surfaceVariant
              ),
              shape = RoundedCornerShape(14.dp),
              border = BorderStroke(
                1.dp,
                if (badge.isUnlocked) VoltLime.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline
              ),
              modifier = Modifier.fillMaxWidth().testTag("badge_${badge.id}")
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(46.dp)
                    .background(
                      if (badge.isUnlocked) VoltLime.copy(alpha = 0.2f) else Color(0xFF282F3B),
                      CircleShape
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  if (badge.isUnlocked) {
                    Text(badge.icon, fontSize = 24.sp)
                  } else {
                    Icon(Icons.Default.Lock, contentDescription = "Locked", tint = Color.Gray)
                  }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(badge.title, fontWeight = FontWeight.Bold, color = if (badge.isUnlocked) Color.White else Color.Gray)
                    if (badge.isUnlocked) {
                      Spacer(modifier = Modifier.width(6.dp))
                      Text("✓ UNLOCKED", fontSize = 10.sp, color = VoltLime, fontWeight = FontWeight.Bold)
                    }
                  }
                  Text(badge.description, style = MaterialTheme.typography.bodySmall, color = if (badge.isUnlocked) Color.LightGray else Color(0xFF757D8A))
                }

                Surface(
                  color = MaterialTheme.colorScheme.surface,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "+${badge.xpReward} XP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (badge.isUnlocked) VoltLime else Color.Gray,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }

        2 -> {
          // Opt-in Leaderboard Simulation (strict privacy: usernames only, no photos)
          item {
            Surface(
              color = Color(0xFF1B202A),
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "🛡️ PRIVACY FIRST: Community leaderboard is opt-in, displaying usernames and consistency points only. Personal photos and health records are strictly private and never shared.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                fontSize = 11.sp,
                modifier = Modifier.padding(12.dp)
              )
            }
          }

          items(listOf(
            Triple("1. TitanVance", "14,800 XP", "👑 Rank 1"),
            Triple("2. IronValkyrie", "12,450 XP", "🥈 Rank 2"),
            Triple("3. CyberPulse99", "11,200 XP", "🥉 Rank 3"),
            Triple("4. Alex Vance (You)", "$xp XP", "⚡ Rank 4"),
            Triple("5. ApexPredator", "9,850 XP", "Rank 5")
          )) { (name, score, rank) ->
            val isUser = name.contains("You")
            Card(
              colors = CardDefaults.cardColors(
                containerColor = if (isUser) ElectricFlame.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
              ),
              border = BorderStroke(1.dp, if (isUser) ElectricFlame else MaterialTheme.colorScheme.outline),
              shape = RoundedCornerShape(10.dp),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(name, fontWeight = if (isUser) FontWeight.ExtraBold else FontWeight.Bold, color = if (isUser) ElectricFlame else Color.White)
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(score, fontWeight = FontWeight.Bold, color = VoltLime)
                  Spacer(modifier = Modifier.width(10.dp))
                  Text(rank, fontSize = 11.sp, color = Color.Gray)
                }
              }
            }
          }
        }
      }
    }
  }
}
