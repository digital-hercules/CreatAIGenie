// Import necessary libraries and modules
package com.ai.genie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ai.genie.databinding.ActivityMainBinding
import com.ai.genie.ui.home.view.ImageGenerateActivity

// Define MainActivity class that extends AppCompatActivity
class MainActivity : AppCompatActivity() {

    // Initialize views using ViewBinding
    private lateinit var binding: ActivityMainBinding

    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the "Generate Image" button
        binding.generateImage.setOnClickListener {
            startActivity(Intent(this, ImageGenerateActivity::class.java))
        }

        // Set click listener for the "Chat with Bot" button
        binding.chatWithBot.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }


        binding.llTextSpeech.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        binding.llSpeechText.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }
        binding.llTextVideo.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        binding.llTextMusic.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }



    }
}
