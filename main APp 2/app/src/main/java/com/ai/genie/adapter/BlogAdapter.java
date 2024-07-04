package com.ai.genie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.models.BlogModel;
import com.ai.genie.ui.BlogDetailsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private ArrayList<BlogModel> itemList;
    private Activity context;
    private final ClickListner listener;

    public BlogAdapter(Activity context, ArrayList<BlogModel> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BlogModel model = itemList.get(position);
        Glide.with(context).load(model.image).placeholder(R.drawable.logo).into(holder.ivImage);
        holder.tvTitle.setText(model.title);
        holder.tvDescription.setText(model.description);
        holder.tvLike.setText(model.like + "");
        holder.tvUnLike.setText(model.unlike + "");
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BlogDetailsActivity.class);
                intent.putExtra(ConstantValue.BlogModelSerializable, model);
                context.startActivity(intent);
               /* Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("image_url", model.image);
                context.startActivity(intent);*/
            }
        });
        holder.llLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int value = model.like;
                value++;
                model.like = value;
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(model.id);
                HashMap<String, Object> map = new HashMap<>();
                map.put("like", model.like);
                databaseReference.updateChildren(map);
                notifyItemChanged(position);
            }
        });

        holder.llUnlike.setOnClickListener(view -> {
            int value = model.unlike;
            value++;
            model.unlike = value;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("blogs").child(model.id);
            HashMap<String, Object> map = new HashMap<>();
            map.put("unlike", model.unlike);
            databaseReference.updateChildren(map);
            notifyItemChanged(position);

        });

        holder.llShare.setOnClickListener(view -> shareIMage(holder.ivImage.getDrawable()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvDescription;
        private TextView tvLike;
        private TextView tvUnLike;
        private LinearLayout llLike;
        private LinearLayout llUnlike;
        private LinearLayout llShare;
        private LinearLayout llMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvUnLike = itemView.findViewById(R.id.tvUnLike);
            llLike = itemView.findViewById(R.id.llLike);
            llUnlike = itemView.findViewById(R.id.llUnlike);
            llShare = itemView.findViewById(R.id.llShare);
            llMain = itemView.findViewById(R.id.llMain);
        }
    }

    private void shareIMage(Drawable resource) {
        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

        try {
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            File file = new File(context.getApplicationContext().getExternalCacheDir(), File.separator + ts + ".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Uri apkURI = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Log.e("abc", "==========apkURI========" + apkURI);

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, apkURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/jpg");
            Intent chooser = Intent.createChooser(intent, "Share File");

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, apkURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            Log.e("abc", "=========onResourceReady====1==========");

            context.startActivity(chooser);

        } catch (Exception e) {
            Log.e("abc", "=========onResourceReady=====Exception=========" + e);

            e.printStackTrace();
            Log.e("abc", "=======Exception=============" + e);
        }

    }
}
