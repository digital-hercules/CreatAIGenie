package com.ai.genie.ui.home.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ai.genie.ProgressDialogUtils
import com.ai.genie.R
import com.ai.genie.ui.home.adapter.ImageGenerateAdapter
import com.ai.genie.common.UsefullData
import com.ai.genie.databinding.ActivityImageGenerateBinding
import com.ai.genie.listner.ClickListner
import com.ai.genie.localdatabase.LocalDatabasQueryClass
import com.ai.genie.localdatabase.LocalScrollViewModel
import com.ai.genie.ui.home.model.NotificationModel
import com.ai.genie.ui.home.model.GenerateImageModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.OutputStream

// Define ImageGenerateActivity class that extends AppCompatActivity and implements MessageClickListener
class ImageGenerateActivity : AppCompatActivity() {

    // Initialize variables and views
    private val REQUEST_CODE_SAVE_DOCUMENT: Int = 200
    private lateinit var binding: ActivityImageGenerateBinding
    private var messageList = ArrayList<LocalScrollViewModel>()
//    private lateinit var adapter: MessageAdapter1
    private lateinit var adapter: ImageGenerateAdapter
    private var mlayoutManager: LinearLayoutManager? = null
    private lateinit var progressDialogUtils: ProgressDialogUtils
    private var selectedResolution: String = "256x256"
    private var imageUrl: String = ""
    private var cat_id: String? = "image_generate"
    private var localDatabasQueryClass: LocalDatabasQueryClass? = null
    var databaseRef = FirebaseDatabase.getInstance().getReference("history")
    var databaseRef1 = FirebaseDatabase.getInstance().getReference("notification")
    var mAuth = FirebaseAuth.getInstance()
    // Override the onCreate method
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = ActivityImageGenerateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


       /* localDatabasQueryClass = LocalDatabasQueryClass(this)
        messageList.addAll(localDatabasQueryClass!!.getCatQuotes(cat_id))*/
        progressDialogUtils = ProgressDialogUtils(this)
//        adapter = MessageAdapter1(messageList, this)
        adapter = ImageGenerateAdapter(
            this,
            messageList,
            object : ClickListner {
                override fun onItemClickCopy(pos: Int, answer: String?) {
                }

                override fun onItemClick(pos: Int, message: String?) {
                    if (message != null) {
                        imageUrl = message
                    }
                    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    intent.type =
                        "image/jpeg" // Set the MIME type to specify the type of document you want to create

                    // Optional: Set the initial file name
                    intent.putExtra(Intent.EXTRA_TITLE, "my_image.jpg")

                    // Start the intent to create a document
                    startActivityForResult(intent, REQUEST_CODE_SAVE_DOCUMENT)
                }

            })

        binding.recyclerView.adapter = adapter

        val userId = mAuth.currentUser!!.uid
        databaseRef.child(userId).child(cat_id!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    messageList.clear()
                    for (snapshot in dataSnapshot.children) {
                        // Retrieve and process each history item
                        val catModel: LocalScrollViewModel? = snapshot.getValue(LocalScrollViewModel::class.java)
                        catModel?.let { messageList.add(it) }
                        // Process the data as needed
                    }
                    adapter.notifyDataSetChanged()
                    binding.recyclerView.scrollToPosition(messageList.size - 1)
                    //                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
//                nvScroll.fullScroll(View.FOCUS_DOWN);
                    binding.nvScroll.post(Runnable {  binding.nvScroll.fullScroll(View.FOCUS_DOWN) })
                }

