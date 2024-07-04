// Import necessary libraries and modules
package com.ai.genie.models.request

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

// Define a data class representing an ImageGenerateRequest
@Keep
@Parcelize
data class ImageGenerateRequest(
    @SerializedName("n")
    val n: Int, // Number of images to generate
    @SerializedName("prompt")
    val prompt: String, // Prompt for generating images
    @SerializedName("size")
    val size: String // Size parameter for image generation
) : Parcelable
