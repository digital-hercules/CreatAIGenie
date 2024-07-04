package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.ui.home.adapter.MessageTextToSpeechAdapter;
import com.ai.genie.ui.home.adapter.VoicePlaylistAdapter;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.UsefullData;
import com.ai.genie.cropper.CropImage;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalDatabasQueryClass;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.home.model.NotificationModel;
import com.ai.genie.ui.home.model.VoicePlaylistModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class TextToSpeechActivity extends AppCompatActivity {

    private ImageView ivBack;
    private RecyclerView recyclerView;
    private EditText etText;
    private ImageView ivCamera;
    private ImageView ivSend;
    MessageTextToSpeechAdapter messageAdapter;
    private BottomSheetDialog bottomSheetDialog1;
    private TextView tvSelectVoice;
    private ImageView ivDisplay;
    public static int selectVoice = 0;
    private ArrayList<VoicePlaylistModel> voicePlaylist = new ArrayList<>();
    File photoFile;
    String imageFilePath;
    //    private ProgressBar progressBar;
    private MediaPlayer mMediaPlayer;

    int PERMISSION_ALL = 101;
    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA

    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    ArrayList<LocalScrollViewModel> messageList = new ArrayList<>();
    private LocalDatabasQueryClass localDatabasQueryClass;

    private String cat_id = "speech_to_voice";
    private String question = "";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)  // Adjust the connect timeout
            .readTimeout(60, TimeUnit.SECONDS)     // Adjust the read timeout
            .writeTimeout(60, TimeUnit.SECONDS)    // Adjust the write timeout
            .build();

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_text_to_speech);


      /*  localDatabasQueryClass= new LocalDatabasQueryClass(this);
        messageList.addAll(localDatabasQueryClass.getCatQuotes(cat_id));*/
        ivBack = findViewById(R.id.ivBack);
        etText = findViewById(R.id.etText);
        ivSend = findViewById(R.id.ivSend);
        recyclerView = findViewById(R.id.recyclerView);
        ivCamera = findViewById(R.id.ivCamera);
        ivDisplay = findViewById(R.id.ivDisplay);
        tvSelectVoice = findViewById(R.id.tvSelectVoice);
//        progressBar = findViewById(R.id.progressBar);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etText.getText().toString().trim().length() > 0) {
                    if (voicePlaylist.size() > 0) {
                        question = etText.getText().toString().trim();
//                        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1, cat_id, question, "Loading...", "0", UsefullData.getDateTime());
                        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                        localScrollViewModel.id = -1;
                        localScrollViewModel.category_id = cat_id;
                        localScrollViewModel.question = question;
                        localScrollViewModel.answer = "Loading...";
                        localScrollViewModel.isImage = "0";
                        localScrollViewModel.date_time = UsefullData.getDateTime();
                        messageList.add(localScrollViewModel);
                        messageAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//                nvScroll.smoothScrollBy(0,0);
                        etText.setText("");
                        callAPI();
                    }
                }

            }
        });
        ivDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showVoicePlayList();
            }
        });

        getVoicePlaylist();
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                goRead();

            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //====================================

        //====================================
        messageAdapter = new MessageTextToSpeechAdapter(this, messageList, new ClickListner() {

            @Override
            public void onItemClickCopy(int pos, String answer) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    play(answer);
                } else {
                    play(answer);

                }

            }

            @Override
            public void onItemClick(int pos, String message) {
                long currTime = System.currentTimeMillis();
                download(currTime + "", message);
            }
        });
        recyclerView.setAdapter(messageAdapter);

        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).child(cat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messageList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve and process each history item
                    LocalScrollViewModel catModel = snapshot.getValue(LocalScrollViewModel.class);
                    messageList.add(catModel);
                    // Process the data as needed
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Handle errors
            }
        });
    }

    private void download(String FileName, String Url) {
      /*  CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.startLoadingdialog();*/
        Toast.makeText(TextToSpeechActivity.this, "Downloading...", Toast.LENGTH_LONG).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url + ""));
        Log.e("abc", "======request======" + request);
        Log.e("abc", "======downloadUrl======" + Url);
        Log.e("abc", "======fileName======" + FileName);
        request.setTitle("Download Speech");
//        request.setMimeType(downloadType);
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName + ".mp3");
        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);


        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // your code
