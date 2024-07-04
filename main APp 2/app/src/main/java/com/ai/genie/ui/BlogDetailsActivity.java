package com.ai.genie.ui;

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

import com.ai.genie.ui.home.view.FullImageActivity;
import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.databinding.ActivityBlogDetailsBinding;
import com.ai.genie.models.BlogModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

public class BlogDetailsActivity extends AppCompatActivity {
    private ActivityBlogDetailsBinding binding;
    BlogModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBlogDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_blog_details);
        model = (BlogModel)getIntent().getSerializableExtra(ConstantValue.BlogModelSerializable);

        Glide.with(this).load(model.image).placeholder(R.drawable.logo).into(binding.ivImage);
        binding.tvTitle.setText(model.title);
        binding.tvDescription.setText(model.description);
        binding.tvLike.setText(model.like+"");
        binding.tvUnLike.setText(model.unlike+"");
        binding.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BlogDetailsActivity.this, FullImageActivity.class);
                intent.putExtra("image_url", model.image);
                startActivity(intent);
            }
        });


        binding.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = model.like;
                value++;
                model.like = value;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(model.id);
                HashMap<String, Object> map = new HashMap<>();
                map.put("like", model.like);
                databaseReference.updateChildren(map);
                binding.tvLike.setText(model.like+"");

            }
        });

        binding.llUnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = model.unlike;
                value++;
                model.unlike = value;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(model.id);
                HashMap<String, Object> map = new HashMap<>();
                map.put("unlike", model.unlike);
                databaseReference.updateChildren(map);
                binding.tvUnLike.setText(model.unlike+"");

            }
        });

        binding.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareIMage(binding.ivImage.getDrawable());

            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void shareIMage(Drawable resource){
        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();

        try {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File file = new File(getApplicationContext().getExternalCacheDir(), File.separator + ts +".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Uri apkURI = FileProvider.getUriForFile(this,getApplicationContext()
                            .getPackageName() + ".provider", file);
            Log.e("abc","==========apkURI========"+apkURI);

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
            Log.e("abc","=========onResourceReady====1==========");

            startActivity(chooser);

        } catch (Exception e) {
            Log.e("abc","=========onResourceReady=====Exception========="+e);

            e.printStackTrace();
            Log.e("abc","=======Exception============="+e);
        }

    }
}