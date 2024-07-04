// Import necessary libraries and modules
package com.ai.genie.models.request

import com.ai.genie.models.Message

// Define a data class representing a ChatRequest
data class ChatRequest(
    val max_tokens: Int, // Maximum number of tokens in the response
    val model: String, // Model to be used for the chat
    val messages: List<Message>, // List of messages with roles
    val temperature: Double // Temperature parameter for randomness in the response
)
