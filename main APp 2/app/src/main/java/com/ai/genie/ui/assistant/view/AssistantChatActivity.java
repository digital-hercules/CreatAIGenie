package com.ai.genie.ui.assistant.view;

import static java.sql.DriverManager.println;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.ui.assistant.adapter.SuggestionAdapter;
import com.ai.genie.ui.home.adapter.MessageAdapter;
import com.ai.genie.api.API;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.SaveData;
import com.ai.genie.common.UsefullData;
import com.ai.genie.cropper.CropImage;
import com.ai.genie.databinding.ActivityAssistantChatBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.assistant.model.AssistantModel;
import com.ai.genie.ui.sidenav.model.Brand;
import com.ai.genie.models.Languages;
import com.ai.genie.ui.authentication.model.User;
import com.ai.genie.util.AdsManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AssistantChatActivity extends AppCompatActivity implements RecognitionListener {

    private TextView tvVoiceMsg;
    private SpeechRecognizer speechRecognizer;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog1;
    private static final int PICK_PDF_FILE = 1;

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

    File photoFile;
    String imageFilePath;
    private int volume = 1;
    //    ArrayList<MessagesModel> messageList = new ArrayList<>();
    ArrayList<LocalScrollViewModel> messageList = new ArrayList<>();

    MessageAdapter messageAdapter;
    private String question;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    //    OkHttpClient client = new OkHttpClient();
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)  // Adjust the connect timeout
            .readTimeout(5, TimeUnit.MINUTES)     // Adjust the read timeout
            .writeTimeout(5, TimeUnit.MINUTES)    // Adjust the write timeout
            .build();
    Call call;


    public static boolean startTextType = false;
    public TextToSpeech textToSpeech;

    private SaveData saveData;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private String answer = "";
    private LocalScrollViewModel localScrollViewModel;
    private User user;
    private Brand brand;

    private AssistantModel model;
    private ActivityAssistantChatBinding binding;

    @Override
    public void onBackPressed() {
        if (binding.llScroll.getVisibility() == View.VISIBLE) {
            binding.llScroll.setVisibility(View.GONE);
        } else {
            AdsManager.showInterstitial(this);
            super.onBackPressed();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityAssistantChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_chat_new);
        FirebaseApp.initializeApp(this);

        saveData = new SaveData(this);
        model = (AssistantModel) getIntent().getSerializableExtra("itemModel");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS_33)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS_33, PERMISSION_ALL);

            }
        } else {
            if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS, PERMISSION_ALL);

            }
        }


       /* localDatabasQueryClass= new LocalDatabasQueryClass(this);
        messageList.addAll(localDatabasQueryClass.getCatQuotes(cat_id));*/
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.voice_dilaog);

        ImageView ivVoice = bottomSheetDialog.findViewById(R.id.ivVoice);
        Glide.with(this).load(R.drawable.voice_animation).into(ivVoice);

        tvVoiceMsg = bottomSheetDialog.findViewById(R.id.tvVoiceMsg);


        binding.tvTitle.setText(getResources().getString(R.string.hi1));
        binding.tvName.setText("Hi ");


        String userId = mAuth.getCurrentUser().getUid();

        databaseUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                // Use user object as needed
                if (user != null) {
                    binding.tvName.setText("Hi " + user.username + "!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
        databaseUser.child(userId).child("brand").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brand = snapshot.getValue(Brand.class);
                // Use user object as needed

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

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
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.ivScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.llScroll.getVisibility() == View.VISIBLE) {
                    binding.llScroll.setVisibility(View.GONE);
                    binding.ivScroll.setImageResource(R.drawable.drop_up);
                } else {
                    binding.llScroll.setVisibility(View.VISIBLE);
                    binding.ivScroll.setImageResource(R.drawable.drop_down);

                }
            }
        });


        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        binding.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraAlert();
            }
        });
        binding.etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.etText.getText().toString().length() > 1) {
                    binding.ivSend.setVisibility(View.VISIBLE);
                    binding.ivCamera.setVisibility(View.GONE);
                    binding.ivSpeak.setVisibility(View.GONE);

                } else {
                    binding.ivSend.setVisibility(View.GONE);
                    binding.ivCamera.setVisibility(View.VISIBLE);
                    binding.ivSpeak.setVisibility(View.VISIBLE);
                    binding.ivSpeak.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                bottomSheetDialog.show();
                tvVoiceMsg.setText("in listening");

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "in listening");
                try {
                    speechRecognizer.startListening(intent);
                } catch (ActivityNotFoundException e) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(AssistantChatActivity.this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
//                        println("Speech recognition not supported on this device")
                }

            }
        });

        if (volume == 1) {
            binding.ivVolume.setImageResource(R.drawable.volume);
        } else {
            binding.ivVolume.setImageResource(R.drawable.volume_mute);

        }

        binding.ivVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume == 1) {
                    volume = 0;
                    binding.ivVolume.setImageResource(R.drawable.volume_mute);
                    textToSpeech.stop();
                } else {
                    volume = 1;
                    textToSpeech.isSpeaking();

                    binding.ivVolume.setImageResource(R.drawable.volume);

                }
            }
        });


        // Create Layout behaves and set it in recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);

        //====================================

        //====================================
        messageAdapter = new MessageAdapter(this, messageList, new ClickListner() {

            @Override
            public void onItemClickCopy(int pos, String answer) {
                textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null);

              /*  ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text to copy", answer);
                Toast.makeText(NewChatActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                myClipboard.setPrimaryClip(myClip);*/
            }

            @Override
            public void onItemClick(int pos, String question1) {
                if (saveData.getAdMessage(model.cat_id) > 0) {

                    question = question1;
//                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, "Typing...","0", UsefullData.getDateTime());

                    LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                    localScrollViewModel.id = -1;
                    localScrollViewModel.category_id = model.cat_id;
                    localScrollViewModel.question = question;
                    localScrollViewModel.answer = "Typing...";
                    localScrollViewModel.isImage = "0";
                    localScrollViewModel.date_time = UsefullData.getDateTime();
               /* MessagesModel messagesModel = new MessagesModel();
                messagesModel.question =question;
                messagesModel.answer ="Typing...";
                messagesModel.date_time ="";*/
                    addToChat(localScrollViewModel);
                    binding.etText.setText("");
                    int value = saveData.getAdMessage(model.cat_id) - 1;
                    saveData.save_int(model.cat_id, value);
                    binding.tvMessageCount.setText("You have " + saveData.getAdMessage(model.cat_id) + " message left.");
                    if (binding.switch1.isChecked()) {
                        callAPI(question, 0);
                    } else {

                        callAPI1(question, 0);

                    }
                } else {
                    ProgressManager.showUpgradeDialog(AssistantChatActivity.this, model.cat_id);

                }


            }
        });
        binding.recyclerView.setAdapter(messageAdapter);

        Log.e("abc", "=============cat_id==============" + model.cat_id);
        databaseRef.child(userId).child(model.cat_id).addValueEventListener(new ValueEventListener() {
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
                messageAdapter.notifyDataSetChanged();
//                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
//                nvScroll.fullScroll(View.FOCUS_DOWN);
               /* binding.nvScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.nvScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "=======onCancelled=============");

                // Handle errors
            }
        });
        //====================================

        binding.ivSend.setOnClickListener(view1 -> {
            if (saveData.getAdMessage(model.cat_id) > 0) {

                if (binding.etText.getText().toString().trim().length() > 0) {
                    question = binding.etText.getText().toString().trim();
//                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, "Typing...","0",UsefullData.getDateTime());


                    LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                    localScrollViewModel.id = -1;
                    localScrollViewModel.category_id = model.cat_id;
                    localScrollViewModel.question = question;
                    localScrollViewModel.answer = "Typing...";
                    localScrollViewModel.isImage = "0";
                    localScrollViewModel.date_time = UsefullData.getDateTime();


                /*MessagesModel messagesModel = new MessagesModel();
                messagesModel.question = question;
                messagesModel.answer = "Typing...";
                messagesModel.date_time = "";
*/
                    addToChat(localScrollViewModel);
                    binding.etText.setText("");

                    int value = 0;
                    if (binding.llScroll.getVisibility() == View.VISIBLE) {
                        binding.llScroll.setVisibility(View.GONE);
                        value = 1;
                    } else {
                        value = 0;
                    }
                    int value1 = saveData.getAdMessage(model.cat_id) - 1;
                    saveData.save_int(model.cat_id, value1);
                    binding.tvMessageCount.setText("You have " + saveData.getAdMessage(model.cat_id) + " message left.");

                    if (binding.switch1.isChecked()) {
                        callAPI(question, value);
                    } else {

                        callAPI1(question, value);

                    }

                }
            } else {
                ProgressManager.showUpgradeDialog(AssistantChatActivity.this, model.cat_id);

            }
        });

        binding.tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call.cancel();
                binding.ivLoading.setVisibility(View.GONE);
                startTextType = false;
                messageAdapter.notifyDataSetChanged();
               /* binding.nvScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.nvScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });*/
                binding.tvStop.setVisibility(View.GONE);
            }
        });

        ArrayList arrayList = new ArrayList();
        Collections.addAll(arrayList, Languages.getSpeakLang());
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.spinner_layout, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        binding.spinner.setAdapter(arrayAdapter);
        binding.spinner.setSelection(14);


        ArrayList arrayList1 = new ArrayList();
        Collections.addAll(arrayList1, Languages.getTone());
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getApplicationContext(), R.layout.spinner_layout, arrayList1);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        binding.spinnerTone.setAdapter(arrayAdapter1);
        binding.spinnerTone.setSelection(0);

        if (model.suggestion.size() > 0) {
            binding.tvSuggestion.setVisibility(View.GONE);
        }

        binding.tvSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuggestion();
            }
        });
        if (saveData.getInt(ConstantValue.SWITCH_WEB) == 0) {
            binding.switch1.setChecked(false);
        } else {
            binding.switch1.setChecked(true);
        }

        binding.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    saveData.save_int(ConstantValue.SWITCH_WEB, 1);
                } else {
                    saveData.save_int(ConstantValue.SWITCH_WEB, 0);

                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.tvMessageCount.setText("You have " + saveData.getAdMessage(model.cat_id) + " message left.");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permissions_required)).setMessage(getResources().getString(R.string.denied_permission)).setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setCancelable(false).create().show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    //    public static Bitmap getScreenShot(View view) {
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static void store(Bitmap bm, String fileName) {
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
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
    public void onReadyForSpeech(Bundle params) {
        if (tvVoiceMsg != null) {
            tvVoiceMsg.setText("Speak now");
        }

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        println("Speech recognition error: $error");
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (!matches.isEmpty()) {
            Log.e("abc", "========matches.get(0)=========" + matches.get(0));
//            binding.question.text = matches[0]
//            getResponse(matches[0])
            binding.etText.setText(matches.get(0).toString());
            bottomSheetDialog.dismiss();


        } else {
            bottomSheetDialog.dismiss();

        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        bottomSheetDialog.show();

        ArrayList partialResultList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (!partialResultList.isEmpty()) {
            String partialResult = partialResultList.get(0).toString();
          /*  binding.question.setText(partialResult)
            dialogVoice.updateText(partialResult.toString())*/
            tvVoiceMsg.setText(partialResult);

        } else {
            bottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    private void showSelectImage() {

        bottomSheetDialog1 = new BottomSheetDialog(this);
        bottomSheetDialog1.setContentView(R.layout.dialog_select_image);

        LinearLayout llCamera = bottomSheetDialog1.findViewById(R.id.llCamera);
        LinearLayout llGallery = bottomSheetDialog1.findViewById(R.id.llGallery);

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS, PERMISSION_ALL);

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
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(AssistantChatActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(AssistantChatActivity.this, PERMISSIONS, PERMISSION_ALL);

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
        Log.e("abc", "===========openCameraIntent============");
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
        } else if (i == PICK_PDF_FILE && i2 == RESULT_OK) {
            if (intent != null) {
                // Get the URI of the selected PDF file
                Uri selectedFileUri = intent.getData();
//                String filePath = getFilePathFromUri(selectedFileUri);
                Log.e("abc", "=========filePath=============" + selectedFileUri.getPath());

                extractPdfFile(selectedFileUri);

//                String extractedText = extractTextFromPDF(selectedFileUri.getPath());

                // Perform actions with the selected file URI
                // For instance, you can upload it to the chat system
                // or display its name in the chat window.
            }
        }
    }

    @Override
    protected void onDestroy() {
        speechRecognizer.destroy();
        textToSpeech.stop();
        startTextType = false;
        messageAdapter.notifyDataSetChanged();

        super.onDestroy();


    }

    private void runTextRec(Bitmap bitmap) {
        FirebaseVision.getInstance().getOnDeviceTextRecognizer().processImage(FirebaseVisionImage.fromBitmap(bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                if (firebaseVisionText.getText().length() <= 0) {
                    Toast.makeText(AssistantChatActivity.this.getApplicationContext(), "No text Found", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.etText.setText(firebaseVisionText.getText());
                bottomSheetDialog1.dismiss();
              /*  Intent intent = new Intent(HomeActivity.this, text_activity.class);
                intent.putExtra("text", firebaseVisionText.getText());
                HomeActivity.this.startActivity(intent);*/
            }
        }).addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception exc) {
                Toast.makeText(AssistantChatActivity.this.getApplicationContext(), "Unable to fetch text from image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addToChat(LocalScrollViewModel message) {
        Log.e("abc", "======addToChat====LocalScrollViewModel=========" + message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(message);
                binding.recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                messageAdapter.notifyDataSetChanged();
               /* binding.nvScroll.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.nvScroll.fullScroll(View.FOCUS_DOWN);
                    }
                });
*/

            }
        });
       /* messageList.add(message);
//        binding.recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);

        if (binding.recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE
                && (binding.recyclerView.isComputingLayout() == false)) {
            messageAdapter.notifyDataSetChanged();
        }

//        messageAdapter.notifyDataSetChanged();

//        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//                nvScroll.smoothScrollBy(0,0);
        binding.nvScroll.post(new Runnable() {
            @Override
            public void run() {
                binding.nvScroll.fullScroll(View.FOCUS_DOWN);
            }
        }); */      /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startTextType = true;
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//                nvScroll.smoothScrollBy(0,0);
                nvScroll.fullScroll(View.FOCUS_DOWN);

            }
        });*/
    } // addToChat End Here =====================

    void addResponse(String response, int value) {
        Log.e("abc", "============addResponse=========");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.tvStop.setVisibility(View.GONE);

                messageList.remove(messageList.size() - 1);
                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                localScrollViewModel.id = -1;
                localScrollViewModel.category_id = model.cat_id;
                localScrollViewModel.question = question;
                localScrollViewModel.answer = response;
                localScrollViewModel.isImage = "0";
                localScrollViewModel.date_time = UsefullData.getDateTime();

                addToChat(localScrollViewModel);
                if (value == 2) {
                    createLocalData(localScrollViewModel);
                }
            }
        });

