package com.ai.genie.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ai.genie.R;
import com.ai.genie.ui.home.model.NotificationModel;
import com.ai.genie.ui.home.DashBoardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.e("newToken", token);

        Log.e("abc","======MyFirebaseMessagingService===========token========="+token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", token).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            Log.e("abc","==========remoteMessage.getData()========="+remoteMessage.getData());
            Log.e("abc","==========remoteMessage.getTitle()========="+remoteMessage.getNotification().getTitle());
            Log.e("abc","==========remoteMessage.getBody()========="+remoteMessage.getNotification().getBody());
            Log.e("abc","==========remoteMessage.getIcon()========="+remoteMessage.getNotification().getIcon());
            Log.e("abc","==========remoteMessage.getImageUrl()========="+remoteMessage.getNotification().getImageUrl());
            Log.e("abc","==========remoteMessage.getEventTime()========="+remoteMessage.getNotification().getEventTime());

            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }

    }

    public static String getToken(Context context) {
        return context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }



    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);


        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.brainailogo);

        remoteViews.setTextViewText(R.id.tvTime,getTime());
        return remoteViews;
    }


    public void showNotification(String title,
                                 String message) {
        createNotification(title,message);
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, DashBoardActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.drawable.brainailogo)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setOnlyAlertOnce(false)
                .setContentIntent(pendingIntent)
                .setShowWhen(true);




        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));

            Log.e("abc","==========builder 1=============");
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.brainailogo);

            Log.e("abc","==========builder 2=============");

        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }
    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private void createNotification(String title, String message){
        Log.e("abc","============createCat=========");

        NotificationModel model = new NotificationModel();
        model.id=-1;
        model.notification_title=title;
        model.notification_description= message;
        model.date= UsefullData.getDateTime();
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef1.child(userId).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.e("abc","============task.isSuccessful()==============");
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());
                }
            }
        });
      /*  LocalDatabasQueryClass localDatabasQueryClass = new LocalDatabasQueryClass(this);

        long id = localDatabasQueryClass.insertNotification(model);

        Log.e("abc","============id========="+id);
        if (id > 0) {
            model.setId(id);
            Log.e("abc", "===id=====insertExpense=============" + id);
//                        expenseCreateListener.onExpenseCreated(expenseModel);


        }*/
    }
    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
        Calendar cal = Calendar.getInstance();
        // sdf.applyPattern("dd MMM yyyy");
        String strTime = sdf.format(cal.getTime());
        return strTime;

    }
}
