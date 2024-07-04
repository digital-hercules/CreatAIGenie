// Import necessary libraries and modules
package com.ai.genie.models

import com.google.gson.annotations.SerializedName

// Define a data class representing a ChatResponse
data class ChatResponse(
	@field:SerializedName("created")
	val created: Int? = null, // Timestamp of when the chat response was created

	@field:SerializedName("usage")
	val usage: Usage? = null, // Usage statistics for the chat response

	@field:SerializedName("model")
	val model: String? = null, // Model used for the chat

	@field:SerializedName("id")
	val id: String? = null, // Unique identifier for the chat response

	@field:SerializedName("choices")
	val choices: List<ChoicesItem?>? = null, // List of choices in the chat response

	@field:SerializedName("object")
	val objectName: String? = null // Object name associated with the chat response
)

// Define a data class representing a choice within a chat response
data class ChoicesItem(
	@field:SerializedName("finish_reason")
	val finishReason: String? = null, // Reason for finishing the choice

	@field:SerializedName("index")
	val index: Int? = null, // Index of the choice

	@field:SerializedName("message")
	val message: Message? = null // Message content associated with the choice
)

// Define a data class representing usage statistics for a chat response
data class Usage(
	@field:SerializedName("completion_tokens")
	val completionTokens: Int? = null, // Number of completion tokens used

	@field:SerializedName("prompt_tokens")
	val promptTokens: Int? = null, // Number of prompt tokens used

	@field:SerializedName("total_tokens")
	val totalTokens: Int? = null // Total number of tokens used
)

// Define a data class representing a message within a choice
data class Message(
	@field:SerializedName("role")
	val role: String? = null, // Role of the message (e.g., "system", "user", "assistant")

	@field:SerializedName("content")
	val content: String? = null // Content of the message
)