/*
        MessagesModel messagesModel = new MessagesModel();
        messagesModel.question=question;
        messagesModel.answer=response;
        messagesModel.date_time="";*/
//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, response,"0",UsefullData.getDateTime());


    } // addResponse End Here =======

    void callAPI(String question, int value) {
        // okhttp
//        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));
        binding.tvStop.setVisibility(View.VISIBLE);

        JSONObject jsonBody = new JSONObject();
        try {

            jsonBody.put("model", "pplx-70b-online");
            jsonBody.put("max_tokens", API.token);
            JSONArray messages = new JSONArray();
            JSONObject message1 = new JSONObject();
            message1.put("role", "system");
            message1.put("content", "You are an AI model that created by CreateAIGenie. if someone asked this, answer it." + model.system);
            messages.put(message1);
            Log.e("abc", "===========question +\"in \"=============" + question + "in " + binding.spinnerTone.getSelectedItem().toString() + " and in " + binding.spinnerTone.getSelectedItem().toString());
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", question);
            messages.put(message);
            jsonBody.put("messages", messages);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder().url(API.API_URL_PERPLEXITY).header("Content-Type", "application/json").header("Authorization", "Bearer " + API.API_KEY_PERPLEXITY).post(requestBody).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("abc", "========IOException======" + e);

                addResponse("Stop generating", 1);
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
                        Log.e("abc", "=========result======" + result);

//                        String result = jsonArray.getJSONObject(0).getString("text");
                        callAPI2(result.trim(), value);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Log.e("abc", "=========response.body()======" + response.body());
                    Log.e("abc", "=========response.body()======" + response.message());

                    addResponse("Failed to load response due to" + response.body().toString(), 0);
                }

            }
        });

    }

    void callAPI2(String question, int value) {
        // okhttp
//        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));
        binding.tvStop.setVisibility(View.VISIBLE);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "ft:gpt-3.5-turbo-1106:tpskdigiart::8n2YtLjb");
            jsonBody.put("max_tokens", API.token);
            jsonBody.put("stream", API.stream);
            JSONArray messages = new JSONArray();
            JSONObject message1 = new JSONObject();
            message1.put("role", "system");
            message1.put("content", "You are an AI model that created by CreateAIGenie. if someone asked this, answer it." + model.system);

            Log.e("abc", "==============" + saveData.get("cat_id"));
            messages.put(message1);

            Log.e("abc", "===========question +\"in \"=============" + question + "in " + binding.spinnerTone.getSelectedItem().toString() + " and in " + binding.spinnerTone.getSelectedItem().toString());
            JSONObject message = new JSONObject();
            message.put("role", "user");
            if (value == 1) {

                if (binding.etKeyword.getText().toString().trim().length() > 0) {
                    message.put("content", question + " in " + binding.spinner.getSelectedItem().toString() + ", in " + binding.spinnerTone.getSelectedItem().toString() + " and added keyword " + binding.etKeyword.getText().toString());

                } else {
                    message.put("content", question + " in " + binding.spinner.getSelectedItem().toString() + " and in " + binding.spinnerTone.getSelectedItem().toString());
                }
            } else {
                message.put("content", question);
            }
            messages.put(message);
            jsonBody.put("messages", messages);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder().url(API.API_URL).header("Content-Type", "application/json").header("Authorization", "Bearer " + API.API).post(requestBody).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("abc", "========IOException======" + e);

                addResponse("Stop generating", 1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("abc", "=========response.body()======" + response.body());
                    answer = "";

                    try {
                        // Process streaming response
                        handleStreamingResponse(response.body().byteStream());

                    } catch (Exception e) {
                        binding.tvStop.setVisibility(View.GONE);

                        if (localScrollViewModel != null) {
                            createLocalData(localScrollViewModel);
                        }
                    }
                   /* JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject message = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = message.getString("content");
//                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim(),0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }*/
                } else {
                    Log.e("abc", "=========response.body()======" + response.body());
                    Log.e("abc", "=========response.body()======" + response.message());

                    addResponse("Failed to load response due to" + response.body().toString(), 0);
                }

            }
        });

    } // callAPI End Here =============

    void callAPI1(String question, int value) {
        // okhttp
//        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));
        binding.tvStop.setVisibility(View.VISIBLE);

        JSONObject jsonBody = new JSONObject();
        try {
            if (binding.switch1.isChecked()) {
                jsonBody.put("model", "ft:gpt-3.5-turbo-1106:tpskdigiart::8n2YtLjb");
            } else {
                jsonBody.put("model", model.model);
            }
            jsonBody.put("max_tokens", API.token);
            jsonBody.put("stream", API.stream);
            JSONArray messages = new JSONArray();
            JSONObject message1 = new JSONObject();
            message1.put("role", "system");
            message1.put("content", "You are an AI model that created by CreateAIGenie. if someone asked this, answer it." + model.system);

            Log.e("abc", "==============" + saveData.get("cat_id"));
          /*  if(!saveData.get(cat_id).equals(cat_id)){
                message1.put("content",  "You are a helpful assistant. Here is analyze data. "+saveData.get(cat_id));

            }else {
                if (!saveData.get(cat_id+"_instruction").equals(cat_id+"_instruction")){
                    message1.put("content", "your assistance as a " + type + " assistant. Here is the some instruction please follow this "+saveData.get(cat_id+"_instruction"));

                }else {
                    message1.put("content", "your assistance as a " + type + " assistant.");
                }

            }*/
            messages.put(message1);


            if (messageList.size() < 30) {

                for (int i = 0; i < messageList.size(); i++) {
                    if (!messageList.get(i).answer.equals("Typing...")) {
                        JSONObject message11 = new JSONObject();
                        message11.put("role", "user");
                        message11.put("content", messageList.get(i).question);
                        messages.put(message11);
                        JSONObject message = new JSONObject();
                        message.put("role", "user");
                        message.put("content", messageList.get(i).answer);
                        messages.put(message);
                    }
                }
            } else {
                int size = messageList.size();
                int startIndex = size > 30 ? size - 30 : 0;

                for (int i = startIndex; i < messageList.size(); i++) {
                    if (!messageList.get(i).answer.equals("Typing...")) {

                        JSONObject message11 = new JSONObject();
                        message11.put("role", "user");
                        message11.put("content", messageList.get(i).question);
                        messages.put(message11);
                        JSONObject message = new JSONObject();
                        message.put("role", "user");
                        message.put("content", messageList.get(i).answer);
                        messages.put(message);
                    }
                }
            }
            if (brand != null) {
                Log.e("abc", "===========brand.enable============" + brand.enable);

                if (brand.enable) {
                    Log.e("abc", "===========brand.enable=====1=======" + brand.enable);

                    JSONObject message = new JSONObject();
                    message.put("role", "user");
                    message.put("content", "Brand details: Brand name - " + brand.brand_name + "," + " Objective - " + brand.objective + "," + " Brand Theme - " + brand.brand_theme + "," + "Other details - " + brand.other_details);
                    messages.put(message);

                }
            }

            Log.e("abc", "===========question +\"in \"=============" + question + "in " + binding.spinnerTone.getSelectedItem().toString() + " and in " + binding.spinnerTone.getSelectedItem().toString());
            JSONObject message = new JSONObject();
            message.put("role", "user");
            if (value == 1) {

                if (binding.etKeyword.getText().toString().trim().length() > 0) {
                    message.put("content", question + " in " + binding.spinner.getSelectedItem().toString() + ", in " + binding.spinnerTone.getSelectedItem().toString() + " and added keyword " + binding.etKeyword.getText().toString());

                } else {
                    message.put("content", question + " in " + binding.spinner.getSelectedItem().toString() + " and in " + binding.spinnerTone.getSelectedItem().toString());
                }
            } else {
                message.put("content", question);
            }
            messages.put(message);
           /* JSONObject message2 = new JSONObject();
            message2.put("role", "assistant");
            message2.put("content", getIntent().getStringExtra("assistant"));
            messages.put(message2);*/
            jsonBody.put("messages", messages);
           /* jsonBody.put("model","text-davinci-003");
            jsonBody.put("model","text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens",4000);
            jsonBody.put("temperature",0);*/
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder().url(API.API_URL).header("Content-Type", "application/json").header("Authorization", "Bearer " + API.API).post(requestBody).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("abc", "========IOException======" + e);

                addResponse("Stop generating", 1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("abc", "=========response.body()======" + response.body());
                    answer = "";

                    try {
                        // Process streaming response
                        handleStreamingResponse(response.body().byteStream());

                    } catch (Exception e) {
                        binding.tvStop.setVisibility(View.GONE);

                        if (localScrollViewModel != null) {
                            createLocalData(localScrollViewModel);
                        }
                    }
                   /* JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        JSONObject message = jsonArray.getJSONObject(0).getJSONObject("message");
                        String result = message.getString("content");
//                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim(),0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }*/
                } else {
                    Log.e("abc", "=========response.body()======" + response.body());
                    Log.e("abc", "=========response.body()======" + response.message());

                    addResponse("Failed to load response due to" + response.body().toString(), 0);
                }

            }
        });

    } // callAPI End Here =============

    private void handleStreamingResponse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            // Process each line of the streaming response
            Log.e("abc", "=============line=============" + line);
            line = line.trim();
            if (line.startsWith("data:")) {
                line = line.substring("data:".length()).trim();
            }
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }

            if (line.equals("[DONE]")) {
                // Finish processing when "[DONE]" is received
                // You may want to handle any cleanup or UI updates here
                Log.e("abc", "============\"[DONE]\"======" + line);
                binding.tvStop.setVisibility(View.GONE);

//                createLocalData(localScrollViewModel);
                break;
            }
            Log.e("abc", "=============line=======1======" + line);

            try {
                // Parse the JSON data
                JSONObject jsonData = new JSONObject(line);

                // Check if it contains choices array
                if (jsonData.has("choices")) {
                    JSONArray choicesArray = jsonData.getJSONArray("choices");

                    // Iterate through choices
                    for (int i = 0; i < choicesArray.length(); i++) {
                        JSONObject choice = choicesArray.getJSONObject(i);

                        // Check if delta contains content
                        if (choice.has("delta") && choice.getJSONObject("delta").has("content")) {
                            String content = choice.getJSONObject("delta").getString("content");

                            // Update your adapter with the content
                            updateAdapter(content);
                        }
                    }
                }

                // Check if finish_reason is "stop"
                if (jsonData.has("choices") && jsonData.getJSONArray("choices").getJSONObject(0).has("finish_reason")) {
                    String finishReason = jsonData.getJSONArray("choices").getJSONObject(0).getString("finish_reason");
                    if ("stop".equals(finishReason)) {
                        // Finish processing when "stop" is received
                        // You may want to handle any cleanup or UI updates here
//                        createLocalData(localScrollViewModel);
                        runOnUiThread(() -> {

                            binding.tvStop.setVisibility(View.GONE);
                            if (localScrollViewModel != null) {
                                createLocalData(localScrollViewModel);
                            }
                        });
                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if (localScrollViewModel != null) {
                                    createLocalData(localScrollViewModel);
                                }
                            }
                        },10000);*/
                        break;
                    }
                }

            } catch (JSONException e) {
                binding.tvStop.setVisibility(View.GONE);

                e.printStackTrace();
            }
