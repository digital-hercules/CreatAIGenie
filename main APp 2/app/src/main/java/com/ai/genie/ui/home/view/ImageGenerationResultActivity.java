package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.genie.R;
import com.ai.genie.api.API;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.UsefullData;
import com.ai.genie.databinding.ActivityImageGenerationResultBinding;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.home.model.NotificationModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageGenerationResultActivity extends AppCompatActivity {
    ActivityImageGenerationResultBinding binding;
    private LocalScrollViewModel model;
    String size = "256x256";
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)  // Adjust the connect timeout
            .readTimeout(5, TimeUnit.MINUTES)     // Adjust the read timeout
            .writeTimeout(5, TimeUnit.MINUTES)    // Adjust the write timeout
            .build();

    private MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String cat_id = "image_generate";
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_generation_result);
        binding = ActivityImageGenerationResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        model = (LocalScrollViewModel) getIntent().getSerializableExtra("itemModel");
        Glide.with(this).load(model.answer).placeholder(R.drawable.logo).into(binding.ivImage);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageGenerationResultActivity.this, FullImageActivity.class);
                intent.putExtra("image_url", model.answer);
                startActivity(intent);
            }
        });
        binding.tvRegenerate.setOnClickListener(view13 -> callAPI());

        binding.llDownload.setOnClickListener(view1 -> {
            long currTime = System.currentTimeMillis();

            download(currTime + "", model.answer);
        });

        binding.tvHistory.setOnClickListener(view12 -> {
            Intent intent = new Intent(ImageGenerationResultActivity.this, ImageGenerateActivity.class);
            startActivity(intent);
        });

        binding.tvGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    void callAPI() {

        JSONObject jsonBody = new JSONObject();
        try {

            if (size.equalsIgnoreCase("1024x1024")) {
                jsonBody.put("model", "dall-e-3");

            }
            if (size.equalsIgnoreCase("1024x1792")) {
                jsonBody.put("model", "dall-e-3");

            }
            if (size.equalsIgnoreCase("1792x1024")) {
                jsonBody.put("model", "dall-e-3");

            }
            jsonBody.put("prompt", model.question);
            jsonBody.put("n", 1);
            jsonBody.put("size", size);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ProgressManager.showDailog(this);

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder()
                .url(API.API_IMAGE_URL)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API.API)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ImageGenerationResultActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("abc", "=========response.body()======" + e);

                ProgressManager.dismissDialog();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            Log.e("abc", "=========response.body()======" + response.body());


                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response.body().string());
                                Log.e("abc", "=========jsonObject======" + jsonObject);

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                String result = jsonArray.getJSONObject(0).getString("url");
//                        String result = jsonArray.getJSONObject(0).getString("text");

                                Glide.with(ImageGenerationResultActivity.this).load(result).placeholder(R.drawable.logo).into(binding.ivImage);

                                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                                localScrollViewModel.id = -1;
                                localScrollViewModel.category_id = cat_id;
                                localScrollViewModel.question = model.question;
                                localScrollViewModel.answer = result;
                                localScrollViewModel.isImage = "1";
                                localScrollViewModel.date_time = UsefullData.getDateTime();
                                createLocalData(localScrollViewModel);
                            } catch (JSONException e) {
                                Toast.makeText(ImageGenerationResultActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            ProgressManager.dismissDialog();


                        } else {
                            Toast.makeText(ImageGenerationResultActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();

                            ProgressManager.dismissDialog();

                        }

                    }
                });

            }
        });

    } // callAPI End Here =============


    private void createLocalData(LocalScrollViewModel message) {
        Log.e("abc", "============createLocalData=========");

//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id, message.question, message.answer,message.isImage,message.date_time);

        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = cat_id;
        localScrollViewModel.question = message.question;
        localScrollViewModel.answer = message.answer;
        localScrollViewModel.isImage = message.isImage;
        localScrollViewModel.date_time = message.date_time;

        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).child(cat_id).push().setValue(localScrollViewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    model = localScrollViewModel;

                    Log.e("abc", "============task.isSuccessful()==============");
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());
                }
            }
        });

    }

    private void download(String FileName, String Url) {
      /*  CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.startLoadingdialog();*/
        Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url + ""));

        request.setTitle("Image Download");
        request.setMimeType("image/jpg");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName + ".jpg");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);


        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // your code
//                customProgressDialog.dismissdialog();

                if (!isFinishing()) {
                    Toast.makeText(ImageGenerationResultActivity.this, "File Downloaded Successfully", Toast.LENGTH_LONG).show();
                    createNotification();
                } else {
                }
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void createNotification() {
        Log.e("abc", "============createCat=========");

        NotificationModel model = new NotificationModel();
        model.id = -1;
        model.notification_title = "Downloaded Image";
        model.notification_description = "Text to image download Successfully";
        model.date = UsefullData.getDateTime();
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef1.child(userId).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
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