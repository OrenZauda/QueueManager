package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class dashboardActivity extends AppCompatActivity {
    ImageView personimage,log_out,my_groups_view,back,create_group_bt;
    TextView mydashboard,mode;
    GoogleSignInClient mGoogleSignInClient;
    String personName;
    String personGivenName;
    String personFamilyName;
    boolean manager_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        create_group_bt = findViewById(R.id.create_group_bt);
        manager_mode = (boolean)getIntent().getSerializableExtra("manager_mode");
        mode = findViewById(R.id.mode);

        if(manager_mode){ mode.setText("Manager\nmode"); }
        else{ mode.setText("Participate\nmode");}


        back = findViewById(R.id.back);
        mydashboard = findViewById(R.id.mydashboard);
        personimage = findViewById(R.id.personimage);
        log_out = findViewById(R.id.log_out);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.log_out:
                        signOut();
                        Intent tologin = new Intent(dashboardActivity.this,LoginActivity.class);
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
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Glide.with(this).load(String.valueOf(personPhoto)).into(personimage);

        }
        mydashboard.setText(personGivenName +" "+personFamilyName);
        my_groups_view = findViewById(R.id.my_groups_view);
        my_groups_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tomanangergroup = new Intent(dashboardActivity.this,Managergroupslist.class);
                startActivity(tomanangergroup);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toselectmode = new Intent(dashboardActivity.this,ChooseAthorithyActivity.class);
                startActivity(toselectmode);
            }
        });
        create_group_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(manager_mode){
                    Intent manageractivity = new Intent(dashboardActivity.this,MangerActivity.class);
                    startActivity(manageractivity);
                }
                else{
                    Toast.makeText(dashboardActivity.this,"valid only in manger mode",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(dashboardActivity.this,"signed out successfuly",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
