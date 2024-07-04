package com.ai.genie.ui.history.view;


import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.genie.R;
import com.ai.genie.ui.history.adapter.HistoryAdapter;
import com.ai.genie.common.UsefullData;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.LocalDatabasQueryClass;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HistoryFragment extends Fragment {


    RecyclerView rvHistory;
    ArrayList<LocalScrollViewModel> messageList = new ArrayList<>();
    private LocalDatabasQueryClass localDatabasQueryClass;
    private MediaPlayer mMediaPlayer;

    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("history");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private int currentPage = 0;
    private int pageSize = 10; // Number of items to fetch per page
    String userId;
    HistoryAdapter historyAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
       /* localDatabasQueryClass= new LocalDatabasQueryClass(getActivity());
        messageList.addAll(localDatabasQueryClass.getHistory());*/
//        Collections.reverse(messageList);
        userId = mAuth.getCurrentUser().getUid();
        // Get the current date
        Date currentDate = Calendar.getInstance().getTime();

// Calculate the date one week ago
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date oneWeekAgo = calendar.getTime();

// Format dates to strings (adjust the format based on how your timestamp is stored)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm aa", Locale.getDefault());
        String currentDateString = dateFormat.format(currentDate);
        String oneWeekAgoString = dateFormat.format(oneWeekAgo);

        rvHistory = view.findViewById(R.id.rvHistory);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(linearLayoutManager);
        historyAdapter = new HistoryAdapter(getActivity(), messageList, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.release();
                    play(answer);
                } else {
                    play(answer);
                }

            }

            @Override
            public void onItemClick(int pos, String question) {
                long currTime = System.currentTimeMillis();
                download(currTime + "", question);
            }
        });
        rvHistory.setAdapter(historyAdapter);

       /* rvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    // If end of list is reached, fetch more data
                    currentPage++; // Increment page count
                    fetchMoreData(currentPage);
                }
            }
        });
*/
        // Fetch initial data
        fetchMoreData(currentPage);
      /*  databaseRef.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e("abc","======== dataSnapshot.getChildren()============="+ dataSnapshot.getChildren());
                        Log.e("abc","=======onDataChange============="+dataSnapshot.getValue());

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            // Retrieve and process each history item
                            Log.e("abc","======== snapshot============="+snapshot);

                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                Log.e("abc","======== snapshot1)============="+snapshot1);


                                LocalScrollViewModel catModel = snapshot1.getValue(LocalScrollViewModel.class);
                                if (catModel.category_id!=null) {
                                    messageList.add(catModel);
                                }
                            }
                            // Process the data as needed
                        }
                        Collections.sort(messageList, new Comparator<LocalScrollViewModel>() {
                            DateFormat f = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
                            @Override
                            public int compare(LocalScrollViewModel lhs, LocalScrollViewModel rhs) {
                                try {

                                    return f.parse(lhs.date_time).compareTo(f.parse(rhs.date_time));
                                } catch (ParseException e) {
                                    throw new IllegalArgumentException(e);
                                }
                            }
                        });
                        Collections.reverse(messageList);
                        historyAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // Handle errors
                    }
                });
*/

        return view;
    }

    private void fetchMoreData(int page) {
        // Calculate the starting point for the next page
        int offset = page * pageSize;

        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear messageList before adding new data
                Log.e("abc", "======dataSnapshot===========" + dataSnapshot);
                messageList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        LocalScrollViewModel catModel = snapshot1.getValue(LocalScrollViewModel.class);
                        if (catModel.category_id != null) {
                            messageList.add(catModel);
                        }
                    }
                }
                Collections.sort(messageList, new Comparator<LocalScrollViewModel>() {
                    DateFormat f = new SimpleDateFormat("dd MMM yyyy HH:mm aa");

                    @Override
                    public int compare(LocalScrollViewModel lhs, LocalScrollViewModel rhs) {
                        try {

                            return f.parse(lhs.date_time).compareTo(f.parse(rhs.date_time));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                });
                Collections.reverse(messageList);
                // Notify adapter that data set has changed
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void download(String FileName, String Url) {
        Toast.makeText(getActivity(), "Downloading...", Toast.LENGTH_LONG).show();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(Url + ""));
        Log.e("abc", "======request======" + request);
        Log.e("abc", "======downloadUrl======" + Url);
        Log.e("abc", "======fileName======" + FileName);
        request.setTitle(FileName);
//        request.setMimeType(downloadType);
        request.allowScanningByMediaScanner();
        request.setAllowedOverMetered(true);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, FileName + ".mp3");
        DownloadManager dm = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);


        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                // your code
//                customProgressDialog.dismissdialog();

                Log.e("abc", "===========isFinishing()============" + getActivity().isFinishing());
                if (!getActivity().isFinishing()) {
                    Toast.makeText(getActivity(), "File Downloaded Successfully", Toast.LENGTH_LONG).show();
                    createNotification();

                } else {

                    //                            Toast.makeText(PlaneDetailsActivity.this,"File Downloaded Successfully", Toast.LENGTH_LONG).show();
                }
                Log.e("abc", "========onComplete=============");
            }
        };
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private void createNotification() {
        Log.e("abc", "============createCat=========");
        NotificationModel model = new NotificationModel();
        model.id = -1;
        model.notification_title = "Downloaded Speech";
        model.notification_description = "Text to speech download Successfully";
        model.date = UsefullData.getDateTime();
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
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
       /* LocalDatabasQueryClass localDatabasQueryClass = new LocalDatabasQueryClass(getActivity());

        long id = localDatabasQueryClass.insertNotification(model);

        Log.e("abc","============id========="+id);
        if (id > 0) {
            model.setId(id);
            Log.e("abc", "===id=====insertExpense=============" + id);
//                        expenseCreateListener.onExpenseCreated(expenseModel);


        }*/
    }

    private void play(String answer) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(answer);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log.e("abc", "=======onCompletion==========");
                }
            });
            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (Exception e) {
            Log.e("abc", "========Exception========" + e);
        }
    }


}
