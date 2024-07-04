package com.ai.genie.ui.authentication.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.ai.genie.R
import com.ai.genie.common.ConstantValue
import com.ai.genie.common.SaveData
import com.bumptech.glide.Glide
import com.ai.genie.ui.home.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth

// Define SplashActivity class that extends AppCompatActivity
class SplashActivity : AppCompatActivity() {
    private lateinit var imageView2: ImageView
    private lateinit var saveData: SaveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContentView(R.layout.activity_splash) // Set the content view to the splash layout

        saveData = SaveData(this)
        imageView2 = findViewById(R.id.imageView2)
        Glide.with(this).load(R.drawable.logo_ani).into(imageView2)
        // Use a Handler to delay the execution of code for 1500 milliseconds (1.5 seconds)
        Handler(mainLooper).postDelayed({
            Log.e("abc", "=======ONBOARDING========" + saveData.getInt(ConstantValue.ONBOARDING))
            if (saveData.getInt(ConstantValue.ONBOARDING) == 1) {
                if (FirebaseAuth.getInstance().currentUser != null) {
                    // Start the MainActivity and finish the current SplashActivity
                    startActivity(Intent(this, DashBoardActivity::class.java))
                } else {
                    // Start the LoginActivity and finish the current SplashActivity
                    startActivity(Intent(this, WelcomeActivity::class.java))
                }
            } else {
                startActivity(Intent(this, OnboardingActivity::class.java))
            }
            finish()
        }, 5000)
    }
}
