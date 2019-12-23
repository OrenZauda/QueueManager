package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChooseAthorithyActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button blogout ;
    ImageView manger_mode,participate_mode,log_out;
    FirebaseAuth mfire;
    private FirebaseAuth.AuthStateListener firelis;
    boolean manager_mode =false;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_athorithy_activity);

        email = (String)getIntent().getSerializableExtra("email");


        manger_mode = findViewById(R.id.manger_mode);
        participate_mode = findViewById(R.id.participate_mode);

        manger_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager_mode = true;
                Intent tomangerscr = new Intent(ChooseAthorithyActivity.this,dashboardActivity.class);
                tomangerscr.putExtra("manager_mode",manager_mode);
                tomangerscr.putExtra("email",email);
                startActivity(tomangerscr);
            }
        });

        participate_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager_mode = false;
                Intent toplayerscr = new Intent(ChooseAthorithyActivity.this,dashboardActivity.class);
                toplayerscr.putExtra("manager_mode",manager_mode);
                toplayerscr.putExtra("email",email);
                startActivity(toplayerscr);
            }
        });



        log_out = findViewById(R.id.log_out);
//        log_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                switch (v.getId()) {
//                    case R.id.logoutbt:
//                        signOut();
//                        Intent tologin = new Intent(ChooseAthorithyActivity.this,LoginActivity.class);
//                        startActivity(tologin);
//                        break;
//                }
//                Intent intomain = new Intent(ChooseAthorithyActivity.this, LoginActivity.class);
//                startActivity(intomain);
//            }
//        });






    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ChooseAthorithyActivity.this,"signed out successfuly",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }
}