//            addResponse(line, 0);
        }
    }

    private void updateAdapter(String content) {
        // Assuming your adapter has a method to add a new item to the data set
        // Make sure to run this on the UI thread if the adapter manipulates UI elements
        runOnUiThread(() -> {
            Log.e("abc", "===========content===========" + content);
//            messageList.remove(messageList.size()-1);
            answer = answer + content;
            Log.e("abc", "===========answer===========" + answer);

            localScrollViewModel = new LocalScrollViewModel();
            localScrollViewModel.id = -1;
            localScrollViewModel.category_id = model.cat_id;
            localScrollViewModel.question = question;
            localScrollViewModel.answer = answer;
            localScrollViewModel.isImage = "0";
            localScrollViewModel.date_time = UsefullData.getDateTime();

            messageList.set(messageList.size() - 1, localScrollViewModel);

            messageAdapter.notifyDataSetChanged();
//            binding.nvScroll.fullScroll(View.FOCUS_DOWN);

           /* binding.nvScroll.post(new Runnable() {
                @Override
                public void run() {
                    binding.nvScroll.fullScroll(View.FOCUS_DOWN);
                }
            });*/
//            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//            messageAdapter.addNewItem(content);
//            messageAdapter.notifyItemInserted(messageAdapter.getItemCount() - 1); // Notify adapter about the new item
        });
    }

    private void createLocalData(LocalScrollViewModel message) {
        Log.e("abc", "============createLocalData=========");

//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id, message.question, message.answer,message.isImage,message.date_time);

        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = model.cat_id;
        localScrollViewModel.question = message.question;
        localScrollViewModel.answer = message.answer;
        localScrollViewModel.isImage = message.isImage;
        localScrollViewModel.date_time = message.date_time;

        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).child(model.cat_id).push().setValue(localScrollViewModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    /*public void translate(){
        try {
            String encode = URLEncoder.encode(question, Key.STRING_CHARSET_NAME);
            ReadJSONFeedTask readJSONFeedTask = new ReadJSONFeedTask();

            readJSONFeedTask.execute(new String[]{"https://translate.googleapis.com/translate_a/single?client=gtx&sl=" + Languages.getsptrCode(spinner.getSelectedItemPosition()) + "&tl=" + Languages.getsptrCode(spinner1.getSelectedItemPosition()) + "&dt=t&ie=UTF-8&oe=UTF-8&q=" + encode});



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {
        private ReadJSONFeedTask() {
        }

        public void onPreExecute() {
            super.onPreExecute();
        }

        public String doInBackground(String... strArr) {
            return readJSONFeed(strArr[0]);
        }

        public void onPostExecute(String str) {
//            text_activity.this.mProgressDialog.dismiss();
            if (str.equals("[\"ERROR\"]")) {
                Toast.makeText(AssistantChatActivity.this, "Sorry Something went Wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONArray jSONArray = new JSONArray(str);
                String str2 = "";
                for (int i = 0; i < jSONArray.getJSONArray(0).length(); i++) {
                    str2 = str2 + jSONArray.getJSONArray(0).getJSONArray(i).getString(0);
                }
                if (messageList != null) {

                    Log.e("abc","=======str2=========="+str2);
                    addResponse(str2,2);

                }
            } catch (Exception e) {

            }
        }
    }
*/
    public String readJSONFeed(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse execute = new DefaultHttpClient().execute(new HttpGet(str));
            if (execute.getStatusLine().getStatusCode() == 200) {
                InputStream content = execute.getEntity().getContent();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content));
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    sb.append(readLine);
                }
                content.close();
            } else {

            }
        } catch (Exception e) {
            sb.append("[\"ERROR\"]");
        }
        return sb.toString();
    }


    // Trigger this function to pick a PDF file
    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(intent, PICK_PDF_FILE);
    }

    private String extractTextFromPDF(String pdfPath) {
        File file = new File(pdfPath);
        Log.e("abc", "=========file=============" + file);

        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            String text = pdfTextStripper.getText(document);
            document.close();
            Log.e("abc", "=========text=============" + text);

            return text;
        } catch (IOException e) {
            Log.e("abc", "=========IOException=============" + e);

            e.printStackTrace();
            return "Failed to extract text from the PDF.";
        }
    }

    public void extractPdfFile(final Uri uri) {
        ProgressManager.showDailog(this);

        new Thread(() -> {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                PdfReader reader = new PdfReader(inputStream);

                int numberOfPages = reader.getNumberOfPages();
                StringBuilder builder = new StringBuilder();

                for (int i = 1; i <= numberOfPages; i++) {
                    try {
                        String fileContent = PdfTextExtractor.getTextFromPage(reader, i);
                        builder.append(fileContent);
                    } catch (IOException e) {
                        Log.e("abc", "Error extracting text from page " + i + ": " + e.getMessage());
                    }
                }

                reader.close(); // Close the PdfReader after extracting all content

                runOnUiThread(() -> {
                    Log.e("abc", "Extracted content: " + builder.toString());
                    ProgressManager.dismissDialog();
                    if (builder.length() > 0) {
                        saveData.save(model.cat_id, "the PDF Document file text " + builder.toString());
                        question = "pdf";
//                        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1, cat_id, question, "I have read the document. Please let me know how I can assist you.", "0", UsefullData.getDateTime());

                        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                        localScrollViewModel.id = -1;
                        localScrollViewModel.category_id = model.cat_id;
                        localScrollViewModel.question = question;
                        localScrollViewModel.answer = "I have read the document. Please let me know how I can assist you.";
                        localScrollViewModel.isImage = "0";
                        localScrollViewModel.date_time = UsefullData.getDateTime();

                        addToChat(localScrollViewModel);
                    } else {
                        Toast.makeText(this, "Text not found", Toast.LENGTH_SHORT).show();
                    }
//                    callAPI2(builder.toString());
                    // Perform UI updates or use the extracted content here
                });
            } catch (IOException e) {
                ProgressManager.dismissDialog();

                Toast.makeText(this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("abc", "Error accessing PDF file: " + e.getMessage());
            }
        }).start();
    }

    private String getFilePathFromUri(Uri uri) {
        Log.e("abc", "========uri.getScheme()==========" + uri.getScheme());
        String filePath = null;
        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            Log.e("abc", "=================cursor=============" + cursor);

            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filePath = cursor.getString(columnIndex);
                Log.e("abc", "=================filePath=============" + filePath);
                cursor.close();
            }

        } else if (uri.getScheme().equals("file")) {
            filePath = uri.getPath();
        }
        return filePath;
    }

    private void showSuggestion() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_suggestion);
        ImageView ivBack = bottomSheetDialog.findViewById(R.id.ivBack);
        RecyclerView rvSuggestion = bottomSheetDialog.findViewById(R.id.rvSuggestion);
        rvSuggestion.setLayoutManager(new LinearLayoutManager(this));
        rvSuggestion.setHasFixedSize(true);

        SuggestionAdapter adapter = new SuggestionAdapter(this, model.suggestion, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {

            }

            @Override
            public void onItemClick(int pos, String question) {

                binding.etText.setText(question);
                bottomSheetDialog.dismiss();

            }
        });
        rvSuggestion.setAdapter(adapter);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }


}