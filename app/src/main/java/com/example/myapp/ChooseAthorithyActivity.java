package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseAthorithyActivity extends AppCompatActivity {

    Button blogout , btmanager,btplayer ;

    FirebaseAuth mfire;
    private FirebaseAuth.AuthStateListener firelis;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_athorithy_activity);

        btmanager = findViewById(R.id.btmanager);
        btplayer = findViewById(R.id.btplayer);

        btmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tomangerscr = new Intent(ChooseAthorithyActivity.this,MangerActivity.class);
                startActivity(tomangerscr);
            }
        });

        btplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toplayerscr = new Intent(ChooseAthorithyActivity.this,PlayerActivity.class);
                startActivity(toplayerscr);
            }
        });



       blogout = findViewById(R.id.btlogout);
        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intomain = new Intent(ChooseAthorithyActivity.this, registerActivity.class);
                startActivity(intomain);
            }
        });






    }
}
