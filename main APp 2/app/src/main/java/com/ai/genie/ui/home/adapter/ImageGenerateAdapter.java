package com.ai.genie.ui.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.home.view.FullImageActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageGenerateAdapter extends RecyclerView.Adapter<ImageGenerateAdapter.ViewHolder>{
    ArrayList<LocalScrollViewModel> messageList;
    Context context;
    private final ClickListner listener;

    public ImageGenerateAdapter(Context context, ArrayList<LocalScrollViewModel> messageList, ClickListner listener){
        this.context = context;
        this.messageList = messageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_generate, null);
        return new ViewHolder(chatView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LocalScrollViewModel message = messageList.get(position);
        holder.tvQuestion.setText(message.question);
        if (message.isImage.equals("0")){
            holder.imageCard.setVisibility(View.GONE);
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.tvAnswer.setText(message.answer);

        }else {
            holder.imageCard.setVisibility(View.VISIBLE);
            holder.tvAnswer.setVisibility(View.GONE);

            Glide.with(context).load(message.answer).placeholder(R.drawable.logo).into(holder.image);
        }


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullImageActivity.class);
                intent.putExtra("image_url", message.answer);
                context.startActivity(intent);
            }
        });
         holder.imageDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(position, message.answer);

            }
        });
         holder.imageShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
                share.putExtra(Intent.EXTRA_TEXT, message.answer);

                context.startActivity(Intent.createChooser(share, "Share Image!"));*/
                shareIMage( message.answer,holder.image.getDrawable());

            }
        });



    }
    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
            Log.e("abc","=========getLocalBitmapUri============");

        } catch (IOException e) {
            Log.e("abc","=========IOException============"+e);

            e.printStackTrace();
        }
        return bmpUri;
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private ImageView imageDownload;
        private ImageView imageShare;
        private TextView tvQuestion;
        private TextView tvAnswer;
        private RelativeLayout imageCard;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageDownload = itemView.findViewById(R.id.imageDownload);
            imageShare = itemView.findViewById(R.id.imageShare);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            imageCard = itemView.findViewById(R.id.imageCard);
        }
    }

    interface MessageClickListener1 {
        void onImageDownloadClick(int position, String message);
    }

    private void shareIMage(String url,Drawable resource){
       Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();

        try {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File file = new File(context.getApplicationContext().getExternalCacheDir(), File.separator + ts +".jpg");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);

            Uri apkURI = FileProvider.getUriForFile(context,
                    context.getApplicationContext()
                            .getPackageName() + ".provider", file);
            Log.e("abc","==========apkURI========"+apkURI);

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
            Log.e("abc","=========onResourceReady====1==========");

            context.startActivity(chooser);
                                   /* final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),  getApplicationContext().getPackageName()+".provider", file);
                                    Log.e("abc","=========photoURI==========="+photoURI);


                                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.setType("image/jpg");

                                    startActivity(Intent.createChooser(intent, "Share image via"));*/
        } catch (Exception e) {
            Log.e("abc","=========onResourceReady=====Exception========="+e);

            e.printStackTrace();
            Log.e("abc","=======Exception============="+e);
        }
      /*  Uri apkURI = FileProvider.getUriForFile(context,
                context.getApplicationContext()
                        .getPackageName() + ".provider", new File(url));
        Log.e("abc","==========apkURI========"+apkURI);

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

        context.startActivity(chooser);*/
    }

}
