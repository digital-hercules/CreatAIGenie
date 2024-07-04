package com.ai.genie.ui.authentication.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ai.genie.databinding.ActivityLoginBinding
import com.ai.genie.ui.home.DashBoardActivity
import com.ai.genie.ui.authentication.view.OnboardingActivity.UserTypeActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()


        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logging in...")
        progressDialog.setCancelable(false)

        binding.loginButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, UserTypeActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        progressDialog.show() // Show progress dialog

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressDialog.dismiss() // Dismiss progress dialog

                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                    // Redirect to the main activity or another desired screen
//                    startActivity(Intent(this, HomeActivity::class.java))
                    val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    // Login failed
                    Toast.makeText(
                        this,
                        "Login failed. Please check your credentials.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
