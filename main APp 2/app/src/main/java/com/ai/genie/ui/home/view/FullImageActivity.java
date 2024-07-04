package com.ai.genie.ui.home.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ai.genie.R;
import com.ai.genie.common.ZoomImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class FullImageActivity extends AppCompatActivity {

    private ImageView ivBack;
    private ImageView ivShare;
    private ZoomImageView ivImage;
    private String image_url;
    private int REQUEST_CODE_SAVE_DOCUMENT = 200;

    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        image_url = getIntent().getStringExtra("image_url");
        Log.e("abc", "==============image_url==========" + image_url);
        ivBack = findViewById(R.id.ivBack);
        ivImage = findViewById(R.id.ivImage);
        ivShare = findViewById(R.id.ivShare);

        Glide.with(this).load(image_url).placeholder(R.drawable.logo).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                Log.e("abc", "=============onLoadFailed=============");
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                Log.e("abc", "=============onResourceReady=============");

                bitmap = ((BitmapDrawable) resource).getBitmap();
                return false;
            }
        }).into(ivImage);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + ts + ".jpg");
                    FileOutputStream fOut = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);

                    Uri apkURI = FileProvider.getUriForFile(FullImageActivity.this,
                            getApplicationContext()
                                    .getPackageName() + ".provider", file);
                    Log.e("abc", "==========apkURI========" + apkURI);

                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_STREAM, apkURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setType("image/jpg");
                    Intent chooser = Intent.createChooser(intent, "Share File");

                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, apkURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    Log.e("abc", "=========onResourceReady====1==========");

                    startActivity(chooser);
                                   /* final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),  getApplicationContext().getPackageName()+".provider", file);
                                    Log.e("abc","=========photoURI==========="+photoURI);


                                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("image/jpg");

                                    startActivity(Intent.createChooser(intent, "Share image via"));*/
                } catch (Exception e) {
                    Log.e("abc", "=========onResourceReady=====Exception=========" + e);

                    e.printStackTrace();
                    Log.e("abc", "=======Exception=============" + e);
                }
              /*  Glide.with(getApplicationContext())
                        .load(image_url) //link of your image file(url)
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new Target<Drawable>() {
                            @Override
                            public void onLoadStarted(@Nullable Drawable placeholder) {

                                Log.e("abc","=========onLoadStarted==============");
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                Log.e("abc","=========onLoadFailed==============");

                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                Log.e("abc","=========onResourceReady==============");


                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                Log.e("abc","=========onLoadCleared==============");


                            }

                            @Override
                            public void getSize(@NonNull SizeReadyCallback cb) {
                                Log.e("abc","=========getSize==============");

                            }

                            @Override
                            public void removeCallback(@NonNull SizeReadyCallback cb) {
                                Log.e("abc","=========removeCallback==============");

                            }

                            @Override
                            public void setRequest(@Nullable Request request) {
                                Log.e("abc","=========setRequest==============");

                            }

                            @Nullable
                            @Override
                            public Request getRequest() {
                                Log.e("abc","=========getRequest==============");

                                return null;
                            }

                            @Override
                            public void onStart() {
                                Log.e("abc","=========onStart==============");

                            }

                            @Override
                            public void onStop() {
                                Log.e("abc","=========onStop==============");

                            }

                            @Override
                            public void onDestroy() {
                                Log.e("abc","=========onDestroy==============");

                            }
                        });
*/

              /*  Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.
                share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_TEXT, image_url);

                startActivity(Intent.createChooser(share, "Share link!"));*/
            }
        });


    }


}