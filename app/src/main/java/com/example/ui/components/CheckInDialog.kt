package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.ElectricFlame

@Composable
fun AddCheckInDialog(
  currentWeight: Float,
  onDismiss: () -> Unit,
  onSave: (weight: Float, chest: Float?, waist: Float?, arms: Float?, photo: String?, notes: String) -> Unit
) {
  var weightText by remember { mutableStateOf("$currentWeight") }
  var chestText by remember { mutableStateOf("104.5") }
  var waistText by remember { mutableStateOf("85.0") }
  var armsText by remember { mutableStateOf("38.0") }
  var notesText by remember { mutableStateOf("Feeling energized and strong!") }

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
        Text("Daily Check-In & Body Log", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
          value = weightText,
          onValueChange = { weightText = it },
          label = { Text("Weight (kg)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth().testTag("checkin_weight_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          OutlinedTextField(
            value = chestText,
            onValueChange = { chestText = it },
            label = { Text("Chest (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
          OutlinedTextField(
            value = waistText,
            onValueChange = { waistText = it },
            label = { Text("Waist (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = armsText,
          onValueChange = { armsText = it },
          label = { Text("Arms (cm)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = notesText,
          onValueChange = { notesText = it },
          label = { Text("Notes / Mood") },
          modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
          TextButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
            Text("Cancel")
          }
          Button(
            onClick = {
              val w = weightText.toFloatOrNull() ?: currentWeight
              val c = chestText.toFloatOrNull()
              val waist = waistText.toFloatOrNull()
              val a = armsText.toFloatOrNull()
              onSave(w, c, waist, a, null, notesText)
            },
            colors = ButtonDefaults.buttonColors(containerColor = ElectricFlame),
            modifier = Modifier.weight(1f).testTag("save_checkin_btn")
          ) {
            Text("Save Log")
          }
        }
      }
    }
  }
}
