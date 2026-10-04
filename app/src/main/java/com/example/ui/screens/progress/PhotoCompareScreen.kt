package com.example.ui.screens.progress

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoCompareScreen(
  onBack: () -> Unit
) {
  var sliderPosition by remember { mutableStateOf(0.5f) }
  var showExportSuccess by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "PHOTO COMPARISON VAULT",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
        },
        navigationIcon = {
          IconButton(onClick = onBack, modifier = Modifier.testTag("back_from_compare")) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
          }
        },
        actions = {
          IconButton(onClick = { showExportSuccess = true }) {
            Icon(Icons.Default.Share, contentDescription = "Share Watermarked Card", tint = VoltLime)
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
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Surface(
        color = Color(0xFF1B202A),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2C3547)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Lock, contentDescription = null, tint = VoltLime, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "ENCRYPTED LOCAL STORAGE: Photos are strictly private, never sent to external servers, and secured with in-app encryption.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFC7CBD1),
            fontSize = 11.sp
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column {
          Text("DAY 1 (BASELINE)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
          Text("Aug 15, 2026 • 83.5 kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Column(horizontalAlignment = Alignment.End) {
          Text("CURRENT (DAY 48)", style = MaterialTheme.typography.labelSmall, color = VoltLime)
          Text("Oct 03, 2026 • 78.0 kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = VoltLime)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Morph / Comparison Canvas Container
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(300.dp)
          .background(Color(0xFF131720), RoundedCornerShape(18.dp))
          .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(18.dp))
      ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
          val w = size.width
          val h = size.height

          // Draw Day 1 Silhouette (left side)
          val day1TorsoW = w * 0.38f
          drawRoundRect(
            color = Color(0xFF553A3A),
            topLeft = Offset(w * 0.28f - day1TorsoW / 2f, h * 0.40f),
            size = Size(day1TorsoW, 90.dp.toPx()),
            cornerRadius = CornerRadius(16f, 16f)
          )
          drawCircle(color = Color(0xFF553A3A), radius = 30.dp.toPx(), center = Offset(w * 0.28f, h * 0.24f))

          // Draw Current Lean Athletic Silhouette (right side)
          val currentTorsoW = w * 0.28f
          drawRoundRect(
            color = ElectricFlame.copy(alpha = 0.9f),
            topLeft = Offset(w * 0.72f - currentTorsoW / 2f, h * 0.40f),
            size = Size(currentTorsoW, 90.dp.toPx()),
            cornerRadius = CornerRadius(16f, 16f)
          )
          drawCircle(color = ElectricFlame.copy(alpha = 0.9f), radius = 28.dp.toPx(), center = Offset(w * 0.72f, h * 0.24f))

          // Divider Slider Line
          val sliderX = w * sliderPosition
          drawLine(
            color = VoltLime,
            start = Offset(sliderX, 0f),
            end = Offset(sliderX, h),
            strokeWidth = 3.dp.toPx()
          )
          drawCircle(
            color = VoltLime,
            radius = 12.dp.toPx(),
            center = Offset(sliderX, h * 0.5f)
          )
        }

        // Labels
        Surface(
          color = Color.Black.copy(alpha = 0.7f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(12.dp)
        ) {
          Text("Before: 83.5 kg", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(6.dp))
        }

        Surface(
          color = Color.Black.copy(alpha = 0.7f),
          shape = RoundedCornerShape(6.dp),
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp)
        ) {
          Text("After: 78.0 kg (-5.5kg)", color = VoltLime, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(6.dp))
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Text("Interactive Split Slider", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
      Slider(
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        modifier = Modifier.fillMaxWidth()
      )

      Spacer(modifier = Modifier.height(20.dp))

      Button(
        onClick = { showExportSuccess = true },
        colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
      ) {
        Icon(Icons.Default.Share, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text("Export Watermarked Transformation Card", fontWeight = FontWeight.Bold)
      }

      if (showExportSuccess) {
        Spacer(modifier = Modifier.height(12.dp))
        Card(
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1E281F)),
          border = androidx.compose.foundation.BorderStroke(1.dp, VoltLime)
        ) {
          Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VoltLime)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Transformation graphic generated with #IronPulse watermark!", color = VoltLime, fontSize = 12.sp)
          }
        }
      }
    }
  }
}
