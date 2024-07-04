package com.ai.genie.util;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ai.genie.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class ImageResultActivity extends AppCompatActivity {

    String outputURL;
    ImageView ivResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_image_result);
        getSupportActionBar().hide();


        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        outputURL = getIntent().getStringExtra("url");

        ivResult = (ImageView) findViewById(R.id.ivResult);

        Glide.with(this).asBitmap().load(outputURL).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                // Image is loaded and ready to be displayed
                ivResult.setImageBitmap(resource);
                findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ContextCompat.checkSelfPermission(ImageResultActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted, request it
                            ActivityCompat.requestPermissions(ImageResultActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    010);
                        } else {
                            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";

                            final File dir = new File(dirPath);

                            final String fileName = outputURL.substring(outputURL.lastIndexOf('/') + 1);


                            Toast.makeText(ImageResultActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                            SaveImageTask saveImageTask = new SaveImageTask(resource);
                            saveImageTask.execute();
                        }

                    }
                });
                // Save the image to external storage

            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
                // Called when the image load is cleared
            }


            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);

            }
        });


    }

    private final class SaveImageTask extends AsyncTask<Void, Void, Void> {

        Bitmap bitmap;
        private ProgressDialog dialog;

        public SaveImageTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, "Image_" + System.currentTimeMillis());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = ImageResultActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (imageUri != null) {
                try {
                    OutputStream out = ImageResultActivity.this.getContentResolver().openOutputStream(imageUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                    if (out != null) {
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ImageResultActivity.this);
            dialog.setMessage("Saving Image....");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();

        }
    }
}