package com.ai.genie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ai.genie.adapter.MessageAdapter1
import com.ai.genie.adapter.MessageClickListener
import com.ai.genie.databinding.ActivityChatBinding
import com.ai.genie.models.ChatResponse
import com.ai.genie.models.MessageModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

// Define NewChatActivity class that extends AppCompatActivity and implements MessageClickListener
class ChatActivity : AppCompatActivity(), MessageClickListener {

    // Initialize views and variables
    private lateinit var binding: ActivityChatBinding
    private var list = ArrayList<MessageModel>()
    private lateinit var adapter: MessageAdapter1
    private var mlayoutManager: LinearLayoutManager? = null

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the back button
        binding.backBtn.setOnClickListener { finish() }

        // Initialize the adapter and set it to the RecyclerView
        adapter = MessageAdapter1(list, this)
        binding.recyclerView.adapter = adapter

        // Initialize and configure the layout manager
        mlayoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        // Set the layout manager to the RecyclerView
        binding.recyclerView.layoutManager = mlayoutManager

        // Set click listener for the send button
        binding.sendbtn.setOnClickListener {
            if (binding.userMsg.text!!.isEmpty()) {
                Toast.makeText(this, "Please input a message", Toast.LENGTH_SHORT).show()
            } else {
                // Call the `callApi()` function to send the user's message to the AI model
                callApi()
            }
        }

        // Set click listener for the back button (optional)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    // Function to make an API call and handle the response
    private fun callApi() {
        val content = binding.userMsg.text.toString()

        // Add the user's message to the list and notify the adapter
        list.add(MessageModel(true, false, content))
        adapter.notifyItemInserted(list.size - 1)

        // Clear the user's input
        binding.userMsg.text?.clear()

        // Scroll to the last message
        binding.recyclerView.scrollToPosition(list.size - 1)

        // Launch a coroutine in the IO context
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Create an OkHttpClient and build a POST request
                val client = OkHttpClient()
                val mediaType = "application/json".toMediaType()
                val body =
                    "{\r\n     \"model\": \"gpt-4-1106-preview\",\r\n     \"messages\": [{\"role\": \"user\", \"content\": \"${content}\"}],\r\n     \"temperature\": 0.7\r\n   }".toRequestBody(
                        mediaType
                    )
                Log.e("abc","==============body========="+body)
                val request = Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer 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"
                    )
                    .build()

                // Execute the request and get the response
                val response = client.newCall(request).execute()

                // Parse the JSON response
                val jsonResponse = response.body?.string()
                val chatModel = Gson().fromJson(jsonResponse, ChatResponse::class.java)
                val textResponse = chatModel.choices?.first()?.message?.content

                // Add the AI's response to the list and notify the adapter
                list.add(MessageModel(false, false, textResponse.toString()))

                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    binding.userMsg.text?.clear()
                    adapter.notifyItemInserted(list.size - 1)
                    binding.recyclerView.scrollToPosition(list.size - 1)
                }
            } catch (e: Exception) {
                // Handle exceptions and display a toast message
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ChatActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Implement the image download click listener (empty implementation)
    override fun onImageDownloadClick(position: Int, message: String) {
        // Handle image download click here if needed
    }
}
