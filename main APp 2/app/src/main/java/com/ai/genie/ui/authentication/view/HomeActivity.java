package com.ai.genie.ui.authentication.view;

import static java.sql.DriverManager.println;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.genie.Privacy_three_policies;
import com.ai.genie.ui.home.view.ImageGenerateActivity;
import com.ai.genie.R;
import com.bumptech.glide.Glide;
import com.ai.genie.ui.home.adapter.MessageAdapter;
import com.ai.genie.api.API;
import com.ai.genie.common.UsefullData;
import com.ai.genie.cropper.CropImage;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.home.view.TextToMusicActivity;
import com.ai.genie.ui.home.view.TextToVideoActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RecognitionListener {
    private DrawerLayout drawer;
    private ImageView ivMenu;
    public static ImageView ivSend;
    public static ImageView ivCamera;
    public static ImageView ivSpeak;
    public static ImageView ivLoading;
    public static EditText etText;
    private TextView tvVoiceMsg;
    private SpeechRecognizer speechRecognizer;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDialog bottomSheetDialog1;
    int PERMISSION_ALL = 101;
    /* String[] PERMISSIONS = {
             android.Manifest.permission.RECORD_AUDIO
     };*/
    String[] PERMISSIONS = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA

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
    RecyclerView recyclerView;
    ArrayList<LocalScrollViewModel> messageList = new ArrayList<>();
    MessageAdapter messageAdapter;
    private String question;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    private String cat_id;
    private NestedScrollView nvScroll;

    public static boolean startTextType = false;
    public TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPermissions(HomeActivity.this, PERMISSIONS_33)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_33, PERMISSION_ALL);

            }
        } else {
            if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, PERMISSION_ALL);

            }
        }
        drawer = findViewById(R.id.drawer_layout);
        ivMenu = findViewById(R.id.ivMenu);
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
        init();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void init() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.voice_dilaog);

        ImageView ivVoice = bottomSheetDialog.findViewById(R.id.ivVoice);
        Glide.with(this).load(R.drawable.voice_animation).into(ivVoice);

        tvVoiceMsg = bottomSheetDialog.findViewById(R.id.tvVoiceMsg);

        ivSend = findViewById(R.id.ivSend);
        ivCamera = findViewById(R.id.ivCamera);
        ivSpeak = findViewById(R.id.ivSpeak);
        etText = findViewById(R.id.etText);
        nvScroll = findViewById(R.id.nvScroll);
        ivLoading = findViewById(R.id.ivLoading);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCameraAlert();
            }
        });
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etText.getText().toString().length() > 1) {
                    ivSend.setVisibility(View.VISIBLE);
                    ivCamera.setVisibility(View.GONE);
                    ivSpeak.setVisibility(View.GONE);
                } else {
                    ivSend.setVisibility(View.GONE);
                    ivCamera.setVisibility(View.VISIBLE);
                    ivSpeak.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_33, PERMISSION_ALL);
                        return;

                    }
                } else {
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }

                bottomSheetDialog.show();
                tvVoiceMsg.setText("in listening");

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
                intent.putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                );
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "in listening");
                try {
                    speechRecognizer.startListening(intent);
                } catch (ActivityNotFoundException e) {
                    bottomSheetDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Speech recognition not supported on this device", Toast.LENGTH_SHORT).show();
//                        println("Speech recognition not supported on this device")
                }

            }
        });


        recyclerView = findViewById(R.id.recyclerView);

        // Create Layout behaves and set it in recyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //====================================

        //====================================
        messageAdapter = new MessageAdapter(this, messageList, new ClickListner() {

            @Override
            public void onItemClickCopy(int pos, String answer) {
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text to copy", answer);
                Toast.makeText(HomeActivity.this, "Copied", Toast.LENGTH_SHORT).show();
                myClipboard.setPrimaryClip(myClip);
            }

            @Override
            public void onItemClick(int pos, String question1) {
                question = question1;

//                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, "Typing...","0", UsefullData.getDateTime());
                LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();

                localScrollViewModel.id = -1;
                localScrollViewModel.category_id = cat_id;
                localScrollViewModel.question = question;
                localScrollViewModel.answer = "Typing...";
                localScrollViewModel.isImage = "0";
                localScrollViewModel.date_time = UsefullData.getDateTime();
                /*MessagesModel messagesModel = new MessagesModel();
                messagesModel.question =question;
                messagesModel.answer ="Typing...";
                messagesModel.date_time ="";*/
                addToChat(localScrollViewModel);
                etText.setText("");
                callAPI(question);
            }
        });
        recyclerView.setAdapter(messageAdapter);
        //====================================

        ivSend.setOnClickListener(view -> {
            question = etText.getText().toString().trim();

//            LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, "Typing...","0",UsefullData.getDateTime());

            LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
            localScrollViewModel.id = -1;
            localScrollViewModel.category_id = cat_id;
            localScrollViewModel.question = question;
            localScrollViewModel.answer = "Typing...";
            localScrollViewModel.isImage = "0";
            localScrollViewModel.date_time = UsefullData.getDateTime();
            /*MessagesModel messagesModel = new MessagesModel();
            messagesModel.question =question;
            messagesModel.answer ="Typing...";
            messagesModel.date_time ="";*/

            addToChat(localScrollViewModel);
            etText.setText("");
            callAPI(question);
        });
    }

    public void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navGenerateImage) {
            startActivity(new Intent(this, ImageGenerateActivity.class));

        } else if (id == R.id.navTextToVideo) {
            startActivity(new Intent(this, TextToVideoActivity.class));

        } else if (id == R.id.navTextToMusic) {
            startActivity(new Intent(this, TextToMusicActivity.class));

        } else if (id == R.id.navChangeVoice) {

        } else if (id == R.id.navShareApp) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            share.putExtra(Intent.EXTRA_TEXT, "Please Download my generative ai app App from Google Play: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

            startActivity(Intent.createChooser(share, "Share link!"));

        } else if (id == R.id.navRateUs) {
            final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }/*
        } else if (id == R.id.navPrivacyPolicy) {
            startActivity(new Intent(this, Privacy_three_policies.class));
            finish();
            ?
*/
        } else if (id == R.id.navLogOut) {
            logout();
        }
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAlert();
        }
    }

    private void showAlert() {


        LayoutInflater factory = LayoutInflater.from(HomeActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_confirmation, null);
        final android.app.AlertDialog deleteDialog = new android.app.AlertDialog.Builder(HomeActivity.this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView tvMessage = deleteDialogView.findViewById(R.id.tvMessage);
        tvMessage.setText("Do you want to exit the app?");
        TextView tvYes = deleteDialogView.findViewById(R.id.tvYes);
        TextView tvNo = deleteDialogView.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view1 -> {

            finish();
            deleteDialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            deleteDialog.dismiss();
        });


        deleteDialog.show();
    }

    private void logout() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation, null, false);

        TextView tvYes = view.findViewById(R.id.tvYes);
        TextView tvNo = view.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view1 -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
        if (!dir.exists())
            dir.mkdirs();
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

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
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
            etText.setText(matches.get(0).toString());
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
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, PERMISSION_ALL);
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
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(HomeActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(HomeActivity.this, PERMISSIONS, PERMISSION_ALL);
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


            intent.putExtra("output", FileProvider.getUriForFile(this, getApplicationContext().getPackageName(), file));
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
                    Toast.makeText(HomeActivity.this.getApplicationContext(), "No text Found", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(HomeActivity.this.getApplicationContext(), "Unable to fetch text from image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void addToChat(LocalScrollViewModel message) {
        if (volume == 1) {
            textToSpeech.speak(message.answer, TextToSpeech.QUEUE_FLUSH, null);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startTextType = true;
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
//                nvScroll.smoothScrollBy(0,0);
                nvScroll.fullScroll(View.FOCUS_DOWN);

            }
        });
    } // addToChat End Here =====================

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
//        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel(-1,cat_id,question, response,"0",UsefullData.getDateTime());
        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
        localScrollViewModel.id = -1;
        localScrollViewModel.category_id = cat_id;
        localScrollViewModel.question = question;
        localScrollViewModel.answer = response;
        localScrollViewModel.isImage = "0";
        localScrollViewModel.date_time = UsefullData.getDateTime();
/*
        MessagesModel messagesModel = new MessagesModel();
        messagesModel.question=question;
        messagesModel.answer=response;
        messagesModel.date_time="";
*/

        addToChat(localScrollViewModel);
    } // addResponse End Here =======

    void callAPI(String question) {
        // okhttp
//        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(API.API_URL)
                .header("Authorization", "Bearer " + API.API)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    Log.e("abc", "=========response.body()==============" + response.body());
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        addResponse("There is something wrong please try again");

                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("Failed to load response due to" + response.body().toString());
                }

            }
        });

    } // callAPI End Here =============


}