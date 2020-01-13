package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class dashboard extends AppCompatActivity {
    ImageView person_image,log_out, my_queues,back, create_queue, join_queue;
    TextView user_name, user_mode_text,create_text,text_logout;
    CardView create_card,card_logout;
    GoogleSignInClient mGoogleSignInClient;
    String personName,personGivenName,personFamilyName ="" ;
    boolean manager_mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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


        if(manager_mode){ user_mode_text.setText("Manager\nmode"); }
        else{ user_mode_text.setText("Participant\nmode");}


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            personGivenName = user.getDisplayName();
            user_name.setText(personGivenName);
        }


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            personName = acct.getDisplayName();
            personGivenName = acct.getGivenName();
            personFamilyName = acct.getFamilyName();
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(person_image);
            user_name.setText(personGivenName +" "+personFamilyName);
        }

        my_queues = findViewById(R.id.my_queues);
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

        if(!manager_mode) {
            create_queue.setVisibility(View.GONE);
            create_card.setVisibility(View.GONE);
            create_text.setVisibility(View.GONE);
        }

        create_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager_mode){
                    Intent manageractivity = new Intent(dashboard.this, CreateQueue.class);
                    startActivity(manageractivity);
                }
//                else{
//                    create_queue
//                    //Toast.makeText(dashboard.this,"valid only in manger mode",Toast.LENGTH_LONG).show();
//                }
            }
        });
        join_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGroupsView = new Intent(dashboard.this, JoinQueue.class);
                startActivity(toGroupsView);
            }
        });


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
