// Import necessary libraries and modules
package com.ai.genie.models

// Define a data class representing a MessageModel
data class MessageModel(
    var isUser: Boolean, // Indicates if the message is from the user
    var isImage: Boolean, // Indicates if the message is an image
    var message: String // The content of the message
)
