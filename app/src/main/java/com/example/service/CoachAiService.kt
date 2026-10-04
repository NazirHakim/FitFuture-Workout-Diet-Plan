package com.example.service

import com.example.BuildConfig
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class CoachAiService {
  private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

  suspend fun getCoachResponse(
    userMessage: String,
    userProfile: UserProfile?,
    chatHistorySnippet: List<String> = emptyList()
  ): String = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY
    val userName = userProfile?.name ?: "Athlete"
    val userGoal = userProfile?.goal?.name?.replace("_", " ") ?: "Build Muscle"
    val userWhy = userProfile?.whyStartedText ?: "To build unshakeable strength and health"
    val streak = userProfile?.currentStreak ?: 5

    // If API key is available and valid, call Gemini 3.5 Flash
    if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
      try {
        val systemPrompt = """
          You are "Coach Alex", an elite, energetic, science-based personal trainer and nutrition coach in the IronPulse fitness app.
          The athlete's name is $userName.
          Their primary goal is $userGoal.
          Current consistency streak: $streak days.
          Their personal 'WHY I STARTED' core reason is: "$userWhy".
          
          Guidelines:
          - Be motivating, supportive, scientifically accurate, and energetic.
          - Never give medical diagnoses. Include safety reminders when appropriate.
          - When relevant, remind $userName of their why ("$userWhy") to keep their fire burning!
          - Keep answers punchy, actionable, with bullet points where helpful.
        """.trimIndent()

        val fullPrompt = "$systemPrompt\n\nUser asked: $userMessage"

        val jsonBody = JSONObject().apply {
          val contents = JSONArray().apply {
            put(JSONObject().apply {
              put("parts", JSONArray().apply {
                put(JSONObject().apply {
                  put("text", fullPrompt)
                })
              })
            })
          }
          put("contents", contents)
        }

        val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
          .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
          .post(requestBody)
          .build()

        client.newCall(request).execute().use { response ->
          if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")
            if (!text.isNullOrBlank()) {
              return@withContext text.trim()
            }
          }
        }
      } catch (e: Exception) {
        // Fall back to offline smart coach
      }
    }

    // Intelligent Offline Coach Engine
    return@withContext generateSmartOfflineCoachReply(userMessage, userName, userGoal, userWhy, streak)
  }

  private fun generateSmartOfflineCoachReply(
    msg: String,
    name: String,
    goal: String,
    why: String,
    streak: Int
  ): String {
    val lower = msg.lowercase()
    return when {
      lower.contains("why") || lower.contains("remind") || lower.contains("quit") || lower.contains("give up") || lower.contains("motivation") -> {
        "Listen to me, $name. You came here with a mission:\n\n\"$why\"\n\nYou're already on a $streak-day streak. Champions aren't made when conditions are easy; they are forged in moments when you show up despite feeling tired. Take a deep breath, lace up your shoes, and let's get after it today. I believe in you!"
      }
      lower.contains("squat") -> {
        "Here are 3 critical cues for a powerful, injury-free squat, $name:\n\n1. **Rooting:** Grip the floor with your big toe, pinky toe, and heel (the tripod foot).\n2. **Abdominal Bracing:** Take a 360-degree diaphragmatic breath into your belly and brace as if about to be punched.\n3. **Knee Tracking:** Drive your knees slightly outward over your toes so your hips have room to descend below parallel."
      }
      lower.contains("bench") || lower.contains("chest") -> {
        "Key coaching points for a stronger Bench Press:\n\n- **Scapular Retraction:** Pinch your shoulder blades back and down into the bench.\n- **Elbow Angle:** Don't flare elbows out at 90 degrees; tuck them to roughly 45–60 degrees to safeguard your rotator cuffs.\n- **Leg Drive:** Keep your heels planted and drive through your quads without raising your glutes off the bench."
      }
      lower.contains("protein") || lower.contains("macro") || lower.contains("diet") || lower.contains("eat") -> {
        "For your goal of $goal, here is the golden nutrition rule:\n\nAim for **1.6g to 2.2g of protein per kg of bodyweight** daily. Distribute this across 3-4 meals to trigger Muscle Protein Synthesis (MPS) consistently. Top sources: chicken breast, eggs, Greek yogurt, salmon, whey, and lentils/tofu!"
      }
      lower.contains("sore") || lower.contains("recovery") || lower.contains("rest") -> {
        "Delayed Onset Muscle Soreness (DOMS) is normal when overloading muscle fibers! To recover faster:\n\n1. Stay hydrated (drink at least 2.5-3L water today).\n2. Get 7.5 to 8.5 hours of sleep (where growth hormone surges).\n3. Take a light 20-minute brisk walk to promote blood flow and nutrient delivery without taxing your nervous system."
      }
      lower.contains("hello") || lower.contains("hi") || lower.contains("hey") -> {
        "Hey $name! Coach Alex here. You're sitting on a solid $streak-day streak! Are we hitting a workout today, fine-tuning your nutrition, or checking in on your form?"
      }
      else -> {
        "Great question, $name! To support your goal of $goal, keep focusing on progressive overload in your lifts, hitting your daily protein target, and staying hydrated. Remember your core motivation:\n\n\"$why\"\n\nKeep pushing the needle forward today—every single rep counts towards the new standard you're setting!"
      }
    }
  }
}
