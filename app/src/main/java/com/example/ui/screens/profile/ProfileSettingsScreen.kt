package com.example.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
  userProfile: UserProfile?,
  onDeleteAccount: () -> Unit,
  onUnitToggle: (Boolean) -> Unit,
  onNotificationToggle: (Boolean, String) -> Unit,
  onOpenDeliverables: () -> Unit
) {
  var showDeleteDialog by remember { mutableStateOf(false) }
  var showPrivacyPolicy by remember { mutableStateOf(false) }
  var showHealthDisclaimer by remember { mutableStateOf(false) }

  val useMetric = userProfile?.useMetricUnits ?: true
  val notifEnabled = userProfile?.notificationsEnabled ?: true
  val notifTime = userProfile?.notificationTime ?: "09:00 AM"

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
      .padding(horizontal = 16.dp)
      .verticalScroll(rememberScrollState())
      .padding(bottom = 90.dp)
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
          text = "ACCOUNT & COMPLIANCE",
          style = MaterialTheme.typography.labelSmall,
          color = ElectricFlame,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
        Text(
          text = "Settings & Privacy",
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.ExtraBold,
          color = Color.White
        )
      }

      Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = CircleShape,
        modifier = Modifier.size(44.dp)
      ) {
        Box(contentAlignment = Alignment.Center) {
          Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Profile Card
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(16.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(54.dp)
            .background(ElectricFlame, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(userProfile?.name?.take(1) ?: "A", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Text(userProfile?.name ?: "Athlete", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
          Text(userProfile?.email ?: "athlete@ironpulse.fit", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          Spacer(modifier = Modifier.height(4.dp))
          Text("Goal: ${userProfile?.goal?.name?.replace("_", " ")}", fontSize = 11.sp, color = VoltLime, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Google Play Store Publishing & Deliverables banner
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFF1E281F)),
      shape = RoundedCornerShape(14.dp),
      border = BorderStroke(1.dp, VoltLime),
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onOpenDeliverables() }
        .testTag("open_deliverables_banner")
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = VoltLime)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text("Google Play Console Deliverables", fontWeight = FontWeight.Bold, color = Color.White)
          Text("Store listing copy, AAB signing instructions & policies", style = MaterialTheme.typography.bodySmall, color = VoltLime)
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = VoltLime)
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Preferences Section
    Text("APP PREFERENCES", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        // Metric / Imperial Toggle
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Unit System", fontWeight = FontWeight.SemiBold, color = Color.White)
            Text(if (useMetric) "Metric (kg, cm, ml)" else "Imperial (lbs, in, oz)", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
          Switch(
            checked = useMetric,
            onCheckedChange = { onUnitToggle(it) },
            colors = SwitchDefaults.colors(checkedThumbColor = ElectricFlame, checkedTrackColor = ElectricFlame.copy(alpha = 0.4f))
          )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outline)

        // Push Notifications
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text("Daily Motivation & Reminders", fontWeight = FontWeight.SemiBold, color = Color.White)
            Text("Scheduled daily at $notifTime", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
          Switch(
            checked = notifEnabled,
            onCheckedChange = { onNotificationToggle(it, notifTime) },
            colors = SwitchDefaults.colors(checkedThumbColor = VoltLime, checkedTrackColor = VoltLime.copy(alpha = 0.4f))
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Privacy & Safety Section
    Text("PRIVACY & GOOGLE PLAY COMPLIANCE", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))

    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(14.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(horizontal = 14.dp)) {
        SettingsRow(
          title = "Privacy Policy & Data Safety",
          subtitle = "Zero tracking • Encrypted local photos",
          icon = Icons.Default.PrivacyTip,
          onClick = { showPrivacyPolicy = true }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        SettingsRow(
          title = "Medical & Health Disclaimer",
          subtitle = "Safety guardrails & physician advisory",
          icon = Icons.Default.MedicalServices,
          onClick = { showHealthDisclaimer = true }
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.outline)
        SettingsRow(
          title = "Delete Account & All Data",
          subtitle = "Irreversible data erasure (Play Policy requirement)",
          icon = Icons.Default.DeleteForever,
          tint = MaterialTheme.colorScheme.error,
          onClick = { showDeleteDialog = true }
        )
      }
    }
  }

  // Delete Account Confirmation Dialog
  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = { Text("Delete Account and All Data?") },
      text = {
        Text("This will permanently erase your profile, workout logs, PR records, and progress photos from this device. Per Google Play Data Safety policies, this action cannot be undone.")
      },
      confirmButton = {
        Button(
          onClick = {
            showDeleteDialog = false
            onDeleteAccount()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
          modifier = Modifier.testTag("confirm_delete_account_btn")
        ) {
          Text("Permanently Delete")
        }
      },
      dismissButton = {
        TextButton(onClick = { showDeleteDialog = false }) {
          Text("Cancel")
        }
      }
    )
  }

  // Privacy Policy Dialog
  if (showPrivacyPolicy) {
    AlertDialog(
      onDismissRequest = { showPrivacyPolicy = false },
      title = { Text("Privacy Policy & Data Safety") },
      text = {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
          Text(
            text = "Effective Date: October 2026\n\n1. DATA COLLECTION & USE:\nIronPulse collects fitness preferences, workout history, and optional body metrics solely to personalize your training routines and nutrition goals.\n\n2. PHOTO PRIVACY:\nProgress photos uploaded by the user are stored privately in encrypted device storage. Photos are NEVER made public, NEVER used for commercial advertisements, and NEVER shared with third parties or AI training models without explicit consent.\n\n3. ZERO UNNECESSARY PERMISSIONS:\nIronPulse adheres to Google Play's principle of least privilege. Photo selection utilizes Android Photo Picker without requiring broad storage permissions.\n\n4. RIGHT TO DELETION:\nUsers can delete their entire account and all associated records at any moment directly inside the Settings tab.",
            style = MaterialTheme.typography.bodySmall,
            lineHeight = 16.sp
          )
        }
      },
      confirmButton = {
        Button(onClick = { showPrivacyPolicy = false }) {
          Text("Close")
        }
      }
    )
  }

  // Medical Disclaimer Dialog
  if (showHealthDisclaimer) {
    AlertDialog(
      onDismissRequest = { showHealthDisclaimer = false },
      title = { Text("Medical & Health Disclaimer") },
      text = {
        Text(
          text = "IronPulse is not a medical device, and the workout protocols, macro calculations, and AI personal trainer recommendations provided within the app are strictly for informational and athletic conditioning purposes only.\n\nAlways consult a licensed medical doctor or certified healthcare practitioner prior to beginning any high-intensity exercise regimen, particularly if you have pre-existing cardiovascular, musculoskeletal, or metabolic conditions.\n\nIronPulse enforces strict safety guardrails prohibiting extreme caloric restriction (<1,200 kcal/day).",
          style = MaterialTheme.typography.bodySmall,
          lineHeight = 16.sp
        )
      },
      confirmButton = {
        Button(onClick = { showHealthDisclaimer = false }) {
          Text("Understood")
        }
      }
    )
  }
}

@Composable
fun SettingsRow(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  tint: Color = Color.White,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .padding(vertical = 12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
    Spacer(modifier = Modifier.width(14.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(title, fontWeight = FontWeight.SemiBold, color = tint)
      Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp)
    }
    Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
  }
}
