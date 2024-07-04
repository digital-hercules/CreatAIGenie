package com.ai.genie.ui.authentication.view

import android.Manifest
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.ai.genie.R
import com.ai.genie.common.ConstantValue
import com.bumptech.glide.Glide
import com.ai.genie.databinding.ActivityRegisterBinding
import com.ai.genie.ui.authentication.model.User
import com.ai.genie.ui.home.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_IMAGE_PICK = 2
    var PERMISSION_ALL = 101
    var imageUri: Uri? = null

    var PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var mAuth = FirebaseAuth.getInstance()

    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    var PERMISSIONS_33 = arrayOf(
        READ_MEDIA_IMAGES,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context, permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    var user_type: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        user_type = intent.getIntExtra(ConstantValue.USER_TYPE, 1)


        if (user_type == 1) {
            binding.llCompany.visibility = View.GONE
            binding.llCreator.visibility = View.GONE
        } else if (user_type == 2) {
            binding.llCompany.visibility = View.GONE
            binding.llCreator.visibility = View.VISIBLE
        } else {
            binding.llCompany.visibility = View.VISIBLE
            binding.llCreator.visibility = View.GONE
        }

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Registering...")
        progressDialog.setCancelable(false)

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.tvLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)

        }

        binding.ivAdd.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!hasPermissions(this@RegisterActivity, *PERMISSIONS_33)) {
                    Log.e("abc", "=======hasPermissions====================")
                    ActivityCompat.requestPermissions(
                        this@RegisterActivity, HomeActivity.PERMISSIONS_33, PERMISSION_ALL
                    )
                    return@setOnClickListener
                }
            } else {
                if (!hasPermissions(this@RegisterActivity, *PERMISSIONS)) {
                    Log.e("abc", "=======hasPermissions====================")
                    ActivityCompat.requestPermissions(
                        this@RegisterActivity, PERMISSIONS, PERMISSION_ALL
                    )
                    return@setOnClickListener

                }
            }
            openGallery()

        }

        binding.registerButton.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val full_name = binding.etName.text.toString()
            val phone_number = binding.etPhone.text.toString()
            val bio = binding.etBio.text.toString()
            val company_name = binding.etCompanyName.text.toString()
            val role = binding.etYourRole.text.toString()

            if (user_type == 1) {
                if (email.isNotEmpty() && password.isNotEmpty() && full_name.isNotEmpty() && phone_number.isNotEmpty()) {
                    registerUser(email, password)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            } else if (user_type == 2) {
                if (email.isNotEmpty() && password.isNotEmpty() && full_name.isNotEmpty() && phone_number.isNotEmpty() && bio.isNotEmpty()) {
                    registerUser(email, password)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            } else {
                if (email.isNotEmpty() && password.isNotEmpty() && full_name.isNotEmpty() && phone_number.isNotEmpty() && company_name.isNotEmpty() && role.isNotEmpty()) {
                    registerUser(email, password)
                } else {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        progressDialog.show() // Show progress dialog
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Registration successful
                val user = auth.currentUser
                // Redirect to the main activity or another desired screen
//                    startActivity(Intent(this, HomeActivity::class.java))
                if (imageUri != null) {
                    uploadImageToFirebaseStorage(user!!.uid)
                } else {
                    saveUserDataToDatabase(
                        user!!.uid,
                        binding.etName.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString(),
                        ""
                    )
                }/*val intent = Intent(this@RegisterActivity, DashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()*/
            } else {
                progressDialog.dismiss() // Dismiss progress dialog

                Log.e(
                    "abc",
                    "===============task.exception?.message====================" + task.exception?.message
                )
                // Registration failed
                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Open gallery
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }


    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    imageUri = data?.data
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(binding.ivImage)
                    }
                    // Handle the selected image (e.g., upload to Firebase, display in ImageView)
                }
            }
        }
    }

    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            101 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.setTitle(resources.getString(R.string.permissions_required))
                        .setMessage(resources.getString(R.string.denied_permission))
                        .setPositiveButton(
                            resources.getString(R.string.settings)
                        ) { dialog, which ->
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }.setNegativeButton(
                            resources.getString(R.string.cancel)
                        ) { dialog, which -> }.setCancelable(false).create().show()
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }
        }
    }

    private fun uploadImageToFirebaseStorage(userId: String) {
        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("profile_images/$userId.jpg")

            imageRef.putFile(imageUri!!).addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    val userId: String = mAuth.getCurrentUser()!!.getUid()
                    saveUserDataToDatabase(
                        userId,
                        binding.etName.text.toString(),
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString(),
                        imageUrl
                    )
                    // Handle the image URL as needed (e.g., save it in the database)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                Log.e(
                    "abc",
                    "===============exception?.message======1==============" + exception?.message
                )

                progressDialog.dismiss() // Dismiss progress dialog

                // Handle unsuccessful upload
            }
        }
    }

    var databaseRef = FirebaseDatabase.getInstance().getReference("users")

    private fun saveUserDataToDatabase(
        userId: String, username: String, email: String, password: String, imageUrl: String
    ) {
        val user = User()
        user.username = username
        user.email = email
        user.password = password
        user.imageUrl = imageUrl
        user.phone_number = binding.etPhone.text.toString()
        user.user_type = user_type!!
        if (user_type == 1) {
            user.bio = "";
            user.company_name = "";
            user.role = "";
        } else if (user_type == 2) {
            user.bio = binding.etBio.text.toString();
            user.company_name = "";
            user.role = "";
        } else {
            user.bio = "";
            user.company_name = binding.etCompanyName.text.toString()
            user.role = binding.etYourRole.text.toString()
        }
        databaseRef.child(userId).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // The data has been successfully saved to the database
                // You can add any further actions you want to perform here
                progressDialog.dismiss() // Dismiss progress dialog

                val intent = Intent(this@RegisterActivity, DashBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                // Handle the error
                val exception = task.exception
                Log.e("abc", "===============exception==============" + exception?.message)

                Toast.makeText(this, exception!!.message, Toast.LENGTH_SHORT).show()

                progressDialog.dismiss() // Dismiss progress dialog
                // Log or display the error message
            }
        }

    }

}
