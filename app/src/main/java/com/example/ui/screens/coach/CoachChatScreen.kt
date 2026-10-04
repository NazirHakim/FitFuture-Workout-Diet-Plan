package com.example.ui.screens.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricFlame
import com.example.ui.theme.VoltLime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoachChatScreen(
  userProfile: UserProfile?,
  messages: List<ChatMessage>,
  isLoading: Boolean,
  onSendMessage: (String) -> Unit
) {
  var textInput by remember { mutableStateOf("") }
  val listState = rememberLazyListState()

  val promptChips = listOf(
    "Remind me why I started! 🔥",
    "How to fix my squat form?",
    "High protein snack ideas",
    "My muscles are sore today"
  )

  LaunchedEffect(messages.size) {
    if (messages.isNotEmpty()) {
      listState.animateScrollToItem(messages.size - 1)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    // Coach Header
    Surface(
      color = MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(46.dp)
            .background(ElectricFlame, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White, modifier = Modifier.size(26.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Coach Alex", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier.size(8.dp).background(VoltLime, CircleShape))
          }
          Text("AI Fitness & Nutrition Mentor • 24/7 Active", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
        }
      }
    }

    // Message List
    LazyColumn(
      state = listState,
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(vertical = 16.dp)
    ) {
      items(messages) { msg ->
        val isUser = msg.sender.equals("USER", ignoreCase = true)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
        ) {
          if (!isUser) {
            Box(
              modifier = Modifier
                .size(32.dp)
                .background(ElectricFlame, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text("⚡", fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
          }

          Surface(
            color = if (isUser) ElectricFlame else MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(
              topStart = 16.dp,
              topEnd = 16.dp,
              bottomStart = if (isUser) 16.dp else 2.dp,
              bottomEnd = if (isUser) 2.dp else 16.dp
            ),
            border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.widthIn(max = 300.dp)
          ) {
            Text(
              text = msg.message,
              style = MaterialTheme.typography.bodyMedium,
              color = Color.White,
              modifier = Modifier.padding(14.dp),
              lineHeight = 20.sp
            )
          }
        }
      }

      if (isLoading) {
        item {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = ElectricFlame)
            Spacer(modifier = Modifier.width(10.dp))
            Text("Coach Alex is formulating advice...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
          }
        }
      }
    }

    // Quick Prompt Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 12.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      promptChips.forEach { prompt ->
        Surface(
          color = MaterialTheme.colorScheme.surfaceVariant,
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
          modifier = Modifier.clickable {
            onSendMessage(prompt)
          }
        ) {
          Text(
            text = prompt,
            fontSize = 12.sp,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
          )
        }
      }
    }

    // Input Bar
    Surface(
      color = MaterialTheme.colorScheme.surfaceVariant,
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 65.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        OutlinedTextField(
          value = textInput,
          onValueChange = { textInput = it },
          placeholder = { Text("Ask Coach Alex anything...") },
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier
            .weight(1f)
            .testTag("coach_chat_input"),
          singleLine = true
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
          onClick = {
            if (textInput.isNotBlank()) {
              val msg = textInput
              textInput = ""
              onSendMessage(msg)
            }
          },
          colors = IconButtonDefaults.iconButtonColors(containerColor = ElectricFlame),
          modifier = Modifier.testTag("send_coach_chat_btn")
        ) {
          Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
        }
      }
    }
  }
}
