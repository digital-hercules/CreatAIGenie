// Import necessary libraries and modules
package com.ai.genie.models.chat

// Define a data class representing a ChatModel
data class ChatModel(

    val choices: List<Choice>, // List of choices in the chat

    val created: Int, // Timestamp of when the chat was created

    val id: String, // Unique identifier for the chat

    val model: String, // Model used for the chat

    val objects: String // Additional objects related to the chat
)
