package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserImage extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ImageView person_image;
    Uri personPhoto;
    LinearLayout upload_image ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_image);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        person_image = findViewById(R.id.person_image);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {

            personPhoto  = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
            person_image.setImageURI(personPhoto);
        }


        else if (user != null) {
            // Name, email address, and profile photo Url
            personPhoto = user.getPhotoUrl();
                Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
                person_image.setImageURI(personPhoto);
        }


    }
}
