package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.genie.R;
import com.ai.genie.ui.home.adapter.ChatWithImageAdapter;
import com.ai.genie.api.API;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.SaveData;
import com.ai.genie.common.UsefullData;
import com.ai.genie.databinding.ActivityChatWithImageBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.features.featuresfoto.puzzle.photopicker.utils.FileUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatWithImageActivity extends AppCompatActivity {

    ActivityChatWithImageBinding binding;
    ChatWithImageAdapter adapter;
    ArrayList<LocalScrollViewModel> messageList = new ArrayList<>();
    final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    //    OkHttpClient client = new OkHttpClient();
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)  // Adjust the connect timeout
            .readTimeout(5, TimeUnit.MINUTES)     // Adjust the read timeout
            .writeTimeout(5, TimeUnit.MINUTES)    // Adjust the write timeout
            .build();
    Call call;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String userId;
    String cat_id = "Vision";
    private String question;
    private Uri imageUri = null;
    private String image_url;
    public TextToSpeech textToSpeech;
    private SaveData saveData;

    int PERMISSION_ALL = 101;
    int REQUEST_IMAGE_PICK = 2;

    String[] PERMISSIONS = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public String[] PERMISSIONS_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA

    };

    boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvMessageCount.setText("You have " + saveData.getAdMessage(cat_id) + " message left.");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatWithImageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_chat_with_image);
        userId = mAuth.getCurrentUser().getUid();
        saveData = new SaveData(this);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        textToSpeech.setSpeechRate(saveData.getFloat(ConstantValue.Voice_rate));
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChat.setLayoutManager(linearLayoutManager);

        adapter = new ChatWithImageAdapter(this, messageList, new ClickListner() {

            @Override
            public void onItemClickCopy(int pos, String answer) {
                textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null);

            }

            @Override
            public void onItemClick(int pos, String question1) {
                question = question1;

                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                localScrollViewModel.id = -1;
                localScrollViewModel.category_id = cat_id;
                localScrollViewModel.question = question;
                localScrollViewModel.answer = "Typing...";
                localScrollViewModel.isImage = "0";
                localScrollViewModel.date_time = UsefullData.getDateTime();

                messageList.add(localScrollViewModel);
                binding.rvChat.scrollToPosition(adapter.getItemCount() - 1);
                adapter.notifyDataSetChanged();

                binding.etText.setText("");
                callAPI(question, image_url);

            }
        });
        binding.rvChat.setAdapter(adapter);

        databaseRef.child(userId).child(cat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve and process each history item
                    LocalScrollViewModel catModel = snapshot.getValue(LocalScrollViewModel.class);
                    Log.e("abc", "=======onDataChange=============");
                    messageList.add(catModel);
                    // Process the data as needed
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "=======onCancelled=============");

                // Handle errors
            }
        });
        binding.ivSend.setOnClickListener(view1 -> {
            if (saveData.getAdMessage(cat_id) > 0) {
                if (binding.etText.getText().toString().trim().length() > 0) {
                    question = binding.etText.getText().toString().trim();

                    LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                    localScrollViewModel.id = -1;
                    localScrollViewModel.category_id = cat_id;
                    localScrollViewModel.question = question;
                    localScrollViewModel.answer = "Typing...";
                    localScrollViewModel.isImage = "0";
                    localScrollViewModel.image = image_url;
                    localScrollViewModel.date_time = UsefullData.getDateTime();
                    messageList.add(localScrollViewModel);
                    binding.rvChat.scrollToPosition(adapter.getItemCount() - 1);
                    adapter.notifyDataSetChanged();

                    binding.etText.setText("");

                    if (image_url != null) {
                        int value = saveData.getAdMessage(cat_id) - 1;
                        saveData.save_int(cat_id, value);
                        callAPI(question, image_url);
                        binding.tvMessageCount.setText("You have " + saveData.getAdMessage(cat_id) + " message left.");

                    } else {
                        Toast.makeText(this, "Please Add Image", Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                ProgressManager.showUpgradeDialog(this, cat_id);

            }
        });

        binding.llPasteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
                    if (textToPaste.toString().contains("http")) {
                        image_url = textToPaste.toString();
                        Toast.makeText(ChatWithImageActivity.this, "Add Link Successfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(ChatWithImageActivity.this, "URL Invalid", Toast.LENGTH_SHORT).show();

                    }
//                    binding.etText.setText(textToPaste);

                } catch (Exception e) {
                    return;
                }
            }
        });

        binding.llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(ChatWithImageActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(ChatWithImageActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(ChatWithImageActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(ChatWithImageActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2:
                    imageUri = data != null ? data.getData() : null;
                    if (imageUri != null) {
                        File f = FileUtils.getFile(this, (imageUri));

                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
                            if (bitmap != null) {
                                // Continue with processing
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] imageBytes = baos.toByteArray();
                                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                                image_url = "data:image/png;base64," + imageString;
                                Log.e("abc", "==========imageString===========" + imageString);
                                // Rest of your code
                                Toast.makeText(this, "Add Image Successfully", Toast.LENGTH_SHORT).show();

                            } else {
                                Log.e("Error", "Failed to decode bitmap from file.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Error", "Exception occurred: " + e.getMessage());
                        }

//                        Glide.with(this).load(imageUri).into(binding.ivImage);
                    }
                    // Handle the selected image (e.g., upload to Firebase, display in ImageView)
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permissions_required))
                            .setMessage(getResources().getString(R.string.denied_permission))
                            .setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    void callAPI(String question, String image_url) {
        // okhttp
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-4-vision-preview");
            jsonBody.put("max_tokens", API.token);
            JSONArray messages = new JSONArray();
            JSONObject message1 = new JSONObject();
            message1.put("role", "user");
            JSONArray contentArray = new JSONArray(); // New JSONArray for content

            // First content: text message
            JSONObject textContent = new JSONObject();
            textContent.put("type", "text");
            textContent.put("text", question);
            contentArray.put(textContent);

            // Second content: image URL
            JSONObject imageUrlContent = new JSONObject();
            imageUrlContent.put("type", "image_url");
            JSONObject imageUrlObject = new JSONObject();
            imageUrlObject.put("url", image_url);
            imageUrlContent.put("image_url", imageUrlObject);
            contentArray.put(imageUrlContent);

            // Add the content array to the message1 object
            message1.put("content", contentArray);

            // Add message1 to the messages array
            messages.put(message1);

            // Add the messages array to the jsonBody
            jsonBody.put("messages", messages);

            Log.e("abc", "===========jsonBody==============" + jsonBody.toString());

        } catch (JSONException e) {
            Log.e("abc", "===========JSONException==============" + e);
            throw new RuntimeException(e);
        }


        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(API.API_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API.API)
                .post(requestBody)
                .build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("abc", "========IOException======" + e);

                addToChat("Stop generating", 0);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("abc", "=========response.body()======" + response.body());

                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject message = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = message.getString("content");
//                        String result = jsonArray.getJSONObject(0).getString("text");
                        addToChat(result.trim(), 1);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("abc", "=========response.body()======" + response.body());
                    Log.e("abc", "=========response.body()======" + response.message());

                    addToChat("Failed to load response due to" + response.body().toString(), 0);
                }

            }
        });

    } // callAPI End Here =============

    void addToChat(String response, int value) {
        Log.e("abc", "======addToChat====LocalScrollViewModel=========" + response);
        messageList.remove(messageList.size() - 1);
        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = cat_id;
        localScrollViewModel.question = question;
        localScrollViewModel.answer = response;
        localScrollViewModel.isImage = "0";
        localScrollViewModel.image = image_url;
        localScrollViewModel.date_time = UsefullData.getDateTime();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(localScrollViewModel);
                binding.rvChat.scrollToPosition(adapter.getItemCount() - 1);
                adapter.notifyDataSetChanged();
                if (value == 1) {
                    createLocalData(localScrollViewModel);
                }

            }
        });

    }

    private void createLocalData(LocalScrollViewModel message) {
        Log.e("abc", "============createLocalData=========");

//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id, message.question, message.answer,message.isImage,message.date_time);

        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = cat_id;
        localScrollViewModel.question = message.question;
        localScrollViewModel.answer = message.answer;
        localScrollViewModel.isImage = message.isImage;
        localScrollViewModel.image = message.image;
        localScrollViewModel.date_time = message.date_time;

        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).child(cat_id).push().setValue(localScrollViewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("abc", "============task.isSuccessful()==============");
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());
                }
            }
        });

    }
}