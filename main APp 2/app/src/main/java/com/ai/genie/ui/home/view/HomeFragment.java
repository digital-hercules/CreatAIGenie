package com.ai.genie.ui.home.view;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.genie.R;
import com.ai.genie.ui.generate_ai.adapter.MarketPlaceOtherAdapter;
import com.ai.genie.ui.home.adapter.UseMostRecentAdapter;
import com.ai.genie.common.ProgressManager;
import com.ai.genie.common.SaveData;
import com.ai.genie.databinding.FragmentHomeBinding;
import com.ai.genie.listner.ClickListner;
import com.ai.genie.localdatabase.CatModel;
import com.ai.genie.localdatabase.LocalDatabasQueryClass;
import com.ai.genie.ui.home.model.SliderModel;
import com.ai.genie.ui.home.model.UseMostRecentModel;
import com.ai.genie.ui.features.featuresfoto.picker.PhotoPicker;
import com.ai.genie.ui.features.featuresfoto.puzzle.photopicker.activity.PickImageActivity;
import com.ai.genie.util.AdsManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    //    private GuideView mGuideView;
//    private GuideView.Builder builder;
    private SaveData saveData;
    public static TextView tvClear;
    private ArrayList<CatModel> otherList = new ArrayList<>();
    private ArrayList<UseMostRecentModel> useMostRecentList = new ArrayList<>();
    ArrayList<SliderModel> sliderDataArrayList = new ArrayList<>();

    private LocalDatabasQueryClass localDatabasQueryClass;
    MarketPlaceOtherAdapter adapter;
    UseMostRecentAdapter adapter1;
    public static boolean display_delete = false;
    DatabaseReference databaseRef2 = FirebaseDatabase.getInstance().getReference("category");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private DatabaseReference databaseReference;
    int PERMISSION_ALL = 101;

    String[] PERMISSIONS = {android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_VIDEO, android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA

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
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permissions_required)).setMessage(getResources().getString(R.string.denied_permission)).setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getActivity().getPackageName(), null));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tvClear = view.findViewById(R.id.tvClear);
        getCategory();

        saveData = new SaveData(getActivity());

      /*  if (saveData.getInt(ConstantValue.DASHBOARD)==1) {
            if (saveData.getInt(ConstantValue.HOME)==0) {
                builder = new GuideView.Builder(getActivity());
                builder.setTitle("Welcome")
                        .setContentText(getResources().getString(R.string.chat))
                        .setGravity(Gravity.center)
                        .setDismissType(DismissType.anywhere)
                        .setPointerType(PointerType.circle)
                        .setTargetView(binding.cardView1)
                        .setContentTextSize(10)
                        .setTitleTextSize(14)
                        .setGuideListener(view1 -> {
                            if (view1.getId() == R.id.card_view1) {
                                builder.setTargetView(binding.cardView2).setTitle(getResources().getString(R.string.generate_image))
                                        .setContentText("Get a brief explanation about anything.")
                                        .setTitleTextSize(14).build();
                            } else if (view1.getId() == R.id.card_view2) {
                                builder.setTargetView(binding.cardView3).setTitle(getResources().getString(R.string.text_to_video)).setContentText(getResources().getString(R.string.text_to_video))
                                        .build();
                            } else if (view1.getId() == R.id.card_view3) {
                                builder.setTargetView(binding.cardView4).setTitle(getResources().getString(R.string.text_to_music)).setContentText(getResources().getString(R.string.text_to_music)).build();

                            } else if (view1.getId() == R.id.card_view4) {
                                builder.setTargetView(binding.cardView5).setTitle(getResources().getString(R.string.text_to_speech)).setContentText(getResources().getString(R.string.text_to_speech)).build();

                            } else if (view1.getId() == R.id.card_view5) {
                                builder.setTargetView(binding.cardView6).setTitle(getResources().getString(R.string.generate_image_caption)).setContentText(getResources().getString(R.string.generate_image_caption)).build();

                            } else if (view1.getId() == R.id.card_view6) {
                                builder.setTargetView(binding.cardView7).setTitle(getResources().getString(R.string.speech_rate)).setContentText(getResources().getString(R.string.speech_rate)).build();

                            } else if (view1.getId() == R.id.card_view7) {
                                builder.setTargetView(binding.cardView8).setTitle(getResources().getString(R.string.video_editor)).setContentText(getResources().getString(R.string.video_editor)).build();

                            }  else if (view1.getId() == R.id.card_view8) {
                                saveData.save_int(ConstantValue.HOME,1);

                                return;
                            }
                            mGuideView = builder.build();
                            mGuideView.show();

                        });

                mGuideView = builder.build();
                mGuideView.show();
            }
        }
*/
        binding.rvMarketPlaceOther.setHasFixedSize(true);
        binding.rvMarketPlaceOther.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new MarketPlaceOtherAdapter(getActivity(), otherList, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {
            }

            @Override
            public void onItemClick(int pos, String question) {
                showAlert(pos, question);
            }
        });
        binding.rvMarketPlaceOther.setAdapter(adapter);

        binding.rvUseMostRecent.setHasFixedSize(true);
        binding.rvUseMostRecent.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter1 = new UseMostRecentAdapter(getActivity(), useMostRecentList, new ClickListner() {
            @Override
            public void onItemClickCopy(int pos, String answer) {
            }

            @Override
            public void onItemClick(int pos, String question) {
                showAlert(pos, question);
            }
        });
        binding.rvUseMostRecent.setAdapter(adapter1);
        SliderView sliderView = view.findViewById(R.id.slider);

       /* SliderModel sliderModel = new SliderModel();
        sliderDataArrayList.add(sliderModel);
        sliderDataArrayList.add(sliderModel);
        sliderDataArrayList.add(sliderModel);*/

        databaseReference = FirebaseDatabase.getInstance().getReference().child("slider");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        SliderModel marketModel = snapshot.getValue(SliderModel.class);
                        marketModel.id = snapshot.getKey();
                        sliderDataArrayList.add(marketModel);
                    }
                    SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);
                    // below method is used to set auto cycle direction in left to
                    // right direction you can change according to requirement.
                    sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
                    sliderView.setSliderAdapter(adapter);
                    sliderView.setScrollTimeInSec(5);
                    sliderView.setAutoCycle(true);
                    sliderView.startAutoCycle();
                    if (sliderDataArrayList.size() > 0) {
                        sliderView.setVisibility(View.VISIBLE);
                    } else {
                        sliderView.setVisibility(View.GONE);

                    }
                } else {
                    sliderView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("abc", "============onCancelled================" + databaseError.toString());

            }
        });


        view.findViewById(R.id.btnEditor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(getActivity(), PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        requestPermissions(PERMISSIONS_33, PERMISSION_ALL);

                    } else {
                        PhotoPicker.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(getActivity());

                    }
                } else {
                    if (!hasPermissions(getActivity(), PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);

                    } else {
                        PhotoPicker.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(getActivity());

                    }
                }

            }
        });

        view.findViewById(R.id.btnCollage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(getActivity(), PERMISSIONS_33)) {
                        Log.e("abc", "=======hasPermissions====================");
                        requestPermissions(PERMISSIONS_33, PERMISSION_ALL);

                    } else {
                        Intent intent = new Intent(getActivity(), PickImageActivity.class);
                        intent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 9);
                        intent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        startActivityForResult(intent, 1001);
                    }
                } else {
                    if (!hasPermissions(getActivity(), PERMISSIONS)) {
                        Log.e("abc", "=======hasPermissions====================");
                        requestPermissions(PERMISSIONS, PERMISSION_ALL);

                    } else {
                        Intent intent = new Intent(getActivity(), PickImageActivity.class);
                        intent.putExtra(PickImageActivity.KEY_LIMIT_MAX_IMAGE, 9);
                        intent.putExtra(PickImageActivity.KEY_LIMIT_MIN_IMAGE, 2);
                        startActivityForResult(intent, 1001);
                    }
                }

            }
        });

        binding.cardView1.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), NewChatActivity.class);
            intent.putExtra("cat_id", "0");
            intent.putExtra("type", 0);
            startActivity(intent);
            AdsManager.showInterstitial(getActivity());
