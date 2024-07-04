package com.ai.genie.ui.home;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ai.genie.Privacy_three_policies;
import com.ai.genie.databinding.ActivityDashBoardBinding;
import com.ai.genie.inapprewardsysytem.inapprewardsysytem.MactivityInApp;
import com.ai.genie.localdatabase.CatModel;
import com.ai.genie.ui.assistant.view.AssistantFragment;
import com.ai.genie.ui.chat.view.ChatFragment;
import com.ai.genie.ui.generate_ai.view.FineTuneActivity;
import com.ai.genie.ui.generate_ai.view.MarketPlaceActivity;
import com.ai.genie.ui.home.view.NotificationActivity;
import com.ai.genie.ui.sidenav.view.BrandVoiceActivity;
import com.ai.genie.ui.sidenav.view.EditProfileActivity;
import com.ai.genie.ui.sidenav.view.FeedbackActivity;
import com.ai.genie.ui.sidenav.view.PlanActivity;
import com.ai.genie.util.AdsManager;
import com.bumptech.glide.Glide;
import com.ai.genie.ui.authentication.view.LoginActivity;
import com.ai.genie.R;
import com.ai.genie.api.API;
import com.ai.genie.common.ConstantValue;
import com.ai.genie.common.SaveData;
import com.ai.genie.common.UsefullData;
import com.ai.genie.localdatabase.LocalDatabasQueryClass;
import com.ai.genie.ui.home.model.NotificationModel;
import com.ai.genie.ui.authentication.model.User;
import com.ai.genie.ui.history.view.HistoryFragment;
import com.ai.genie.ui.home.view.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashBoardActivity extends AppCompatActivity {//implements NavigationView.OnNavigationItemSelectedListener {

    int PERMISSION_ALL = 101;
    private int select_fragment = 1;
    //    private GuideView mGuideView;
//    private GuideView.Builder builder;
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

    private SaveData saveData;
    private LocalDatabasQueryClass localDatabasQueryClass;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
    DatabaseReference databaseRef1 = FirebaseDatabase.getInstance().getReference("notification");
    private ArrayList<NotificationModel> otherList = new ArrayList<>();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private UsefullData usefullData;
    String userId;
    private ActivityDashBoardBinding binding;
    DatabaseReference databaseRef2 = FirebaseDatabase.getInstance().getReference("category");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasPermissions(DashBoardActivity.this, PERMISSIONS_33)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(DashBoardActivity.this, PERMISSIONS_33, PERMISSION_ALL);

            }
        } else {
            if (!hasPermissions(DashBoardActivity.this, PERMISSIONS)) {
                Log.e("abc", "=======hasPermissions====================");
                ActivityCompat.requestPermissions(DashBoardActivity.this, PERMISSIONS, PERMISSION_ALL);

            }
        }
        AdsManager.initSdk(this);
        saveData = new SaveData(this);
        usefullData = new UsefullData(this);
