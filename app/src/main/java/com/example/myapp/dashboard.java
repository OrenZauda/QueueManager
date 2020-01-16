package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

public class dashboard extends AppCompatActivity {
    ImageView person_image,log_out, my_queues,back, create_queue, join_queue;
    TextView user_name, user_mode_text,create_text,text_logout;
    CardView create_card,card_logout;
    GoogleSignInClient mGoogleSignInClient;
    String personName,personGivenName,personFamilyName ="" ;
    Uri personPhoto;
    boolean manager_mode;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static boolean returnto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


//You can add some extra conditions here if you want.

        text_logout = findViewById(R.id.text_logout);
        card_logout = findViewById(R.id.card_logout);
        create_text=findViewById(R.id.create_text);
        create_card = findViewById(R.id.create_card);
        join_queue = findViewById(R.id.join_queue);
        create_queue = findViewById(R.id.create_queue);
        manager_mode = (boolean)getIntent().getSerializableExtra("manager_mode");
        user_mode_text = findViewById(R.id.user_mode);
        back = findViewById(R.id.back);
        user_name = findViewById(R.id.user_name);
        person_image = findViewById(R.id.personimage);
        log_out = findViewById(R.id.log_out);
        my_queues = findViewById(R.id.my_queues);







        //get google user account
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        if(manager_mode)
            //set text to participant mode
            user_mode_text.setText("Manager\nmode");
        else{
            //set text to participant mode
            user_mode_text.setText("Participant\nmode");
            //remove the create queue button from dash board
            create_queue.setVisibility(View.GONE);
            create_card.setVisibility(View.GONE);
            create_text.setVisibility(View.GONE);
        }


        //get google user  account information
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
            user_name.setText(personGivenName +" "+personFamilyName);
        }
        //get fire base  user information
        else if (user != null) {
            // Name, email address, and profile photo Url
            personGivenName = user.getDisplayName();
            personPhoto = user.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
            user_name.setText(personGivenName);
        }




        my_queues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent toMyQueues= new Intent(dashboard.this, MyQueues.class);
                    toMyQueues.putExtra("manager mode",manager_mode);
                    startActivity(toMyQueues);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toselectmode = new Intent(dashboard.this, SelectMode.class);
                startActivity(toselectmode);
            }
        });

        create_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent manageractivity = new Intent(dashboard.this, CreateQueue.class);
                   manageractivity.putExtra("manager_mode",manager_mode);
                    startActivity(manageractivity);


            }
        });
        join_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toJoinQueue = new Intent(dashboard.this, JoinQueue.class);
                toJoinQueue.putExtra("manager_mode",manager_mode);
                startActivity(toJoinQueue);
            }
        });


        person_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard.this, UserImage.class);
                i.putExtra("manager_mode",manager_mode);
                View sharedView = person_image;
                String transitionName = "small image";

                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(dashboard.this, sharedView, transitionName);
                startActivity(i, transitionActivityOptions.toBundle());

            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                switch (v.getId()) {
                    case R.id.log_out:
                        signOut();
                        Intent tologin = new Intent(dashboard.this, Login.class);
                        startActivity(tologin);
                        break;
                }
            }
        });

    }
    public Uri getImageUri(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), src, "title", null);
        return Uri.parse(path);
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(dashboard.this,"signed out successfuly",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }




}
