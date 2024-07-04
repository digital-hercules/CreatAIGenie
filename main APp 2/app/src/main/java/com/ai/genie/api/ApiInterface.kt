// Import necessary libraries and modules
package com.ai.genie.api

import com.ai.genie.models.chat.ChatModel
import com.ai.genie.ui.home.model.GenerateImageModel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// Define an interface for making API requests
interface ApiInterface {

    // Define a suspend function for making a POST request to get a chat response
    @POST("/v1/completions")
    suspend fun getChat(
        @Header("Content-Type") contentType: String, // Request content type header
        @Header("Authorization") authorization: String, // Authorization header
        @Body requestBody: RequestBody // Request body
    ): ChatModel // Response type is ChatModel

    // Define a suspend function for making a POST request to generate an image
    @POST("/v1/images/generations")
    suspend fun generateImage(
        @Header("Content-Type") contentType: String, // Request content type header
        @Header("Authorization") authorization: String, // Authorization header
        @Body requestBody: RequestBody // Request body
    ): GenerateImageModel // Response type is GenerateImageModel
}