//                customProgressDialog.dismissdialog();

                Log.e("abc", "===========isFinishing()============" + isFinishing());
                if (!isFinishing()) {
                    createNotification();
                    Toast.makeText(TextToSpeechActivity.this, "File Downloaded Successfully", Toast.LENGTH_LONG).show();

                } else {

                    //                            Toast.makeText(PlaneDetailsActivity.this,"File Downloaded Successfully", Toast.LENGTH_LONG).show();
                }
                Log.e("abc", "========onComplete=============");
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void createNotification() {
        Log.e("abc", "============createCat=========");

        NotificationModel model = new NotificationModel();
        model.id = -1;
        model.notification_title = "Downloaded Speech";
        model.notification_description = "Text to speech download Successfully";
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
       /* LocalDatabasQueryClass localDatabasQueryClass = new LocalDatabasQueryClass(this);

        long id = localDatabasQueryClass.insertNotification(model);

        Log.e("abc","============id========="+id);
        if (id > 0) {
            model.setId(id);
            Log.e("abc", "===id=====insertExpense=============" + id);
//                        expenseCreateListener.onExpenseCreated(expenseModel);


        }*/
    }

    private void play(String answer) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(answer);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.e("abc", "=======onCompletion==========");
                }
            });
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (Exception e) {
            Log.e("abc", "========Exception========" + e);
        }
    }


    private void getVoicePlaylist() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.play.ht/api/v2/cloned-voices/";

        Log.e("abc", " ========url============ " + url.replace(" ", "%20"));
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abc", "=======response=======" + response.trim());
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                        VoicePlaylistModel nm = new VoicePlaylistModel();
                        nm.id = jsonObject1.getString("id");
                        nm.name = jsonObject1.getString("name");
                        nm.type = jsonObject1.getString("type");
                        nm.gender = jsonObject1.getString("gender");
                        nm.voice_engine = jsonObject1.getString("voice_engine");
                        voicePlaylist.add(nm);

                    }

                    if (voicePlaylist.size() > 0) {
                        tvSelectVoice.setText(voicePlaylist.get(0).name);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // TODO Auto-generated method stub
                Log.e("ERROR", "error => " + error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("accept", "application/json");
                params.put("AUTHORIZATION", "da9d09bc54bb492db350c32081d50266");
                params.put("X-USER-ID", "wu9ULOPaTANRBj1XZvFtvNnzeG12");

       /*         params.put("AUTHORIZATION","638e58225a2e4d6a87471a71b31cb4ee");
                params.put("X-USER-ID","52e1jLaV5tWcI4LvjtNQ33RKupM2");*/
                return params;
            }
        };


        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(postRequest);
        queue.getCache().clear();
    }

    private void showVoicePlayList() {


        LayoutInflater factory = LayoutInflater.from(TextToSpeechActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_voice_playlist, null);
        final android.app.AlertDialog deleteDialog = new android.app.AlertDialog.Builder(TextToSpeechActivity.this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        RecyclerView rvVoicePlaylist = deleteDialogView.findViewById(R.id.rvVoicePlaylist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvVoicePlaylist.setLayoutManager(linearLayoutManager);
        VoicePlaylistAdapter voicePlaylistAdapter = new VoicePlaylistAdapter(this, voicePlaylist, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {
                TextToSpeechActivity.selectVoice = pos;
                deleteDialog.dismiss();
            }
        });
        rvVoicePlaylist.setAdapter(voicePlaylistAdapter);


        deleteDialog.show();
    }

    void callAPI() {
        ProgressManager.showDailog(this);
        // okhttp
//        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("text", question);
            jsonBody.put("voice", voicePlaylist.get(selectVoice).id);
            jsonBody.put("output_format", "mp3");
            jsonBody.put("voice_engine", "PlayHT2.0");
            jsonBody.put("quality", "high");
            jsonBody.put("speed", 1);
            jsonBody.put("emotion", "male_sad");
            jsonBody.put("voice_guidance", 3);
            jsonBody.put("style_guidance", 20);
            jsonBody.put("temperature", 1);
            jsonBody.put("seed", 0);
            jsonBody.put("sample_rate", 24000);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), null);
        okhttp3.Request request = new okhttp3.Request.Builder().url("https://api.play.ht/api/v2/tts").header("accept", "application/json").header("Accept", "*/*").header("content-type", "application/json")

                .header("AUTHORIZATION", "da9d09bc54bb492db350c32081d50266").header("X-USER-ID", "wu9ULOPaTANRBj1XZvFtvNnzeG12").header("Content-Length", "<calculated when request is sent>").header("Host", "<calculated when request is sent>").post(requestBody).build();
      /*  .header("AUTHORIZATION","638e58225a2e4d6a87471a71b31cb4ee")
                .header("X-USER-ID","52e1jLaV5tWcI4LvjtNQ33RKupM2")
*/
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("abc", "===========IOException===============" + e);
                Log.e("abc", "===========IOException====e.getMessage()===========" + e.getMessage());
