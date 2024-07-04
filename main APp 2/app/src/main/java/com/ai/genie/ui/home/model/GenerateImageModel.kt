// Import necessary libraries and modules
package com.ai.genie.ui.home.model

import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

// Define a data class representing a GenerateImageModel
@Keep
@Parcelize
data class GenerateImageModel(
    @SerializedName("created")
    val created: Int, // Timestamp of when the image was created

    @SerializedName("data")
    val `data`: List<Data> // List of image data objects
) : Parcelable {

    // Define a data class representing image data
    @Keep
    @Parcelize
    data class Data(
        @SerializedName("url")
        val url: String // URL of the generated image
    ) : Parcelable
}