                override fun onCancelled(databaseError: DatabaseError) {

                    // Handle errors
                }
            })

        mlayoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        setResolution()

        // Set the layout manager only once.
        binding.recyclerView.layoutManager = mlayoutManager

        // Set click listener for the send button to generate an image
        binding.sendbtn.setOnClickListener {
            generateImage()
        }

        // Set click listener for the back button (optional)
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    // Function to set the resolution based on radio button selection
    private fun setResolution() {
        // Set the default selection
        binding.resolution256.isChecked = true
        // Set a listener to handle radio button selection changes
        binding.resolutionRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.resolution256 -> selectedResolution = "256x256"
                R.id.resolution512 -> selectedResolution = "512x512"
                R.id.resolution1024 -> selectedResolution = "1024x1024"
            }
        }
    }

    // Function to generate an image
    private fun generateImage() {
        progressDialogUtils.showProgress("Loading...")
        val prompt = binding.userMsg.text.toString().trim()
//        val localScrollViewModel = LocalScrollViewModel(-1, cat_id, prompt, "Loading...", "0", UsefullData.getDateTime())
        val localScrollViewModel = LocalScrollViewModel()
        localScrollViewModel.id= -1
        localScrollViewModel.category_id= cat_id
        localScrollViewModel.question= prompt
        localScrollViewModel.answer=  "Loading..."
        localScrollViewModel.isImage=  "0"
        localScrollViewModel.date_time=  UsefullData.getDateTime()

        messageList.add(localScrollViewModel);
//        addToChat(localScrollViewModel);
//        list.add(MessageModel(true, false, prompt))
        adapter.notifyItemInserted(messageList.size - 1)
        binding.recyclerView.recycledViewPool.clear()
        binding.recyclerView.scrollToPosition(messageList.size - 1)
        binding.nvScroll.post(Runnable {  binding.nvScroll.fullScroll(View.FOCUS_DOWN) })

        adapter.notifyDataSetChanged();

        lifecycleScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val mediaType = "application/json".toMediaType()
            val body =
                "{\r\n    \"prompt\": \"${prompt}\",\r\n    \"n\": 1,\r\n    \"size\": \"${selectedResolution}\"\r\n  }".toRequestBody(
                    mediaType
                )
            val request = Request.Builder()
                .url("https://api.openai.com/v1/images/generations")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader(
                    "Authorization",
                    "Bearer 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"
                )
                .build()
            val response = client.newCall(request).execute()
            val jsonResponse = response.body?.string()
            val generateImageModel = Gson().fromJson(jsonResponse, GenerateImageModel::class.java)

            if (generateImageModel.data!=null) {


                val textResponse = generateImageModel.data[0].url

                addResponse(prompt, textResponse,0);
                /*  val localScrollViewModel = LocalScrollViewModel(-1, cat_id, prompt, textResponse, 1, "")
            messageList.add(localScrollViewModel);*/

//            list.add(MessageModel(false, true, textResponse))

                withContext(Dispatchers.Main) {
                    // Move this line into the `withContext()` block.
                    binding.userMsg.text?.clear() // Use a safe call operator to avoid the error.

                    adapter.notifyDataSetChanged()
//                adapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerView.recycledViewPool.clear()
                    binding.recyclerView.scrollToPosition(messageList.size - 1)
                    binding.nvScroll.post(Runnable {  binding.nvScroll.fullScroll(View.FOCUS_DOWN) })

                    progressDialogUtils.hideProgress()
                }
            }else{
                addResponse(prompt, "Image not available. Please try again.",1)
                /*  val localScrollViewModel = LocalScrollViewModel(-1, cat_id, prompt, textResponse, 1, "")
            messageList.add(localScrollViewModel);*/

//            list.add(MessageModel(false, true, textResponse))

                withContext(Dispatchers.Main) {
                    // Move this line into the `withContext()` block.
                    binding.userMsg.text?.clear() // Use a safe call operator to avoid the error.

                    adapter.notifyDataSetChanged()
//                adapter.notifyItemInserted(messageList.size - 1)
                    binding.recyclerView.recycledViewPool.clear()
                    binding.recyclerView.scrollToPosition(messageList.size - 1)
                    binding.nvScroll.post(Runnable {  binding.nvScroll.fullScroll(View.FOCUS_DOWN) })

                    progressDialogUtils.hideProgress()
                }
            }
        }
    }
    fun addResponse(question: String?,response: String?,value: Int?) {
        Log.e("abc","========response==========="+response)
        messageList.removeAt(messageList.size - 1)

//        val localScrollViewModel = LocalScrollViewModel(-1, cat_id, question, response, "1", UsefullData.getDateTime())
        val localScrollViewModel = LocalScrollViewModel()
        localScrollViewModel.id= -1
        localScrollViewModel.category_id= cat_id
        localScrollViewModel.question= question
        localScrollViewModel.answer=  response
        if (value==0) {
            localScrollViewModel.isImage = "1"
        }else{
            localScrollViewModel.isImage = "0"

        }
        localScrollViewModel.date_time=  UsefullData.getDateTime()

        addToChat(localScrollViewModel)
        if (value==0) {
            createLocalData(localScrollViewModel)
        }
    }
    private fun addToChat (message:LocalScrollViewModel){
                messageList.add(message);
//                nvScroll.smoothScrollBy(0,0);
//                nvScroll.fullScroll(View.FOCUS_DOWN);

    } // addToChat End Here

    private fun createLocalData(message: LocalScrollViewModel) {
       /* val localScrollViewModel = LocalScrollViewModel(
            -1,
            cat_id,
            message.question,
            message.answer,
            message.isImage,
            message.date_time
        )*/
        val localScrollViewModel = LocalScrollViewModel()
        localScrollViewModel.id= -1
        localScrollViewModel.category_id= cat_id
        localScrollViewModel.question= message.question
        localScrollViewModel.answer= message.answer
        localScrollViewModel.isImage=  message.isImage
        localScrollViewModel.date_time=  message.date_time

        val localDatabasQueryClass = LocalDatabasQueryClass(this)

        val userId: String = mAuth.getCurrentUser()!!.getUid()
        databaseRef.child(userId).child(cat_id!!).push().setValue(localScrollViewModel)
            .addOnCompleteListener(
                OnCompleteListener<Void?> { task ->
                    if (task.isSuccessful) {
                        Log.e("abc", "============task.isSuccessful()==============")
                    } else {
                        val exception = task.exception
                        Log.e("abc", "===============exception==============" + exception!!.message)
                    }
                })
       /* val id = localDatabasQueryClass.insertExpense(localScrollViewModel)
        Log.e("abc", "============id=========$id")
        if (id > 0) {
            localScrollViewModel.id = id
            Log.e("abc", "===id=====insertExpense=============$id")
            //                        expenseCreateListener.onExpenseCreated(expenseModel);
            val expenseCreateListener =
                LocalScrollProductCreateListener { expenseModel ->
                    Log.e(
                        "abc",
                        "=========expenseModel.quotes============" + expenseModel.category_id
                    )
                }
        }*/
    }

    // Implement the image download click listener
  /*  override fun onImageDownloadClick(position: Int, message: String) {
        imageUrl = message
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type =
            "image/jpeg" // Set the MIME type to specify the type of document you want to create

        // Optional: Set the initial file name
        intent.putExtra(Intent.EXTRA_TITLE, "my_image.jpg")

        // Start the intent to create a document
        startActivityForResult(intent, REQUEST_CODE_SAVE_DOCUMENT)
    }*/

    // Override the onActivityResult method to handle document creation result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SAVE_DOCUMENT && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                // Now, you can save the image to the selected location
                saveImageToUri(uri, imageUrl)
            }
        }
    }

    // Function to save the image to a specified URI
    private fun saveImageToUri(uri: Uri, imageUrl: String) {
        progressDialogUtils.showProgress("Saving image")
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val outputStream: OutputStream? = contentResolver.openOutputStream(uri)

                    try {
                        if (outputStream != null) {
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }
                        outputStream?.close()
                        createNotification();
                        progressDialogUtils.hideProgress()
                        // Image saved successfully
                    } catch (e: IOException) {
                        e.printStackTrace()
                        progressDialogUtils.hideProgress()
                        // Handle errors here
                    }
                }
            })
    }

    private fun createNotification() {
        Log.e("abc", "============createCat=========")
        val model = NotificationModel()
        model.id = -1
        model.notification_title = "Image Downloaded"
        model.notification_description = "Image download successfully"
        model.date = UsefullData.getDateTime()
        val userId = mAuth.currentUser!!.uid
        databaseRef1.child(userId).push().setValue(model)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    Log.e("abc", "============task.isSuccessful()==============")
                } else {
                    val exception = task.exception
                    Log.e("abc", "===============exception==============" + exception!!.message)
                }
            })

       /* var localDatabasQueryClass = LocalDatabasQueryClass(this)
        val id = localDatabasQueryClass.insertNotification(model)
        Log.e("abc", "============id=========$id")
        if (id > 0) {
            model.id = id
            Log.e("abc", "===id=====insertExpense=============$id")
            //                        expenseCreateListener.onExpenseCreated(expenseModel);
        }*/
    }
}