//        startActivity(new Intent(this, SubscriptionActivity.class));
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("abc", "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get new FCM registration token
                String token = task.getResult();
                Log.e("abc", "===============token==============" + token);

                // Log and toast
                       /* String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("abc", msg);
                        Toast.makeText(DashBoardActivity.this, msg, Toast.LENGTH_SHORT).show();*/
            }
        });


        userId = mAuth.getCurrentUser().getUid();
        databaseRef1.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    NotificationModel notificationModel = snapshot.getValue(NotificationModel.class);
                    otherList.add(notificationModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        showAlertSubscription();
/*
        localDatabasQueryClass= new LocalDatabasQueryClass(this);
*/
        if (otherList.size() > 0) {
            binding.tvCount.setVisibility(View.VISIBLE);
            binding.tvCount.setText(otherList.size() + "");
        } else {
            binding.tvCount.setVisibility(View.GONE);

        }
        /*if (saveData.getInt(ConstantValue.DASHBOARD)==0) {

            builder = new GuideView.Builder(this);
            builder.setTitle("Welcome")
                    .setContentText("Let me show how AI can help you :)")
                    .setGravity(Gravity.center)
                    .setDismissType(DismissType.anywhere)
                    .setPointerType(PointerType.circle)
                    .setTargetView(binding.tvTitle)
                    .setContentTextSize(10)
                    .setTitleTextSize(14)
                    .setGuideListener(view -> {
                        if (view.getId() == R.id.tvTitle) {
                            builder.setTargetView(binding.ivMenu).setTitle("Menu")
                                    .setContentText("Get a brief explanation about anything.")
                                    .setTitleTextSize(14).build();
                        } else if (view.getId() == R.id.ivMenu) {
                            builder.setTargetView(binding.ivMarketPlace).setTitle(getResources().getString(R.string.market_place)).setContentText(getResources().getString(R.string.market_place))
                                    .build();
                        } else if (view.getId() == R.id.ivMarketPlace) {
                            builder.setTargetView(binding.flNotification).setTitle(getResources().getString(R.string.notification)).setContentText(getResources().getString(R.string.market_place)).build();

                        }else if (view.getId() == R.id.flNotification) {
                            builder.setTargetView(binding.fabAdd).setTitle(getResources().getString(R.string.add_cat)).setContentText(getResources().getString(R.string.add_cat)).build();

                        } else if (view.getId() == R.id.fabAdd) {
                            saveData.save_int(ConstantValue.DASHBOARD,1);
                            return;

                        }
                        mGuideView = builder.build();
                        mGuideView.show();

                    });

            mGuideView = builder.build();
            mGuideView.show();
        }*/
        binding.ivMenu.setOnClickListener(v -> closeDrawer());


        binding.ivMarketPlace.setOnClickListener(v -> startActivity(new Intent(DashBoardActivity.this, MarketPlaceActivity.class)));


        binding.flNotification.setOnClickListener(view -> startActivity(new Intent(DashBoardActivity.this, NotificationActivity.class)));

        binding.fabAdd.setOnClickListener(view -> show());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        TextView tvUser = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUser);
        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvEmail);
        CircleImageView ivImage = navigationView.getHeaderView(0).findViewById(R.id.ivImage);
        LinearLayout llProfile = navigationView.getHeaderView(0).findViewById(R.id.llProfile);
        LinearLayout llPlans = navigationView.getHeaderView(0).findViewById(R.id.llPlans);
        LinearLayout llBrandVoice = navigationView.getHeaderView(0).findViewById(R.id.llBrandVoice);
        LinearLayout llFeedback = navigationView.getHeaderView(0).findViewById(R.id.llFeedback);
        LinearLayout llFAQ = navigationView.getHeaderView(0).findViewById(R.id.llFAQ);
        LinearLayout llAboutApp = navigationView.getHeaderView(0).findViewById(R.id.llAboutApp);
        LinearLayout llRewards = navigationView.getHeaderView(0).findViewById(R.id.llRewards);
        TextView tvLogOut = navigationView.getHeaderView(0).findViewById(R.id.tvLogOut);

        llProfile.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        llPlans.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, PlanActivity.class);
            startActivity(intent);
        });

        llBrandVoice.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, BrandVoiceActivity.class);
            startActivity(intent);
        });

        llFeedback.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FeedbackActivity.class);
            startActivity(intent);
        });

        llFAQ.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(API.POLICY_URL));
            startActivity(intent);
        });

        llAboutApp.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this,Privacy_three_policies.class);
            startActivity(intent);
        });
        llRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, MactivityInApp.class);
                startActivity(intent);
            }
        });

        tvLogOut.setOnClickListener(view -> logout());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            userId = mAuth.getCurrentUser().getUid();

            databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    // Use user object as needed
                    if (user != null) {
                        txtProfileName.setText(user.email);
                        tvUser.setText(user.username);
                        Glide.with(DashBoardActivity.this).load(user.imageUrl).error(R.drawable.profile).into(ivImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });

            // Start the MainActivity and finish the current SplashActivity
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.home);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // as soon as the application opens the first
        // fragment should be shown to the user
        // in this case it is algorithm fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        // By using switch we can easily get
        // the selected fragment
        // by using there id.
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            selectedFragment = new HomeFragment();
            select_fragment = 1;
        } else if (itemId == R.id.document) {
            selectedFragment = new AssistantFragment();
            select_fragment = 2;
        } else if (itemId == R.id.chat) {
            selectedFragment = new ChatFragment();
            select_fragment = 3;
        } else if (itemId == R.id.history) {
            selectedFragment = new HistoryFragment();
            select_fragment = 4;
        }
        // It will help to replace the
        // one fragment to other.
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
        return true;
    };

    public void closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navEditProfile) {
            Intent intent = new Intent(this,EditProfileActivity.class);
            startActivity(intent);
        }else if (id == R.id.navSubscription) {
            Intent intent = new Intent(this,SubscriptionActivityNew.class);
            startActivity(intent);
        }else if (id == R.id.navPlans) {
            Intent intent = new Intent(this,PlanActivity.class);
            startActivity(intent);
        }else if (id == R.id.navBrandVoice) {
            Intent intent = new Intent(this,BrandVoiceActivity.class);
            startActivity(intent);
        }else if (id == R.id.navTheme) {
            showBottomSheet();
        }else if (id == R.id.navHelp) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(API.POLICY_URL));
            startActivity(intent);
        } else if (id == R.id.navTutorial) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/@Alli-in-OneGptOfficial-bt8cf"));
            startActivity(intent);

        }  else if (id == R.id.navShareApp) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            // Add data to the intent, the receiving app will decide
            // what to do with it.
            share.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            share.putExtra(Intent.EXTRA_TEXT, "Please Download my generative ai app App from Google Play: https://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

            startActivity(Intent.createChooser(share, "Share link!"));

        } else if (id == R.id.navRateUs) {
            final String appPackageName = getApplicationContext().getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe
            ) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (id == R.id.navPrivacyPolicy) {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));

        }else if (id == R.id.navLogOut) {
            logout();
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }*/

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        super.onBackPressed();
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (select_fragment != 1) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                binding.bottomNavigation.setSelectedItemId(R.id.home);
            } else {
                showAlert();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    askNotificationPermission();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permissions_required)).setMessage(getResources().getString(R.string.denied_permission)).setPositiveButton(getResources().getString(R.string.settings), (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    });

    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    private void showAlert() {
        LayoutInflater factory = LayoutInflater.from(DashBoardActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_confirmation, null);
        final android.app.AlertDialog deleteDialog = new android.app.AlertDialog.Builder(DashBoardActivity.this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView tvMessage = deleteDialogView.findViewById(R.id.tvMessage);
        tvMessage.setText("Do you want to exit the app?");
        TextView tvYes = deleteDialogView.findViewById(R.id.tvYes);
        TextView tvNo = deleteDialogView.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view1 -> {
            finish();
            deleteDialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            deleteDialog.dismiss();
        });
        deleteDialog.show();
    }

    private void logout() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirmation, null, false);

        TextView tvYes = view.findViewById(R.id.tvYes);
        TextView tvNo = view.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view1 -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            dialog.dismiss();
        });
        tvNo.setOnClickListener(view1 -> {

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    private void showBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_theme);
        ImageView ivBack = bottomSheetDialog.findViewById(R.id.ivBack);
        RadioButton radio1 = bottomSheetDialog.findViewById(R.id.radio1);
        RadioButton radio2 = bottomSheetDialog.findViewById(R.id.radio2);
        RadioButton radio3 = bottomSheetDialog.findViewById(R.id.radio3);
        if (saveData.getInt(ConstantValue.THEME_MODE) == 1) {
            radio2.setChecked(true);
        } else if (saveData.getInt(ConstantValue.THEME_MODE) == 2) {
            radio3.setChecked(true);
        } else {
            radio1.setChecked(true);

        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save_int(ConstantValue.THEME_MODE, 0);
                usefullData.setTheme(DashBoardActivity.this);
                recreateUI();

            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save_int(ConstantValue.THEME_MODE, 1);
                usefullData.setTheme(DashBoardActivity.this);
                recreateUI();

            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData.save_int(ConstantValue.THEME_MODE, 2);
                usefullData.setTheme(DashBoardActivity.this);
                recreateUI();
            }
        });

        bottomSheetDialog.show();
    }


    private void recreateUI() {

        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public void show() {
        BottomSheetDialog bottomSelectDialog = new BottomSheetDialog(this);
        bottomSelectDialog.setContentView(R.layout.select_cat_dialog);

        TextView tvGenerateAi = bottomSelectDialog.findViewById(R.id.tvGenerateAi);
        TextView tvViewCat = bottomSelectDialog.findViewById(R.id.tvViewCat);
        ImageView ivClose = bottomSelectDialog.findViewById(R.id.ivClose);
        tvViewCat.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, MarketPlaceActivity.class);
            startActivity(intent);
            bottomSelectDialog.dismiss();

        });
        tvGenerateAi.setOnClickListener(view -> {
            show3();
            bottomSelectDialog.dismiss();
        });

        ivClose.setOnClickListener(view -> bottomSelectDialog.dismiss());
        bottomSelectDialog.show();
    }

    public void show3() {
        BottomSheetDialog bottomSelectDialog = new BottomSheetDialog(this);
        bottomSelectDialog.setContentView(R.layout.select_use_template);
        TextView tvUseTemplate = bottomSelectDialog.findViewById(R.id.tvUseTemplate);

        AppCompatImageView ivClose = bottomSelectDialog.findViewById(R.id.ivClose);

        // Click listener for "Create Fine Tune"
        tvUseTemplate.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FineTuneActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            bottomSelectDialog.dismiss();
        });

        ivClose.setOnClickListener(view -> bottomSelectDialog.dismiss());

        bottomSelectDialog.show();
    }
    public void show2() {
        BottomSheetDialog bottomSelectDialog = new BottomSheetDialog(this);
        bottomSelectDialog.setContentView(R.layout.select_cat_dialog);
        TextView tvGenerateAi = bottomSelectDialog.findViewById(R.id.tvGenerateAi);
        TextView tvViewCat = bottomSelectDialog.findViewById(R.id.tvViewCat);
        tvViewCat.setText(getResources().getString(R.string.create_fine_tune));
        tvGenerateAi.setText(getResources().getString(R.string.use_template));
        ImageView ivClose = bottomSelectDialog.findViewById(R.id.ivClose);

        // Click listener for "Create Fine Tune"
        tvViewCat.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FineTuneActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            bottomSelectDialog.dismiss();
        });

        // Click listener for "Generate AI"
        tvGenerateAi.setOnClickListener(view -> {
            bottomSelectDialog.dismiss(); // Dismiss current dialog
            showSelectUseTemplateDialog();
        });

        ivClose.setOnClickListener(view -> bottomSelectDialog.dismiss());

        bottomSelectDialog.show();
    }


    private void showSelectUseTemplateDialog() {
        BottomSheetDialog selectUseTemplateDialog = new BottomSheetDialog(this);
        selectUseTemplateDialog.setContentView(R.layout.select_use_template);
        ImageView ivClose = selectUseTemplateDialog.findViewById(R.id.ivClose);
        TextView tvUseTemplate = selectUseTemplateDialog.findViewById(R.id.tvUseTemplate);

        // Click listener for "Use Template" in select_use_template.xml
        tvUseTemplate.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FineTuneActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
            selectUseTemplateDialog.dismiss();
        });

        ivClose.setOnClickListener(view -> selectUseTemplateDialog.dismiss());

        selectUseTemplateDialog.show();
    }


    private void showSelectCatDialog() {
        BottomSheetDialog selectCatDialog = new BottomSheetDialog(this);
        selectCatDialog.setContentView(R.layout.select_cat_dialog);
        TextView tvGenerateAi = selectCatDialog.findViewById(R.id.tvGenerateAi);
        TextView tvViewCat = selectCatDialog.findViewById(R.id.tvViewCat);
        tvViewCat.setText(getResources().getString(R.string.create_fine_tune));
        tvGenerateAi.setText(getResources().getString(R.string.use_template));
        ImageView ivClose = selectCatDialog.findViewById(R.id.ivClose);

        // Click listener for "Create Fine Tune"
        tvViewCat.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FineTuneActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
            selectCatDialog.dismiss();
        });

        // Click listener for "Generate AI"
        tvGenerateAi.setOnClickListener(view -> {
            Intent intent = new Intent(DashBoardActivity.this, FineTuneActivity.class);
            intent.putExtra("type", 2);
            startActivity(intent);
            selectCatDialog.dismiss();
        });

        ivClose.setOnClickListener(view -> selectCatDialog.dismiss());

        selectCatDialog.show();
    }

    private void show1() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.ad_cat_dialog);


        TextView tvGenerate = bottomSheetDialog.findViewById(R.id.tvGenerate);
        TextInputEditText etTitle = bottomSheetDialog.findViewById(R.id.etTitle);
        TextInputEditText etInstruction = bottomSheetDialog.findViewById(R.id.etInstruction);
        ImageView ivClose = bottomSheetDialog.findViewById(R.id.ivClose);
        tvGenerate.setOnClickListener(view -> {
            if (etTitle.getText().toString().trim().length() > 0 || etInstruction.getText().toString().trim().length() > 0) {
                long currTime = System.currentTimeMillis();
//                    CatModel catModel = new CatModel(-1,etTitle.getText().toString().trim()+currTime,etTitle.getText().toString().trim(),etInstruction.getText().toString().trim());
                CatModel catModel = new CatModel();
                catModel.id = -1;
                catModel.category_id = etTitle.getText().toString().trim() + currTime;
                catModel.category_name = etTitle.getText().toString().trim();
                catModel._instruction = etInstruction.getText().toString().trim();
                createCat(catModel);
                bottomSheetDialog.dismiss();
            }
        });

        ivClose.setOnClickListener(view -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void createCat(CatModel catModel) {
        Log.e("abc", "============createCat=========");

//        CatModel model = new CatModel(-1,catModel.category_id, catModel.category_name,catModel._instruction);
        CatModel model = new CatModel();
        model.id = -1;
        model.category_id = catModel.category_id;
        model.category_name = catModel.category_name;
        model._instruction = catModel._instruction;
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef2.child(userId).push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    }

    private void showAlertSubscription() {
        LayoutInflater factory = LayoutInflater.from(DashBoardActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.dialog_notify_subscription, null);
        final android.app.AlertDialog deleteDialog = new android.app.AlertDialog.Builder(DashBoardActivity.this).create();
        deleteDialog.setView(deleteDialogView);
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        ImageView ivBack = deleteDialogView.findViewById(R.id.ivBack);
        TextView tvPlan = deleteDialogView.findViewById(R.id.tvPlan);

        ivBack.setOnClickListener(view1 -> {
            deleteDialog.dismiss();
        });
        tvPlan.setOnClickListener(view1 -> {
            Intent intent = new Intent(this, PlanActivity.class);
            startActivity(intent);
            deleteDialog.dismiss();
        });

        deleteDialog.show();
    }
}