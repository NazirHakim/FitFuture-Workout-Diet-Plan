package com.example.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeliverablesScreen(
  onBack: () -> Unit
) {
  val clipboardManager = LocalClipboardManager.current

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "PLAY STORE DELIVERABLES",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_deliverables")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 1. Play Store Listing Copy Card
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, VoltLime.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("1. GOOGLE PLAY STORE LISTING COPY", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = VoltLime)
          Spacer(modifier = Modifier.height(8.dp))

          DeliverableField(
            label = "App Title (<= 30 chars per Play Policy):",
            value = "IronPulse: AI Fitness & Gym"
          )

          DeliverableField(
            label = "Short Description (<= 80 chars):",
            value = "AI personal trainer, workout tracker, smart nutrition plans & body progress."
          )

          DeliverableField(
            label = "Keywords / Tags:",
            value = "fitness, workout tracker, personal trainer, bodybuilding, gym log, macro counter, health, routine, hiit, hypertrophy"
          )

          Spacer(modifier = Modifier.height(8.dp))
          Text("Full Store Description:", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
          Surface(
            color = Color(0xFF131720),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
          ) {
            Text(
              text = """
Transform your physique with IronPulse — the all-in-one AI personal trainer, workout tracker, and nutrition coach engineered for real, sustainable athletic performance.

🔥 HIGHLIGHTED CAPABILITIES:
• AI Personal Trainer (Coach Alex): 24/7 intelligent coaching, exercise form feedback, progressive overload suggestions, and relentless motivation tailored to your core "Why I Started".
• Custom Workout Splits: Built for Gym, Dumbbells, or Zero-Equipment Bodyweight at Home.
• Active Workout Player: Set & rep tracking, rest countdown timer with haptic cues, PR logger, and real-time calorie burn estimates.
• Interactive Muscle Anatomy: Visual 3D-inspired muscle maps highlighting targeted primary and secondary muscle groups.
• Intelligent Nutrition & Macros: Calorie targets, protein/carb/fat balances, meal recipes, and one-tap water logging.
• Body Transformation Trajectory: Visual before/after morph trajectory at 1, 3, 6, and 12 months.
• Private Photo Vault: Encrypted on-device progress photos with Day 1 vs Current slider comparisons.
• Gamification & Grit: Unlock trophies, level up your athletic XP, and conquer challenges like "Bring Sally Up".

🔒 PRIVACY & MEDICAL DISCLAIMER:
Your health records and photos belong to you. Photos are stored privately on your device. IronPulse is designed for fitness conditioning and does not provide medical diagnoses.
              """.trimIndent(),
              fontSize = 11.sp,
              color = Color(0xFFC7CBD1),
              lineHeight = 16.sp,
              modifier = Modifier.padding(10.dp)
            )
          }
        }
      }

      // 2. Instructions to Generate Signed AAB
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ElectricFlame.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("2. INSTRUCTIONS TO BUILD SIGNED AAB (PLAY BUNDLE)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = ElectricFlame)
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "To publish on Google Play Console, generate an Android App Bundle (.aab):\n\n" +
                   "Step 1: In the terminal, run:\n" +
                   "gradle :app:bundleRelease\n\n" +
                   "Step 2: The signed bundle is produced at:\n" +
                   "app/build/outputs/bundle/release/app-release.aab\n\n" +
                   "Step 3: In Google Play Console:\n" +
                   "• Go to Production / Internal Testing > Create New Release\n" +
                   "• Drag & drop 'app-release.aab'\n" +
                   "• Complete the Data Safety questionnaire (photos encrypted locally, zero unnecessary permissions)\n" +
                   "• Submit for review!",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD6DBE2),
            lineHeight = 18.sp
          )
        }
      }

      // 3. Database Schema Overview
      Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text("3. DATABASE SCHEMA & PERSISTENCE", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CyberCyan)
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "• user_profile: Biometrics, dietary preferences, goals, streak, XP, and core 'why' reason.\n" +
                   "• exercises: 10+ gym & home movements with muscle maps, tips, and rest parameters.\n" +
                   "• workout_logs: Sessions with duration, calories, and completed movements.\n" +
                   "• exercise_prs: Personal records per lift.\n" +
                   "• daily_checkins: Girth measurements, weight, water, and calories.\n" +
                   "• meal_items: Breakfast, Lunch, Dinner, Snack recipe cards & macros.\n" +
                   "• chat_messages: History with Coach Alex.\n" +
                   "• achievements & challenges: Gamification badges and active milestones.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFD6DBE2),
            lineHeight = 18.sp
          )
        }
      }
    }
  }
}

@Composable
fun DeliverableField(label: String, value: String) {
  Column(modifier = Modifier.padding(vertical = 4.dp)) {
    Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
    Surface(
      color = Color(0xFF131720),
      shape = RoundedCornerShape(6.dp),
      modifier = Modifier.fillMaxWidth().padding(top = 2.dp)
    ) {
      Text(
        text = value,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
      )
    }
  }
}
