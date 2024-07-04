package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.genie.R;
import com.ai.genie.api.API;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.UsefullData;
import com.ai.genie.databinding.ActivityImageGenerationBinding;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageGenerationActivity extends AppCompatActivity {

    ActivityImageGenerationBinding binding;
    int PERMISSION_ALL = 101;
    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA};

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


    String size = "256x256";
    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.MINUTES)  // Adjust the connect timeout
            .readTimeout(5, TimeUnit.MINUTES)     // Adjust the read timeout
            .writeTimeout(5, TimeUnit.MINUTES)    // Adjust the write timeout
            .build();

    private MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private String cat_id = "image_generate";
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_generation);
        binding = ActivityImageGenerationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etText.setText("");
            }
        });

        binding.llAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(ImageGenerationActivity.this, PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(ImageGenerationActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(ImageGenerationActivity.this, PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        ActivityCompat.requestPermissions(ImageGenerationActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                goRead();
            }
        });

        binding.llPasteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                try {
                    CharSequence textToPaste = clipboard.getPrimaryClip().getItemAt(0).getText();
                    binding.etText.setText(textToPaste);

                } catch (Exception e) {
                    return;
                }
            }
        });

        binding.ivStyle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivStyle1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.ivStyle2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.ivStyle3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        });

        binding.ivStyle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivStyle1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.ivStyle2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.ivStyle3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            }
        });

        binding.ivStyle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivStyle1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.ivStyle2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                binding.ivStyle3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
            }
        });

        binding.tv1.setOnClickListener(view12 -> {
            size = binding.tv1.getText().toString();
            binding.tv1.setBackground(getResources().getDrawable(R.drawable.bg_round_corners_gradiant));
            binding.tv1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
            binding.tv2.setBackground(null);
            binding.tv3.setBackground(null);
            binding.tv4.setBackground(null);
            binding.tv5.setBackground(null);
        });


        binding.tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = binding.tv2.getText().toString();
                binding.tv2.setBackground(getResources().getDrawable(R.drawable.bg_round_corners_gradiant));
                binding.tv2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.tv1.setBackground(null);
                binding.tv3.setBackground(null);
                binding.tv4.setBackground(null);
                binding.tv5.setBackground(null);

            }
        });


        binding.tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = binding.tv3.getText().toString();
                binding.tv3.setBackground(getResources().getDrawable(R.drawable.bg_round_corners_gradiant));
                binding.tv3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.tv2.setBackground(null);
                binding.tv1.setBackground(null);
                binding.tv4.setBackground(null);
                binding.tv5.setBackground(null);

            }
        });


        binding.tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = binding.tv4.getText().toString();
                binding.tv4.setBackground(getResources().getDrawable(R.drawable.bg_round_corners_gradiant));
                binding.tv4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.tv2.setBackground(null);
                binding.tv3.setBackground(null);
                binding.tv1.setBackground(null);
                binding.tv5.setBackground(null);

            }
        });


        binding.tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = binding.tv5.getText().toString();
                binding.tv5.setBackground(getResources().getDrawable(R.drawable.bg_round_corners_gradiant));
                binding.tv5.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.main_color)));
                binding.tv2.setBackground(null);
                binding.tv3.setBackground(null);
                binding.tv4.setBackground(null);
                binding.tv1.setBackground(null);

            }
        });

        binding.tvGenerate.setOnClickListener(view1 -> {
            if (!binding.etText.getText().toString().trim().isEmpty()) {
                callAPI();
            }
        });

        binding.tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageGenerationActivity.this, ImageGenerateActivity.class);
                startActivity(intent);
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
            jsonBody.put("prompt", binding.etText.getText().toString());
            jsonBody.put("n", 1);
            jsonBody.put("size", size);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ProgressManager.showDailog(this);

        Log.e("abc", "===========jsonBody==============" + jsonBody.toString());
        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);

        Request request = new Request.Builder().url(API.API_IMAGE_URL).header("Content-Type", "application/json").header("Authorization", "Bearer " + API.API).post(requestBody).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(ImageGenerationActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                Log.e("abc", "=========response.body()======" + e);
                ProgressManager.dismissDialog();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.e("abc", "=========response.body()======" + response.body());


                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String result = jsonArray.getJSONObject(0).getString("url");
//                        String result = jsonArray.getJSONObject(0).getString("text");
                        LocalScrollViewModel localScrollViewModel = new LocalScrollViewModel();
                        localScrollViewModel.id = -1;
                        localScrollViewModel.category_id = cat_id;
                        localScrollViewModel.question = binding.etText.getText().toString();
                        localScrollViewModel.answer = result;
                        localScrollViewModel.isImage = "1";
                        localScrollViewModel.date_time = UsefullData.getDateTime();
                        createLocalData(localScrollViewModel);
                    } catch (JSONException e) {
                        Toast.makeText(ImageGenerationActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    ProgressManager.dismissDialog();
                } else {
                    Toast.makeText(ImageGenerationActivity.this, "There is something wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    ProgressManager.dismissDialog();
                }
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
                    Intent intent = new Intent(ImageGenerationActivity.this, ImageGenerationResultActivity.class);
                    intent.putExtra("itemModel", localScrollViewModel);
                    startActivity(intent);
                    Log.e("abc", "============task.isSuccessful()==============");
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());
                }
            }
        });

    }

    public void goRead() {
        startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 101);

    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 101) {
            try {
                String[] strArr = {"_data"};
                Cursor query = getContentResolver().query(intent.getData(), strArr, (String) null, (String[]) null, (String) null);
                query.moveToFirst();
                String string = query.getString(query.getColumnIndex(strArr[0]));
                query.close();
                File file2 = new File(string);
                Uri fromFile2 = Uri.fromFile(file2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}