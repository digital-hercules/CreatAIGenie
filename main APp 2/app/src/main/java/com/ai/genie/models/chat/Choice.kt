// Import necessary libraries and modules
package com.ai.genie.models.chat

// Define a data class representing a Choice within a chat
data class Choice(
    val finish_reason: String, // Reason for finishing the choice
    val index: Int, // Index of the choice
    val logprobs: Any, // Log probabilities associated with the choice (type may vary)
    val text: String // Textual content of the choice
)