//                startActivity(new Intent(getActivity(), NewChatActivity.class));
        });


        binding.cardView2.setOnClickListener(view12 -> {
            startActivity(new Intent(getActivity(), ImageGenerationActivity.class));
            AdsManager.showInterstitial(getActivity());

        });


        binding.cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TextToVideoActivity.class));
//                ProgressManager.notify(getActivity());
                AdsManager.showInterstitial(getActivity());

            }
        });

        binding.cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProgressManager.notify(getActivity());

                startActivity(new Intent(getActivity(), TextToMusicActivity.class));
                AdsManager.showInterstitial(getActivity());

            }
        });


        binding.cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TextToSpeechActivity.class));
                AdsManager.showInterstitial(getActivity());

            }
        });

        binding.cardView6.setOnClickListener(view13 -> {
//                startActivity(new Intent(getActivity(), NewChatActivity.class));
            ProgressManager.notify(getActivity());

            /*Intent intent = new Intent(getActivity(),NewChatActivity.class);
            intent.putExtra("cat_id","0");
            intent.putExtra("type","0");
            startActivity(intent);*/
        });

        binding.cardView7.setOnClickListener(view14 -> {
//                startActivity(new Intent(getActivity(), NewChatActivity.class));
            Intent intent = new Intent(getActivity(), VoiceActivity.class);
            startActivity(intent);
            AdsManager.showInterstitial(getActivity());
        });

        binding.cardView8.setOnClickListener(view15 -> {
            ProgressManager.notify(getActivity());
          /*  Intent intent = new Intent(getActivity(), VideoMainActivity.class);
            startActivity(intent);*/
        });

        tvClear.setOnClickListener(view16 -> {
            display_delete = false;
            adapter.notifyDataSetChanged();
            tvClear.setVisibility(View.GONE);
        });

        binding.tvMostTrending.setOnClickListener(view17 -> {
            if (binding.rvUseMostRecent.getVisibility() == View.VISIBLE) {
                binding.rvUseMostRecent.setVisibility(View.GONE);
            } else {
                binding.rvUseMostRecent.setVisibility(View.VISIBLE);
                binding.nested.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });
        binding.tvGeneratedAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.rvMarketPlaceOther.getVisibility() == View.VISIBLE) {
                    binding.rvMarketPlaceOther.setVisibility(View.GONE);
                } else {
                    binding.rvMarketPlaceOther.setVisibility(View.VISIBLE);
                    binding.nested.fullScroll(NestedScrollView.FOCUS_DOWN);

                }
            }
        });

        binding.llScroll1.setOnClickListener(view18 -> {

            binding.llScroll1.setBackground(getResources().getDrawable(R.drawable.main_bg));
            binding.tvScroll1.setTextColor(getResources().getColor(R.color.white_text));

            binding.llScroll2.setBackground(getResources().getDrawable(R.drawable.black_border));
            binding.llScroll3.setBackground(getResources().getDrawable(R.drawable.black_border));
            binding.tvScroll2.setTextColor(getResources().getColor(R.color.black));
            binding.tvScroll3.setTextColor(getResources().getColor(R.color.black));


            binding.llSelect1.setVisibility(View.VISIBLE);
            binding.llSelect2.setVisibility(View.VISIBLE);
            binding.llSelect3.setVisibility(View.VISIBLE);
            binding.tvGeneratedAi.setVisibility(View.VISIBLE);

        });
        binding.llScroll2.setOnClickListener(view19 -> {
            binding.llScroll2.setBackground(getResources().getDrawable(R.drawable.main_bg));
            binding.tvScroll2.setTextColor(getResources().getColor(R.color.white_text));

            binding.llScroll1.setBackground(getResources().getDrawable(R.drawable.black_border));
            binding.llScroll3.setBackground(getResources().getDrawable(R.drawable.black_border));
            binding.tvScroll3.setTextColor(getResources().getColor(R.color.black));
            binding.tvScroll1.setTextColor(getResources().getColor(R.color.black));


            binding.llSelect2.setVisibility(View.VISIBLE);
            binding.rvUseMostRecent.setVisibility(View.VISIBLE);
            binding.llSelect1.setVisibility(View.GONE);
            binding.llSelect3.setVisibility(View.GONE);


        });
        binding.llScroll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.llScroll3.setBackground(getResources().getDrawable(R.drawable.main_bg));
                binding.tvScroll3.setTextColor(getResources().getColor(R.color.white_text));

                binding.llScroll2.setBackground(getResources().getDrawable(R.drawable.black_border));
                binding.llScroll1.setBackground(getResources().getDrawable(R.drawable.black_border));
                binding.tvScroll2.setTextColor(getResources().getColor(R.color.black));
                binding.tvScroll1.setTextColor(getResources().getColor(R.color.black));


                binding.llSelect3.setVisibility(View.VISIBLE);
                binding.rvMarketPlaceOther.setVisibility(View.VISIBLE);
                binding.llSelect1.setVisibility(View.GONE);
                binding.llSelect2.setVisibility(View.GONE);
                binding.tvGeneratedAi.setVisibility(View.GONE);

            }
        });


        binding.cardView9.setOnClickListener(view110 -> {
            Intent intent = new Intent(getActivity(), ChatWithImageActivity.class);
            startActivity(intent);
            AdsManager.showInterstitial(getActivity());
        });
        return view;
    }


    private void showAlert(int pos, String cat) {

        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(R.layout.dialog_confirmation, null);
        final android.app.AlertDialog deleteDialog = new android.app.AlertDialog.Builder(getActivity()).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView tvMessage = deleteDialogView.findViewById(R.id.tvMessage);
        tvMessage.setText("Do you want to delete this ai categories?");
        TextView tvYes = deleteDialogView.findViewById(R.id.tvYes);
        TextView tvNo = deleteDialogView.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view1 -> {
            String userId = mAuth.getCurrentUser().getUid();

            databaseRef2.child(userId).child(cat).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Category successfully deleted
                    // You can add any additional logic here
                    otherList.remove(pos);
                    adapter.notifyItemRemoved(pos);
                    if (otherList.size() > 0) {
                        binding.llOther.setVisibility(View.VISIBLE);
                    } else {
                        binding.llOther.setVisibility(View.GONE);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle errors
                }
            });
           /* if (localDatabasQueryClass.deleteOtherCat(cat)==1){
                Log.e("abc","=========deleteScrollProductByRegNum============"+localDatabasQueryClass.deleteScrollProductByRegNum(cat));

                otherList.remove(pos);
                adapter.notifyItemRemoved(pos);
                if (otherList.size()>0){
                    llOther.setVisibility(View.VISIBLE);
                }else {
                    llOther.setVisibility(View.GONE);

                }
                   *//* adapter.notifyItemRemoved(pos);
                    adapter.notifyDataSetChanged();*//*
            }*/
            deleteDialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            deleteDialog.dismiss();
        });


        deleteDialog.show();
    }


    private void getCategory() {
        otherList.clear();
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef2.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve and process each history item
                    Log.e("abc", "======snapshot=======" + snapshot);
                    Log.e("abc", "=============" + snapshot.child("title"));
                    CatModel catModel = snapshot.getValue(CatModel.class);
                    otherList.add(catModel);
                    // Process the data as needed
                }
                if (otherList.size() > 0) {
                    binding.llOther.setVisibility(View.VISIBLE);
                } else {
                    binding.llOther.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (otherList.size() > 0) {
                    binding.llOther.setVisibility(View.VISIBLE);
                } else {
                    binding.llOther.setVisibility(View.GONE);
                }
                // Handle errors
            }
        });
    }

}