//                addResponse("Failed to load response due to"+e.getMessage());
                ProgressManager.dismissDialog();

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull okhttp3.Response response) throws IOException {
                Log.e("abc", "==========response.body().string()==============" + response.body());

                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string(); // Read the response body
                        Log.e("abc", "==========responseBody==============" + responseBody);

                        // Split the response into individual events
                        String[] events = responseBody.split("\n\n");

                        for (String event : events) {
                            String[] lines = event.split("\n");
                            String eventType = null;
                            String eventData = null;

                            for (String line : lines) {
                                if (line.startsWith("event:")) {
                                    Log.e("abc", "============event==================");

                                    // Extract the event type (e.g., "generating")
                                    eventType = line.replace("event:", "").trim();
                                } else if (line.startsWith("data:")) {
                                    // Extract the data associated with the event
                                    Log.e("abc", "============data==================");

                                    eventData = line.replace("data:", "").trim();
                                }
                            }

                            if (eventType != null && eventData != null) {
                                // Process the event and associated data here
                                if ("generating".equals(eventType)) {
                                    Log.e("abc", "============generating==================");
                                    // Handle generating event
                                    // Process eventData as needed
                                } else if ("completed".equals(eventType)) {
                                    Log.e("abc", "============completed==================");
                                    try {
                                        JSONObject eventDataJson = new JSONObject(eventData);
                                        if (eventDataJson.has("url")) {
                                            String url = eventDataJson.getString("url");

                                            addResponse(url);
                                            // Now you have the URL
                                            // You can use the 'url' variable as needed in your code
                                            Log.e("abc", "URL from completed event: " + url);
                                        } else {
                                            Log.e("abc", "URL not found in completed event data");
                                        }
                                    } catch (JSONException e) {
                                        ProgressManager.dismissDialog();

                                        Log.e("abc", "Error parsing completed event data");
                                    }
                                    // Handle completed event
                                    // Process eventData as needed
                                }
                                // Add more event types as necessary
                            }
                        }
                    } catch (IOException e) {
                        ProgressManager.dismissDialog();

                        // Handle I/O exception
                    } catch (Exception e) {
                        ProgressManager.dismissDialog();

                        // Handle other exceptions
                    }
                } else {
                    // Handle the case where the API response is not successful
                    int statusCode = response.code();
                    String errorBody = response.body().string();
                    Log.e("abc", "Error Status Code: " + statusCode);
                    Log.e("abc", "Error Response Body: " + errorBody);
                    ProgressManager.dismissDialog();

                }

            }
        });

    }

    private void getSpeechVoice(String url) {

        RequestQueue queue = Volley.newRequestQueue(this);

        Log.e("abc", " ====getSpeechVoice====url============ " + url.replace(" ", "%20"));
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("abc", "===getSpeechVoice====response=======" + response.trim());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("output");
                    String url = jsonObject1.getString("url");
                    addResponse(url.trim());

                } catch (Exception e) {
                    addResponse("Failed to load!");

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // TODO Auto-generated method stub
                Log.e("ERROR", "error => " + error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }


            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("content-type", "application/json");
                params.put("AUTHORIZATION", "638e58225a2e4d6a87471a71b31cb4ee");
                params.put("X-USER-ID", "52e1jLaV5tWcI4LvjtNQ33RKupM2");
                return params;
            }
        };


        postRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        queue.add(postRequest);
        queue.getCache().clear();
    }

/*

    private class NetworkTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                        TextToSpeechActivity.this,
                        API.COGNITO_POOL_ID,
                        API.MY_REGION
                );

                // Synchronously ask the Polly Service to describe available TTS voices.
                AmazonPollyPresigningClient client = new AmazonPollyPresigningClient(credentialsProvider);
                DescribeVoicesRequest describeVoicesRequest = new DescribeVoicesRequest();
                DescribeVoicesResult describeVoicesResult = client.describeVoices(describeVoicesRequest);
                List<Voice> voices = describeVoicesResult.getVoices();
                Log.e("abc", "=============voices.size()===============" + voices.size());
            } catch (Exception e) {
                Log.e("abc", "An error occurred in doInBackground", e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Update UI or perform other actions after the network operations are complete
        }
    }
*/


    private void showSelectImage() {

        bottomSheetDialog1 = new BottomSheetDialog(this);
        bottomSheetDialog1.setContentView(R.layout.dialog_select_image);

        LinearLayout llCamera = bottomSheetDialog1.findViewById(R.id.llCamera);
        LinearLayout llGallery = bottomSheetDialog1.findViewById(R.id.llGallery);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                openCameraIntent();

            }
        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(TextToSpeechActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(TextToSpeechActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }

                goRead();

            }
        });
        bottomSheetDialog1.show();

    }

    public void goRead() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);

    }

    public void openCameraIntent() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        if (intent.resolveActivity(getPackageManager()) != null) {
        try {
            this.photoFile = createImageFile();
        } catch (IOException unused) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        File file = this.photoFile;
        if (file != null) {


            intent.putExtra("output", FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file));
            startActivityForResult(intent, 100);
        }
