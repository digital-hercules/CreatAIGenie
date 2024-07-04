package com.ai.genie.ui.history.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.ui.home.view.ImageGenerateActivity;
import com.ai.genie.ui.assistant.model.AssistantModel;
import com.ai.genie.ui.assistant.view.AssistantChatActivity;
import com.ai.genie.ui.home.view.NewChatActivity;
import com.ai.genie.ui.home.view.TextToSpeechActivity;
import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.common.UsefullData;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalScrollViewModel;
import com.ai.genie.ui.home.model.NotificationModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private ArrayList<LocalScrollViewModel> itemList;
    private Activity context;
    private final ClickListner listener;
    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public HistoryAdapter(Activity context, ArrayList<LocalScrollViewModel> itemList, ClickListner listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {

        LocalScrollViewModel localScrollViewModel = itemList.get(position);
        holder.tvTime.setText(localScrollViewModel.date_time);
        holder.tvQuestion.setText(localScrollViewModel.question);

        if (localScrollViewModel.isImage.equals("0")) {
            if (localScrollViewModel.question.equals("pdf")) {
                holder.card_view.setVisibility(View.VISIBLE);
                holder.tvQuestion.setVisibility(View.GONE);
            } else {
                holder.card_view.setVisibility(View.GONE);
                holder.tvQuestion.setVisibility(View.VISIBLE);
            }
            holder.ivImage.setVisibility(View.GONE);
            holder.llSound.setVisibility(View.GONE);
            holder.tvAnswer.setVisibility(View.VISIBLE);
            holder.cardCopy.setVisibility(View.VISIBLE);
            holder.cardShare.setVisibility(View.GONE);
            holder.cardDownload.setVisibility(View.GONE);
            holder.cardSpeech.setVisibility(View.GONE);
            holder.tvAnswer.setText(localScrollViewModel.answer);
        } else if (localScrollViewModel.isImage.equals("1")) {
            holder.tvAnswer.setVisibility(View.GONE);
            holder.llSound.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.VISIBLE);
            holder.cardCopy.setVisibility(View.GONE);
            holder.cardShare.setVisibility(View.VISIBLE);
            holder.cardDownload.setVisibility(View.VISIBLE);
            holder.cardSpeech.setVisibility(View.GONE);
            Glide.with(context).load(localScrollViewModel.answer).into(holder.ivImage);
        } else if (localScrollViewModel.isImage.equals("2")) {
            holder.tvAnswer.setVisibility(View.GONE);
            holder.ivImage.setVisibility(View.GONE);
            holder.llSound.setVisibility(View.VISIBLE);
            holder.cardCopy.setVisibility(View.GONE);
            holder.cardShare.setVisibility(View.GONE);
            holder.cardDownload.setVisibility(View.VISIBLE);
            holder.cardSpeech.setVisibility(View.VISIBLE);
        }

        holder.cardCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("text to copy", localScrollViewModel.answer);
                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show();
                myClipboard.setPrimaryClip(myClip);
            }
        });
        holder.cardSpeech.setOnClickListener(view -> listener.onItemClickCopy(position, localScrollViewModel.answer));
        holder.cardShare.setOnClickListener(view -> shareIMage(localScrollViewModel.answer, holder.ivImage.getDrawable()));

        holder.cardDownload.setOnClickListener(view -> {
            if (localScrollViewModel.isImage.equals("2")) {
                listener.onItemClick(position, localScrollViewModel.answer);
            } else {
                long currTime = System.currentTimeMillis();

                download(currTime + "", localScrollViewModel.answer);
            }
        });

        holder.itemView.setOnClickListener(view -> {
            Log.e("abc", "=====localScrollViewModel.category_id============" + localScrollViewModel.category_id);
            Log.e("abc", "=====localScrollViewModel.isImage============" + localScrollViewModel.isImage);
            if (localScrollViewModel.isImage.equals("0")) {
                if (localScrollViewModel.category_id.equals("0")) {
                    Intent intent = new Intent(context, NewChatActivity.class);
                    intent.putExtra("cat_id", localScrollViewModel.category_id);
                    intent.putExtra("type", 0);
                    context.startActivity(intent);
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("assistant");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    AssistantModel marketModel = snapshot.getValue(AssistantModel.class);
                                    marketModel.id = snapshot.getKey();
                                    marketModel.suggestion.remove(0);
                                    // Check if suggestion is null before accessing its elements

                                    if (marketModel.cat_id.equals(localScrollViewModel.category_id)) {
                                        Intent intent = new Intent(context, AssistantChatActivity.class);
                                        intent.putExtra("itemModel", marketModel);
                                        context.startActivity(intent);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e("abc", "============onCancelled================" + databaseError.toString());
                        }
                    });
                }

            } else if (localScrollViewModel.isImage.equals("1")) {
                Intent intent = new Intent(context, ImageGenerateActivity.class);
                context.startActivity(intent);

            } else if (localScrollViewModel.isImage.equals("2")) {
                Intent intent = new Intent(context, TextToSpeechActivity.class);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvQuestion;
        private TextView tvAnswer;
        private ImageView ivImage;
        private CardView cardCopy;
        private CardView cardShare;
        private CardView cardDownload;
        private CardView cardSpeech;
        private LinearLayout llSound;
        private CardView card_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
            ivImage = itemView.findViewById(R.id.ivImage);
            cardCopy = itemView.findViewById(R.id.cardCopy);
            cardShare = itemView.findViewById(R.id.cardShare);
            cardDownload = itemView.findViewById(R.id.cardDownload);
            cardSpeech = itemView.findViewById(R.id.cardSpeech);
            llSound = itemView.findViewById(R.id.llSound);
            card_view = itemView.findViewById(R.id.card_view);
        }
    }

    private void download(String FileName, String Url) {
      /*  CustomProgressDialog customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.startLoadingdialog();*/
        Toast.makeText(context, "Downloading...", Toast.LENGTH_LONG).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url + ""));

        request.setTitle("Image Download");
        request.setMimeType("image/jpg");
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName + ".jpg");
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);


        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // your code
//                customProgressDialog.dismissdialog();

                if (!context.isFinishing()) {
                    Toast.makeText(context, "File Downloaded Successfully", Toast.LENGTH_LONG).show();
                    createNotification();
                } else {
                }
            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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
       /* LocalDatabasQueryClass localDatabasQueryClass = new LocalDatabasQueryClass(context);

        long id = localDatabasQueryClass.insertNotification(model);

        Log.e("abc","============id========="+id);
        if (id > 0) {
            model.setId(id);
            Log.e("abc", "===id=====insertExpense=============" + id);
//                        expenseCreateListener.onExpenseCreated(expenseModel);


        }*/
    }

    private void shareIMage(String url, Drawable resource) {
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
