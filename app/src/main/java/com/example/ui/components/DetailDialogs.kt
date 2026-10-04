package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ArticleCard
import com.example.data.model.MealPlanItem
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@Composable
fun ArticleDetailDialog(
  article: ArticleCard,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(article.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              color = ElectricFlame.copy(alpha = 0.2f),
              shape = RoundedCornerShape(6.dp)
            ) {
              Text(
                text = article.actionTag,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricFlame,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(article.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
        Text(article.subtitle, style = MaterialTheme.typography.bodySmall, color = VoltLime)

        Spacer(modifier = Modifier.height(16.dp))

        Text(article.content, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD6DBE2), lineHeight = 22.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Got It")
        }
      }
    }
  }
}

@Composable
fun MealDetailDialog(
  meal: MealPlanItem,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = RoundedCornerShape(18.dp),
      color = MaterialTheme.colorScheme.surface,
      border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
      modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
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
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = ElectricFlame,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
            )
          }

          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(meal.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
        Text("${meal.calories} kcal • P: ${meal.proteinG}g | C: ${meal.carbsG}g | F: ${meal.fatG}g", style = MaterialTheme.typography.bodySmall, color = VoltLime)

        Spacer(modifier = Modifier.height(16.dp))

        Text("INGREDIENTS", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(meal.ingredients, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD6DBE2))

        Spacer(modifier = Modifier.height(16.dp))

        Text("PREPARATION INSTRUCTIONS", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(meal.recipe, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFD6DBE2), lineHeight = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text("Close Recipe")
        }
      }
    }
  }
}
