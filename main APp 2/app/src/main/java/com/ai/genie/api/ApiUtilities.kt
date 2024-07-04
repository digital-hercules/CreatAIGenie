// Import necessary libraries and modules
package com.ai.genie.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Create an object for managing API utilities
object ApiUtilities {
    // Define a function to get an instance of the ApiInterface
    fun getApiInterface(): ApiInterface {
        // Build a Retrofit instance with the base URL and Gson converter factory
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/") // Base URL for API requests
            .addConverterFactory(GsonConverterFactory.create()) // Gson converter for JSON parsing
            .build()
            .create(ApiInterface::class.java) // Create an instance of the ApiInterface
    }
}
