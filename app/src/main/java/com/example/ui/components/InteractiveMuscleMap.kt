package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@Composable
fun InteractiveMuscleMap(
  primaryMuscle: String,
  secondaryMuscles: String,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
      .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "TARGET MUSCLE ANATOMY",
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 1.sp
      )

      Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .background(ElectricFlame, RoundedCornerShape(2.dp))
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Primary", style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .background(VoltLime, RoundedCornerShape(2.dp))
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "Secondary", style = MaterialTheme.typography.bodySmall, color = Color.White)
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Stylized Anatomy Silhouette Canvas (Front & Back)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .height(140.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Front View Silhouette
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("FRONT", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(modifier = Modifier.size(90.dp, 120.dp)) {
          val w = size.width
          val h = size.height

          val isChest = primaryMuscle.contains("Chest", true) || primaryMuscle.contains("Pectoral", true)
          val isShoulders = primaryMuscle.contains("Shoulder", true) || primaryMuscle.contains("Deltoid", true) || secondaryMuscles.contains("Deltoid", true)
          val isQuads = primaryMuscle.contains("Quad", true) || primaryMuscle.contains("Leg", true)
          val isAbs = primaryMuscle.contains("Ab", true) || primaryMuscle.contains("Core", true) || secondaryMuscles.contains("Core", true)
          val isArms = primaryMuscle.contains("Bicep", true) || secondaryMuscles.contains("Bicep", true) || secondaryMuscles.contains("Tricep", true)

          // Head
          drawCircle(
            color = Color(0xFF3A4250),
            radius = w * 0.12f,
            center = Offset(w * 0.5f, h * 0.12f)
          )

          // Shoulders / Deltoids
          val shoulderColor = if (isShoulders) ElectricFlame else Color(0xFF2C3340)
          drawRoundRect(
            color = shoulderColor,
            topLeft = Offset(w * 0.2f, h * 0.22f),
            size = Size(w * 0.6f, h * 0.1f),
            cornerRadius = CornerRadius(8f, 8f)
          )

          // Chest
          val chestColor = if (isChest) ElectricFlame else if (secondaryMuscles.contains("Chest", true)) VoltLime else Color(0xFF2C3340)
          drawRoundRect(
            color = chestColor,
            topLeft = Offset(w * 0.3f, h * 0.32f),
            size = Size(w * 0.4f, h * 0.12f),
            cornerRadius = CornerRadius(6f, 6f)
          )

          // Arms
          val armColor = if (isArms) VoltLime else Color(0xFF252B36)
          drawRoundRect(
            color = armColor,
            topLeft = Offset(w * 0.16f, h * 0.32f),
            size = Size(w * 0.11f, h * 0.32f),
            cornerRadius = CornerRadius(4f, 4f)
          )
          drawRoundRect(
            color = armColor,
            topLeft = Offset(w * 0.73f, h * 0.32f),
            size = Size(w * 0.11f, h * 0.32f),
            cornerRadius = CornerRadius(4f, 4f)
          )

          // Core / Abs
          val absColor = if (isAbs) (if (primaryMuscle.contains("Ab", true)) ElectricFlame else VoltLime) else Color(0xFF242A35)
          drawRoundRect(
            color = absColor,
            topLeft = Offset(w * 0.34f, h * 0.45f),
            size = Size(w * 0.32f, h * 0.15f),
            cornerRadius = CornerRadius(4f, 4f)
          )

          // Quads / Legs
          val quadColor = if (isQuads) ElectricFlame else Color(0xFF282F3B)
          drawRoundRect(
            color = quadColor,
            topLeft = Offset(w * 0.3f, h * 0.62f),
            size = Size(w * 0.18f, h * 0.35f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          drawRoundRect(
            color = quadColor,
            topLeft = Offset(w * 0.52f, h * 0.62f),
            size = Size(w * 0.18f, h * 0.35f),
            cornerRadius = CornerRadius(6f, 6f)
          )
        }
      }

      // Back View Silhouette
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("BACK", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Canvas(modifier = Modifier.size(90.dp, 120.dp)) {
          val w = size.width
          val h = size.height

          val isBack = primaryMuscle.contains("Back", true) || primaryMuscle.contains("Lat", true) || primaryMuscle.contains("Erector", true)
          val isGlutes = primaryMuscle.contains("Glute", true) || secondaryMuscles.contains("Glute", true) || primaryMuscle.contains("Squat", true)
          val isHamstrings = primaryMuscle.contains("Hamstring", true) || secondaryMuscles.contains("Hamstring", true)

          // Head back
          drawCircle(
            color = Color(0xFF3A4250),
            radius = w * 0.12f,
            center = Offset(w * 0.5f, h * 0.12f)
          )

          // Upper Back / Traps
          val trapColor = if (isBack) ElectricFlame else Color(0xFF2C3340)
          drawRoundRect(
            color = trapColor,
            topLeft = Offset(w * 0.26f, h * 0.22f),
            size = Size(w * 0.48f, h * 0.14f),
            cornerRadius = CornerRadius(6f, 6f)
          )

          // Lats
          val latColor = if (isBack) ElectricFlame else Color(0xFF242A35)
          drawRoundRect(
            color = latColor,
            topLeft = Offset(w * 0.28f, h * 0.37f),
            size = Size(w * 0.44f, h * 0.15f),
            cornerRadius = CornerRadius(6f, 6f)
          )

          // Glutes
          val gluteColor = if (isGlutes) VoltLime else Color(0xFF262C37)
          drawRoundRect(
            color = gluteColor,
            topLeft = Offset(w * 0.28f, h * 0.53f),
            size = Size(w * 0.44f, h * 0.12f),
            cornerRadius = CornerRadius(6f, 6f)
          )

          // Hamstrings
          val hamColor = if (isHamstrings) (if (primaryMuscle.contains("Hamstring", true)) ElectricFlame else VoltLime) else Color(0xFF282F3B)
          drawRoundRect(
            color = hamColor,
            topLeft = Offset(w * 0.3f, h * 0.66f),
            size = Size(w * 0.18f, h * 0.32f),
            cornerRadius = CornerRadius(6f, 6f)
          )
          drawRoundRect(
            color = hamColor,
            topLeft = Offset(w * 0.52f, h * 0.66f),
            size = Size(w * 0.18f, h * 0.32f),
            cornerRadius = CornerRadius(6f, 6f)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    Text(
      text = "Primary: $primaryMuscle",
      style = MaterialTheme.typography.bodyMedium,
      color = ElectricFlame
    )
    if (secondaryMuscles.isNotBlank()) {
      Text(
        text = "Secondary: $secondaryMuscles",
        style = MaterialTheme.typography.bodySmall,
        color = VoltLime
      )
    }
  }
}