//        }

    }


    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File createTempFile = File.createTempFile("IMG_" + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        this.imageFilePath = createTempFile.getAbsolutePath();
        return createTempFile;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 203) {
            CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
            if (i2 == -1) {
                try {
                    runTextRec(MediaStore.Images.Media.getBitmap(getContentResolver(), activityResult.getUri()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (i2 == 204) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
        if (i == 100) {
            Intent intent2 = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
            File file = new File(this.imageFilePath);
            Uri fromFile = Uri.fromFile(file);
            intent2.setData(fromFile);
            sendBroadcast(intent2);
            if (file.length() > 0) {
                CropImage.activity(fromFile).start(this);
            }
        } else if (i == 101) {
            try {
                String[] strArr = {"_data"};
                Cursor query = getContentResolver().query(intent.getData(), strArr, (String) null, (String[]) null, (String) null);
                query.moveToFirst();
                String string = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
                File file2 = new File(string);
                Uri fromFile2 = Uri.fromFile(file2);
                if (file2.length() > 0) {
                    CropImage.activity(fromFile2).start(this);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void showCameraAlert() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_camera, null, false);

        ImageView ivScan = view.findViewById(R.id.ivScan);
        TextView tvContinue = view.findViewById(R.id.tvContinue);

        Glide.with(this).load(R.drawable.scan).into(ivScan);
        tvContinue.setOnClickListener(view1 -> {

            showSelectImage();
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void runTextRec(Bitmap bitmap) {
        FirebaseVision.getInstance().getOnDeviceTextRecognizer().processImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getText().length() <= 0) {
                    Toast.makeText(TextToSpeechActivity.this.getApplicationContext(), "No text Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                etText.setText(firebaseVisionText.getText());
                bottomSheetDialog1.dismiss();
              /*  Intent intent = new Intent(HomeActivity.this, text_activity.class);
                intent.putExtra("text", firebaseVisionText.getText());
                HomeActivity.this.startActivity(intent);*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception exc) {
                Toast.makeText(TextToSpeechActivity.this.getApplicationContext(), "Unable to fetch text from image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addResponse(String response) {
        Log.e("abc", "============addResponse=========" + response);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("abc", "============addResponse=1=======" + response);
                ProgressManager.dismissDialog();

                messageList.remove(messageList.size() - 1);
//                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, response,"2", UsefullData.getDateTime());
                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                localScrollViewModel.id = -1;
                localScrollViewModel.category_id = cat_id;
                localScrollViewModel.question = question;
                localScrollViewModel.answer = response;
                localScrollViewModel.isImage = "2";
                localScrollViewModel.date_time = UsefullData.getDateTime();
                messageList.add(localScrollViewModel);
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
                Log.e("abc", "============addResponse==2=======" + response);

                createLocalData(localScrollViewModel);
            }
        });


    } // addResponse End Here =======

    private void createLocalData(LocalScrollViewModel message) {
        Log.e("abc", "============createLocalData=========");

//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id, message.question, message.answer,message.isImage,message.date_time);

        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = cat_id;
        localScrollViewModel.question = question;
        localScrollViewModel.answer = message.answer;
        localScrollViewModel.isImage = message.isImage;
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
       /* LocalDatabasQueryClass localDatabasQueryClass = new LocalDatabasQueryClass(this);

        long id = localDatabasQueryClass.insertExpense(localScrollViewModel);

        Log.e("abc","============id========="+id);
        if (id > 0) {
            localScrollViewModel.setId(id);
            Log.e("abc", "===id=====insertExpense=============" + id);
//                        expenseCreateListener.onExpenseCreated(expenseModel);
            LocalScrollProductCreateListener expenseCreateListener = new LocalScrollProductCreateListener() {
                @Override
                public void onLocalScrollProductCreated(LocalScrollViewModel expenseModel) {

                    Log.e("abc", "=========expenseModel.quotes============" + expenseModel.category_id);
                }

            };

        }*/
    }

}

