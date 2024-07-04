package com.ai.genie.ui.sidenav.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ai.genie.ui.home.DashBoardActivity;
import com.bumptech.glide.Glide;
import com.ai.genie.R;
import com.ai.genie.databinding.ActivityEditProfileBinding;
import com.ai.genie.ui.authentication.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    int PERMISSION_ALL = 101;
    int REQUEST_IMAGE_PICK = 2;

    String[] PERMISSIONS = {
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.READ_MEDIA_VIDEO,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.CAMERA

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

    private Uri imageUri = null;
    private ProgressDialog progressDialog;
    private  User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
//        setContentView(R.layout.activity_edit_profile);
        String userId = mAuth.getCurrentUser().getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);



        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                // Use user object as needed
                if (user!=null) {
                    if (user.user_type==1){
                        binding.llCompany.setVisibility(View.GONE);
                        binding.llCreator.setVisibility(View.GONE);
                    }else if (user.user_type==2){
                        binding.llCompany.setVisibility(View.GONE);
                        binding.llCreator.setVisibility(View.VISIBLE);
                    }else{
                        binding.llCompany.setVisibility(View.VISIBLE);
                        binding.llCreator.setVisibility(View.GONE);
                    }
                    binding.etEmail.setText(user.email);
                    binding.etName.setText(user.username);
                    binding.etCompanyName.setText(user.company_name);
                    binding.etYourRole.setText(user.role);
                    binding.etPhone.setText(user.phone_number);
                    binding.etBio.setText(user.bio);
                    Glide.with(EditProfileActivity.this).load(user.imageUrl).error(R.drawable.profile).into(binding.ivImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (!hasPermissions(EditProfileActivity.this, PERMISSIONS_33)) {
                        Log.e("abc","=======hasPermissions====================");
                        ActivityCompat.requestPermissions(EditProfileActivity.this, PERMISSIONS_33, PERMISSION_ALL);

                        return;
                    }
                } else {
                    if (!hasPermissions(EditProfileActivity.this, PERMISSIONS)) {
                        Log.e("abc","=======hasPermissions====================");
                        ActivityCompat.requestPermissions(EditProfileActivity.this, PERMISSIONS, PERMISSION_ALL);

                        return;
                    }
                }
                openGallery();
            }
        });

        binding.btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                String full_name = binding.etName.getText().toString();
                String phone_number = binding.etPhone.getText().toString();
                String bio = binding.etBio.getText().toString();
                String company_name = binding.etCompanyName.getText().toString();
                String role = binding.etYourRole.getText().toString();


                if (user.user_type==1) {
                    if (!email.isEmpty() && !full_name.isEmpty() && !phone_number.isEmpty()) {
                        progressDialog.show();
                        if (imageUri!=null) {
                            uploadImageToFirebaseStorage(userId);
                        }else {
                            saveUserDataToDatabase(userId, binding.etName.getText().toString(), binding.etEmail.getText().toString(), user.password, user.imageUrl);

                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                }else if(user.user_type==2){
                    if (!email.isEmpty() &&  !full_name.isEmpty() && !phone_number.isEmpty() && !bio.isEmpty()) {
                        progressDialog.show();
                        if (imageUri!=null) {
                            uploadImageToFirebaseStorage(userId);
                        }else {
                            saveUserDataToDatabase(userId, binding.etName.getText().toString(), binding.etEmail.getText().toString(), user.password, user.imageUrl);

                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (!email.isEmpty() && !full_name.isEmpty() && !phone_number.isEmpty() && !company_name.isEmpty() && !role.isEmpty()) {
                        if (imageUri!=null) {
                            uploadImageToFirebaseStorage(userId);
                        }else {
                            saveUserDataToDatabase(userId, binding.etName.getText().toString(), binding.etEmail.getText().toString(), user.password, user.imageUrl);

                        }
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    }
                }
                /*if (!email.isEmpty() && !full_name.isEmpty()){
                    progressDialog.show();
                    if (imageUri!=null) {
                        uploadImageToFirebaseStorage(userId);
                    }else {
                        saveUserDataToDatabase(userId, binding.etName.getText().toString(), binding.etEmail.getText().toString(), user.password, user.imageUrl);

                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }*/
            }
        });


    }

    private void uploadImageToFirebaseStorage(String userId) {
        if (imageUri != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference imageRef = storageRef.child("profile_images/" + userId + ".jpg");

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    String userId = mAuth.getCurrentUser().getUid();
                                    saveUserDataToDatabase(userId, binding.etName.getText().toString(), binding.etEmail.getText().toString(),user.password, imageUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("abc", "===============exception?.message======1==============" + exception.getMessage());
                            progressDialog.dismiss(); // Dismiss progress dialog
                        }
                    });
        }
    }

    private void saveUserDataToDatabase(String userId, String username, String email, String password, String imageUrl) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.imageUrl = imageUrl;
        if (user.user_type==1){
            user.bio = "";
            user.company_name = "";
            user.role = "";
        }else if (user.user_type==2){
            user.bio = binding.etBio.getText().toString();
            user.company_name = "";
            user.role = "";
        }else{
            user.bio = "";
            user.company_name = binding.etCompanyName.getText().toString();
            user.role = binding.etYourRole.getText().toString();
        }
        databaseRef.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss(); // Dismiss progress dialog

                    Intent intent = new Intent(EditProfileActivity.this, DashBoardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Exception exception = task.getException();
                    Log.e("abc", "===============exception==============" + exception.getMessage());

                    Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss(); // Dismiss progress dialog
                }
            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 2:
                    imageUri = data != null ? data.getData() : null;
                    if (imageUri != null) {
                        Glide.with(this).load(imageUri).into(binding.ivImage);
                    }
                    // Handle the selected image (e.g., upload to Firebase, display in ImageView)
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(getResources().getString(R.string.permissions_required))
                            .setMessage(getResources().getString(R.string.denied_permission))
                            .setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", getPackageName(), null));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .create()
                            .show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}