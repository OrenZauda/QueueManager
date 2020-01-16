package com.example.myapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import android.database.Cursor;
import android.graphics.Bitmap;

import android.os.Environment;


import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;


import id.zelory.compressor.Compressor;
import me.drakeet.materialdialog.MaterialDialog;


public class UserImage extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ImageView person_image ;
    Uri personPhoto;
    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    LinearLayout upload_image ;

    FirebaseUser user;
    boolean manager_mode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);
        upload_image = findViewById(R.id.upload_layout);
        user= FirebaseAuth.getInstance().getCurrentUser();
        person_image = findViewById(R.id.person_image);
        manager_mode = (boolean)getIntent().getSerializableExtra("manager_mode");



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            upload_image.setVisibility(View.GONE);
            personPhoto  = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
            person_image.setImageURI(personPhoto);
        }


        else if (user != null) {
            personPhoto = user.getPhotoUrl();
                Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
                person_image.setImageURI(personPhoto);
        }




        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasWriteStoragePermission = 0;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteStoragePermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_STORAGE);
                    }
                    //return;
                }

                listDialogue();
            }
        });



    }

    public void listDialogue(){
        final ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        arrayAdapter.add("Take Photo");
        arrayAdapter.add("Select Gallery");

        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding(0, dpAsPixels, 0, dpAsPixels);
        listView.setDividerHeight(0);
        listView.setAdapter(arrayAdapter);

        final MaterialDialog alert = new MaterialDialog(this).setContentView(listView);

        alert.setPositiveButton("Cancel", new View.OnClickListener() {
            @Override public void onClick(View v) {
                alert.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){

                    alert.dismiss();
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "propic.jpg";
                    personPhoto = Uri.parse(root);
                    //i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(i, CAPTURE_PHOTO);

                }else {

                    alert.dismiss();
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);

                }
            }
        });

        alert.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    Uri imageUri = imageReturnedIntent.getData();
                    String selectedImagePath = getPath(imageUri);
                    File f = new File(selectedImagePath);
                    Bitmap bmp = Compressor.getDefault(this).compressToBitmap(f);

                    person_image.setImageBitmap(bmp);

                    Uri uri =getImageUri(bmp, Bitmap.CompressFormat.JPEG,100);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("my log", "User profile updated.");
                                    }
                                }
                            });



                }
                break;

            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {

                    Bitmap bmp = imageReturnedIntent.getExtras().getParcelable("data");
                    person_image.setImageBitmap(bmp);

                    Uri uri =getImageUri(bmp, Bitmap.CompressFormat.JPEG,100);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(uri)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("my log", "User profile updated.");
                                    }
                                }
                            });

                    Intent toDashBoard = new Intent(UserImage.this,dashboard.class);
                    toDashBoard.putExtra("manager_mode",manager_mode);
                    startActivity(toDashBoard);

                }

                break;
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null,
                null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "title", null);
        return Uri.parse(path);
    }

}
